package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GScreenShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.HTTPAssetLoader;
import com.ss.gameLogic.objects.Wheel;
import java.util.ArrayList;

public class StartScene extends GScreen {
    private Group group = new Group();
    private Group gr = new Group();
    private Group gCrossPanel;
    private ArrayList<Group> lsBgIcon;
    private boolean checkWhell=false;

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        GStage.addToLayer(GLayer.ui,group);
        GStage.addToLayer(GLayer.top,gr);
        SoundEffect.Playmusic(1);
//        GMain.platform.ShowBanner(true);
        TextureAtlasC.initAtlas();
        BitmapFontC.initBitmapFont();
        render();
    }

    @Override
    public void run() {

    }
    private void render(){
        Config.loadjson();
        Image bg = GUI.createImage(TextureAtlasC.uigame,"bgStart");
        bg.setWidth(Config.ScreenW);
        bg.setHeight(Config.ScreenH);
        group.addActor(bg);
        //////// btn new Game//////
        Image btnNewGame = GUI.createImage(TextureAtlasC.uigame,"btnNewGame");
        btnNewGame.setOrigin(Align.center);
        btnNewGame.setPosition(Config.ScreenW/2,Config.ScreenH/2+btnNewGame.getHeight(), Align.center);
        group.addActor(btnNewGame);
        //////// btn Continus//////
        Image btnContinus = GUI.createImage(TextureAtlasC.uigame,"btnContinus");
        btnContinus.setOrigin(Align.center);
        btnContinus.setPosition(Config.ScreenW/2,btnNewGame.getY()+btnContinus.getHeight()*1.8f, Align.center);
        group.addActor(btnContinus);
        //////// btn Rank//////
        Image btnRank = GUI.createImage(TextureAtlasC.uigame,"btnRank");
        btnRank.setOrigin(Align.center);
        btnRank.setPosition(Config.ScreenW/2,btnContinus.getY()+btnRank.getHeight()*1.8f, Align.center);
        group.addActor(btnRank);
        //////// btn minigame /////
        Image btnMini = GUI.createImage(TextureAtlasC.uigame,"btnMini");
        btnMini.setPosition(Config.ScreenW/2,Config.ScreenH-btnMini.getHeight()/2,Align.center);
        group.addActor(btnMini);
        ////// event btn//////
        eventBtnNewGame(btnNewGame);
        eventBtnContinus(btnContinus);
        SetStatus(btnContinus);
        AniBtn(btnNewGame,btnContinus,btnRank);
        //////// whell mini game ///////
        WheelItem();
        eventBtnMini(btnMini);
        ///////// btn gameOther//////
        Image btnGmOther = GUI.createImage(TextureAtlasC.uigame,"btnGameOther");
        btnGmOther.setOrigin(Align.center);
        btnGmOther.setPosition(Config.ScreenW-btnGmOther.getWidth()*0.8f,Config.ScreenH-btnGmOther.getHeight()/2,Align.center);
        group.addActor(btnGmOther);
        eventBtnGmOther(btnGmOther);
        AnibtnGm(btnGmOther);

    }
    private void AnibtnGm(Image btn){
        btn.addAction(Actions.sequence(
                Actions.scaleTo(0.8f,0.8f,0.2f),
                Actions.scaleTo(1f,1f,0.2f),
                Actions.delay(1),
                Actions.moveBy(-5,0,0.1f,Interpolation.bounceIn),
                Actions.moveBy(10,0,0.1f,Interpolation.bounceIn),
                Actions.moveBy(-10,0,0.1f,Interpolation.bounceIn),
                Actions.moveBy(10,0,0.1f,Interpolation.bounceIn),
                Actions.moveBy(-5,0,0.1f,Interpolation.bounceIn),
                GSimpleAction.simpleAction((d,a)->{
                    AnibtnGm(btn);
                    return true;
                }),
                Actions.delay(1)
                ));
    }
    private void eventBtnGmOther(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                initCrossPanel();
            }
        });
    }
    private void eventBtnNewGame(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                GMain.prefs.putBoolean("isnewgame",false);
                GMain.prefs.putInteger("Level",1);
                GMain.prefs.putLong("Score",0);
                GMain.prefs.flush();
                setScreen(new GameScene());
            }
        });
    }
    private void eventBtnContinus(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                setScreen(new GameScene());
            }
        });
    }
    private void SetStatus(Image btn){
        boolean check = GMain.prefs.getBoolean("isnewgame");
        if(check==false){
            btn.setColor(Color.DARK_GRAY);
            btn.setTouchable(Touchable.disabled);
        }
    }
    private void AniBtn(Image btn1,Image btn2,Image btn3){
        btn1.addAction(Actions.sequence(
            Actions.scaleTo(0.9f,0.9f,0.4f),
            Actions.scaleTo(1f,1f,0.4f),
            GSimpleAction.simpleAction((d,a)->{
                btn2.addAction(Actions.sequence(
                    Actions.scaleTo(0.9f,0.9f,0.4f),
                    Actions.scaleTo(1f,1f,0.4f),
                    GSimpleAction.simpleAction((d1,a1)->{
                        btn3.addAction(Actions.sequence(
                            Actions.scaleTo(0.9f,0.9f,0.4f),
                            Actions.scaleTo(1f,1f,0.4f),
                            Actions.delay(1),
                            GSimpleAction.simpleAction((d2,a2)->{
                                AniBtn(btn1,btn2,btn3);
                                return true;
                            })
                        ));

                        return true;
                    })
                ));
                return true;
            })
        ));

    }
    private void eventBtnMini(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                gr.setVisible(true);
                gr.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));
//                WheelItem();
            }
        });
    }
    private void WheelItem(){
        FileHandle js = Gdx.files.internal("data/wheel.json");
        String jsonStr = js.readString();
        String jv2 = GMain.platform.GetConfigStringValue("skills",jsonStr);
        JsonReader json = new JsonReader();
        JsonValue jv = json.parse(jv2);
        gr.setVisible(false);
        gr.setPosition(Config.ScreenW/2,Config.ScreenH/2);
        gr.setScale(0);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.8f);
        gr.addActor(blackOverlay);
        ////// frm wheel/////
        Image frmWheel = GUI.createImage(TextureAtlasC.uigame,"frmWheel");
        frmWheel.setScale(1.1f);
        frmWheel.setOrigin(Align.center);
        frmWheel.setPosition(0,0,Align.center);
        gr.addActor(frmWheel);
        ///////// Wheel ///////////
        Wheel.wheelTex = TextureAtlasC.uigame.findRegion("whell");
        Wheel.wheelTex.flip(false,true);
//        Wheel.wheelTick = Gdx.audio.newSound(Gdx.files.internal("sound/wheel_sound.mp3"));
        Wheel.wheelTick = GAssetsManager.getSound("wheel_sound.mp3");

        Wheel.pointer = TextureAtlasC.WhellAtlas.findRegion("pointer");
        Wheel.pointer.flip(false,true);
        Wheel.wheelDot = TextureAtlasC.WhellAtlas.findRegion("dot");
        Wheel.lightDot = TextureAtlasC.WhellAtlas.findRegion("lightdot");
        Wheel.wheelText = BitmapFontC.FontAlert;
        Wheel.TEXT_SPACE = 5f;
        Wheel.PARTITION = 6;
        Wheel.ITEM_SCALE= 0.7f;
        Wheel.ITEM_FLOAT= 0.5f;
        for (JsonValue ob: jv){
            Wheel.wheelItems.add(Wheel.WheelItem.newInst(TextureAtlasC.Fottergame.findRegion(
                    ob.get("region").asString()),
                    ob.get("id").asInt(),
                    ob.get("quantity").asInt(),
                    ""+ob.get("quantity").asInt(),
                    ob.get("percent").asInt()));
//
        }
//        Wheel.wheelItems.add(Wheel.WheelItem.newInst(TextureAtlasC.Fottergame.findRegion("SoVang"), 0, 2, "800", 1000));
        //////// listener ////////
        Wheel.inst().setWheelListener(new Wheel.EventListener() {
            @Override
            public boolean start() {
                if(checkWhell==false)
                    AddmoreWheel();
                else
                    blackOverlay.setTouchable(Touchable.disabled);
                return checkWhell;
            }

            @Override
            public void end(Wheel.WheelItem item) {
                blackOverlay.setTouchable(Touchable.enabled);
                Gdx.app.log("output item", item.getRegion() + "");
                showFrmBonus(item.getRegion().toString().trim(),item.getQty());

            }

            @Override
            public void error(String msg) {
                Gdx.app.log("Wheel error", msg);
            }
        });
        Wheel.inst().init();
        Wheel.inst().setPosition(-Wheel.wheelTex.getRegionWidth()/2,-Wheel.wheelTex.getRegionWidth()/2);
        gr.addActor(Wheel.inst());
        /////// pointer/////
        Image pointer = GUI.createImage(TextureAtlasC.uigame,"kim");
        pointer.setPosition(0,0,Align.center);
        gr.addActor(pointer);
        ////////// Alert/////////
        Label arlert = new Label(C.lang.alert,new Label.LabelStyle(BitmapFontC.FontAlert,Color.GOLD));
        arlert.setFontScale(0.6f);
        arlert.setAlignment(Align.center);
        arlert.setOrigin(Align.center);
        arlert.setPosition(0,-Wheel.wheelTex.getRegionHeight()/2-200,Align.center);
        gr.addActor(arlert);
        ////////// Alert tutorial
        Group grText = new Group();
        Label notice = new Label(C.lang.lbtoturial,new Label.LabelStyle(BitmapFontC.FontAlert, null));
        notice.setFontScale(0.6f);
        notice.setAlignment(Align.center);
        notice.setOrigin(Align.center);
        notice.setPosition(0,0,Align.center);
        grText.addActor(notice);
        grText.setWidth(notice.getPrefWidth());
        grText.setHeight(notice.getPrefHeight());
        grText.setPosition(0,Wheel.wheelTex.getRegionHeight()/2+200);
        gr.addActor(grText);
        AniText(grText);
        blackOverlay.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                gr.setVisible(false);
                gr.addAction(Actions.scaleTo(0,0,0.5f, Interpolation.swingOut));

            }
        });
    }
    private void AniText(Group lb){
        lb.addAction(Actions.sequence(
                Actions.scaleTo(1.1f,1.1f,0.5f),
                Actions.scaleTo(1f,1f,0.5f),
                GSimpleAction.simpleAction((d,a)->{
                    AniText(lb);
                    return true;
                })
        ));
    }
    private void showFrmBonus(String type, int quan){
        SetBonus(type,quan);
        checkWhell=false;
        SoundEffect.Play(SoundEffect.Unlock);
        Group grBonus = new Group();
        GStage.addToLayer(GLayer.top,grBonus);
        grBonus.setPosition(Config.ScreenW/2,Config.ScreenH/2);
        grBonus.setScale(0);
        grBonus.addAction(Actions.scaleTo(1,1,0.5f,Interpolation.bounceOut));
        ///// frm bonus /////
        Image frm = GUI.createImage(TextureAtlasC.uigame,"frmBonus");
        frm.setPosition(0,0,Align.center);
        grBonus.addActor(frm);
        /////// particle///////
        effectWin ef = new effectWin(4,0,0,0);
        grBonus.addActor(ef);
        /////// icon //////
        Image icon  = GUI.createImage(TextureAtlasC.Fottergame,type);
        icon.setPosition(0,0,Align.center);
        grBonus.addActor(icon);
        /////// quantity bonus /////
        Label LbQuan = new Label(""+quan,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        LbQuan.setFontScale(0.6f);
        LbQuan.setOrigin(Align.center);
        LbQuan.setAlignment(Align.center);
        LbQuan.setPosition(0,icon.getY()+icon.getHeight()*0.9f,Align.center);
        grBonus.addActor(LbQuan);
        grBonus.addActor(frm);
        /////// particle///////
        effectWin ef2 = new effectWin(5,0,0,0);
        grBonus.addActor(ef2);
        ef2.start();
        ///// btn ok /////
        Image btnOk = GUI.createImage(TextureAtlasC.uigame,"btnOk");
        btnOk.setPosition(0,frm.getHeight()/2-btnOk.getHeight(),Align.center);
        grBonus.addActor(btnOk);
        btnOk.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                grBonus.clear();
                grBonus.remove();
            }
        });
    }
    private void SetBonus(String type,int quan){
        if(type.equals("icon1")){
            System.out.println("color");
            int color=  GMain.prefs.getInteger("color",3);
            color+=quan;
            GMain.prefs.putInteger("color",color);
            GMain.prefs.flush();

        }else if(type.equals("icon2")){
            System.out.println("roket");
            int rocket=  GMain.prefs.getInteger("rocket",3);
            rocket+=quan;
            GMain.prefs.putInteger("rocket",rocket);
            GMain.prefs.flush();

        }if(type.equals("icon3")) {
            System.out.println("bom");
            int bom = GMain.prefs.getInteger("bom", 3);
            bom += quan;
            GMain.prefs.putInteger("bom", bom);
            GMain.prefs.flush();
        }
    }
    private void initCrossPanel() {
        lsBgIcon = new ArrayList<>();
        gCrossPanel = new Group();
        GStage.addToLayer(GLayer.top,gCrossPanel);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.8f);
        gCrossPanel.addActor(blackOverlay);
        gCrossPanel.setPosition(Config.ScreenW/2,Config.ScreenH/2);
        gCrossPanel.setScale(0);
        gCrossPanel.addAction(Actions.scaleTo(1,1,0.3f,Interpolation.swingOut));

        Image bgCross = GUI.createImage(TextureAtlasC.uigame, "frmGameOther");
//        gCrossPanel.setSize(bgCross.getWidth(), bgCross.getHeight());
//        gCrossPanel.setOrigin(Align.center);
        bgCross.setPosition(0,0,Align.center);
        gCrossPanel.addActor(bgCross);
        ////////btn close////////
        Image btnExit = GUI.createImage(TextureAtlasC.uigame,"btnExit");
        btnExit.setPosition(bgCross.getWidth()/2-btnExit.getWidth()/2,-bgCross.getHeight()/2+btnExit.getHeight()/2,Align.center);
        gCrossPanel.addActor(btnExit);
        btnExit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                gCrossPanel.clear();
                gCrossPanel.remove();
            }
        });
        ///////////// label////////
        Label text = new Label(C.lang.altGmother,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        text.setAlignment(Align.center);
        text.setPosition(0,-bgCross.getHeight()*0.6f,Align.center);
        gCrossPanel.addActor(text);
        for (int i=0; i<2; i++) {
            for (int j=0;j<2;j++){
                Group g = new Group();
                Image bgIcon = GUI.createImage(TextureAtlasC.uigame, "bg_icon_cross");
                g.setSize(bgIcon.getWidth(), bgIcon.getHeight());
                g.setPosition(bgCross.getX() + 40 + 312*j, bgCross.getY() + bgCross.getHeight()/2 - bgIcon.getHeight()*i + 30);
                g.addActor(bgIcon);
                gCrossPanel.addActor(g);
                lsBgIcon.add(g);
            }

        }
//        gStartScene.addActor(gCrossPanel);
        ArrayList<HTTPAssetLoader.LoadItem> loadItems = new ArrayList<>();
        JsonReader jReader = new JsonReader();
        JsonValue jValue = jReader.parse(Config.otherGameData);
        for (JsonValue v : jValue)
            loadItems.add(HTTPAssetLoader.LoadItem.newInst(
                    v.get("id").asInt(),
                    v.get("url").asString(),
                    v.get("display_name").asString(),
                    v.get("android_store_uri").asString(),
                    v.get("ios_store_uri").asString(),
                    v.get("fi_store_uri").asString()
            ));
        HTTPAssetLoader.inst().init(new HTTPAssetLoader.Listener() {
            @Override
            public void finish(ArrayList<HTTPAssetLoader.LoadItem> loadedItems) {
                for (HTTPAssetLoader.LoadItem item : loadedItems){
                    Group gIcon = lsBgIcon.get(loadedItems.indexOf(item));
                    Image actor = new Image(new TextureRegionDrawable(item.getItemTexture()));
                    actor.setSize(gIcon.getWidth(), gIcon.getHeight()/1.8f);
                    actor.setScale(0.7f, -0.7f);
                    actor.setOrigin(Align.center);
                    actor.setY(30);
                    gIcon.addActor(actor);
                    gCrossPanel.addActor(gIcon);
                    Label lbName = new Label(item.getDisplayName(), new Label.LabelStyle(BitmapFontC.FontAlert, Color.GOLD));
                    lbName.setAlignment(Align.center);
                    lbName.setFontScale(.5f);
                    lbName.setPosition(actor.getX() + actor.getWidth()/2 - lbName.getWidth()/2,
                            actor.getY() + actor.getHeight());
                    gIcon.addActor(lbName);
                    gIcon.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            SoundEffect.Play(SoundEffect.click);
                            Gdx.net.openURI(item.getAndroidStoreURI());
                        }
                    });
                }
            }
            @Override
            public void error(Throwable e) {
                e.printStackTrace();
            }
        }, loadItems);
    }
    public void AddmoreWheel(){
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.8f);
        group.addActor(blackOverlay);
        group.setScaleX(0);
        group.setOrigin(Align.center);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
        group.addAction(Actions.scaleTo(1,1,0.3f, Interpolation.swingOut));
        ////// Label watch //////
        Label text = new Label(C.lang.lbwatch1,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        text.setFontScale(0.5f);
        text.setOrigin(Align.center);
        text.setAlignment(Align.center);
        text.setPosition(0,-100,Align.center);
        group.addActor(text);
        //// button  watch ////
        Image btnWath = GUI.createImage(TextureAtlasC.uigame,"btnWatch");
        btnWath.setOrigin(Align.center);
        btnWath.setPosition(0,btnWath.getHeight(),Align.center);
        group.addActor(btnWath);
        AnibtnGm(btnWath);
        ///// eventBtnWatch ////
        btnWath.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showAds(group);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        //// button  close ////
        blackOverlay.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                group.clear();
                group.remove();
            }
        });


    }
    void showAds(Group group){
        if(GMain.platform.isVideoRewardReady()) {
            GMain.platform.ShowVideoReward((boolean success) -> {
                if (success) {
                    checkWhell=true;
                    group.clear();
                    group.remove();
                    SoundEffect.Play(SoundEffect.ChangeColor);


                }else {
                    group.clear();
                    group.remove();
                }
            });
        }else {
            Label notice = new Label("Kiểm tra kết nối",new Label.LabelStyle(BitmapFontC.robotoVi, Color.RED));
            notice.setPosition(0,0,Align.center);
            group.addActor(notice);
            notice.addAction(Actions.sequence(
                    Actions.moveBy(0,-50,0.5f),
                    GSimpleAction.simpleAction((d, a)->{
                        notice.clear();
                        notice.remove();
                        return true;
                    })
            ));

        }
    }


}
