package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.interfaces.SelectMode;

public class fotter {
    public Group group = new Group();
    public Group gr = new Group();
    public Image fotter;
    private Array<Image> arrItem = new Array<>();
    private Array<Image> arrbtnAddmore = new Array<>();
    private Array<Label> arrLb = new Array<>();
    private Array<String> arrLbStr = new Array<>();
    private Array<Image> arrIcon = new Array<>();
    public Array<Label> arrLbQuantity = new Array<>();
    public Array<Integer> arrQuantity = new Array<>();
    private int quantity=3;
    private int Zindex=0;
    private int touch=0;
    private int index=0;
    private GShapeSprite gShape;
    private Header header;
    private Image light,frm;
    public fotter(Header header){
        this.header = header;
        GStage.addToLayer(GLayer.top,group);
//        GStage.addToLayer(GLayer.top,gr);
        header.group.addActor(gr);
        render();
//        GMain.prefs.putInteger("color",3);
//        GMain.prefs.putInteger("rocket",3);
//        GMain.prefs.putInteger("bom",3);
//        GMain.prefs.flush();
        group.setWidth(Config.ScreenW);
        group.setHeight(fotter.getHeight());
        group.setPosition(Config.ScreenW/2-group.getWidth()/2,Config.ScreenH-fotter.getHeight());
        eventItem();
        setAddmore();
        renderTutorial();

    }
    private void render(){
        fotter = GUI.createImage(TextureAtlasC.Fottergame,"fotter");
        fotter.setPosition(fotter.getWidth()/2,0);
        group.addActor(fotter);
        ////// render item //////
        for(int i=0;i<3;i++){
            Image item = GUI.createImage(TextureAtlasC.Fottergame,"item"+(i+1));
            item.setPosition(Config.ScreenW/2+item.getWidth()*(i+1),fotter.getY()+fotter.getHeight()*0.6f,Align.center);
            group.addActor(item);
            ////////// label /////////
            Label LbQuantity = new Label(""+quantity,new Label.LabelStyle(BitmapFontC.NerwynOrange,null));
            LbQuantity.setFontScale(1f);
            LbQuantity.setOrigin(Align.center);
            LbQuantity.setAlignment(Align.center);
            LbQuantity.setPosition(item.getX()+item.getWidth()-30,item.getY()+item.getHeight()-10,Align.center);
            group.addActor(LbQuantity);
            ////////// btn addmore/////
            Image btnAddmore = GUI.createImage(TextureAtlasC.Fottergame,"addMore");
            btnAddmore.setOrigin(Align.center);
            btnAddmore.setPosition(item.getX()+item.getWidth()-30,item.getY()+item.getHeight()-10,Align.center);
            group.addActor(btnAddmore);
            //////////array//////
            arrItem.add(item);
            arrLbQuantity.add(LbQuantity);
            arrQuantity.add(quantity);
            arrbtnAddmore.add(btnAddmore);
        }

    }
    private void eventItem(){
        for(Image ob: arrItem){
            ob.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    SoundEffect.Play(SoundEffect.click);
                    if(touch==0){
                        if(arrbtnAddmore.get(arrItem.indexOf(ob,true)).isVisible()==true){
                            System.out.println("het skilll!!");
                            AddmoreSkill(arrItem.indexOf(ob,true));
                            setAddmore();
                        }else {
                            touch++;
                            index=arrItem.indexOf(ob,true);
                            SetMode(arrItem.indexOf(ob,true));
                            visibleItem(arrItem.indexOf(ob,true));
                            arrLbQuantity.get(index).setVisible(false);
                            showfrmTutorial(index,true);
                        }

                    }else if(touch==1) {
                       setDone();
                        Config.selectMode = SelectMode.AREA;
                        showfrmTutorial(index,false);

                    }

                }
            });
        }
    }
    public void visibleItem(int index){
        gShape = new GShapeSprite();
        gShape.createRectangle(true, 0, 0, group.getWidth(), group.getHeight());
        group.addActor(gShape);
        gShape.setColor(0, 0, 0, 0.5f);
        Zindex = arrItem.get(index).getZIndex();
        arrItem.get(index).setZIndex(1000);
    }
    public void setDone(){
        arrLbQuantity.get(index).setVisible(true);
        touch=0;
        gShape.clear();
        gShape.remove();
        arrItem.get(index).setZIndex(Zindex);
    }
    public void setQuantitySkill(int index){
        for (Label lb :arrLbQuantity){
            if(arrLbQuantity.indexOf(lb,true)==index)
            {
                arrQuantity.set(index,arrQuantity.get(index)-1);
                arrLbQuantity.get(index).setText(arrQuantity.get(index));

            }
        }
        setAddmore();
        GMain.prefs.putInteger("color",arrQuantity.get(0));
        GMain.prefs.putInteger("rocket",arrQuantity.get(1));
        GMain.prefs.putInteger("bom",arrQuantity.get(2));
        GMain.prefs.flush();
    }
    public void setAddmore(){
        for(int i=0;i<arrQuantity.size;i++){
            if(arrQuantity.get(i)>0){
                arrbtnAddmore.get(i).setVisible(false);
                arrLbQuantity.get(i).setVisible(true);
            }else {
                arrbtnAddmore.get(i).setVisible(true);
                arrLbQuantity.get(i).setVisible(false);

            }
        }

    }

    private void SetMode(int index){
        if(index==0){
            Config.selectMode = SelectMode.CHANGECOLOR;
        }else if(index==1){
            Config.selectMode = SelectMode.VERTICAL;
        }else if(index==2){
            Config.selectMode = SelectMode.SQUARE;
        }
    }
    public void UpdateSkill(int index, int quantity){
        arrQuantity.set(index,arrQuantity.get(index)+quantity);
        arrLbQuantity.get(index).setText(""+arrQuantity.get(index));

    }
    public void LoadTexSkill(int index, int quantity){
        arrQuantity.set(index,quantity);
        arrLbQuantity.get(index).setText(""+arrQuantity.get(index));

    }
    public void loadSkill(){
        int color=  GMain.prefs.getInteger("color",3);
        int rocket=  GMain.prefs.getInteger("rocket",3);
        int bomb=  GMain.prefs.getInteger("bom",3);
        LoadTexSkill(0,color);
        LoadTexSkill(1,rocket);
        LoadTexSkill(2,bomb);
        setAddmore();


    }
    private void renderTutorial(){
        arrLbStr.add(C.lang.lbSkill1,C.lang.lbSkill2,C.lang.lbSkill3);
        frm = GUI.createImage(TextureAtlasC.Fottergame,"frm");
        frm.setWidth(Config.ScreenW);
        frm.setPosition(header.group.getX(),header.group.getY()-frm.getHeight());
        gr.addActor(frm);
        frm.setVisible(false);
        ///////// label ///////
        for (int i=0;i<3;i++){
            Label lb = new Label(arrLbStr.get(i),new Label.LabelStyle(BitmapFontC.FontAlert,null));
            lb.setFontScale(0.45f);
            lb.setOrigin(Align.center);
            lb.setAlignment(Align.center);
            lb.setPosition(Config.ScreenW/2+lb.getPrefWidth()/5,frm.getY()+frm.getHeight()/4,Align.center);
            lb.setVisible(false);
            gr.addActor(lb);
            arrLb.add(lb);
        }
        light = GUI.createImage(TextureAtlasC.Fottergame,"light");
        light.setScale(1.5f);
        light.setOrigin(Align.center);
        light.setPosition(frm.getX()+140,frm.getY()+frm.getHeight()/2,Align.center);
        gr.addActor(light);
        light.setVisible(false);
        for(int i=0;i<3;i++){
            Image icon = GUI.createImage(TextureAtlasC.Fottergame,"icon"+(i+1));
            icon.setPosition(light.getX()+light.getWidth()/2,light.getY()+light.getHeight()/2,Align.center);
            gr.addActor(icon);
            icon.setVisible(false);
            arrIcon.add(icon);

        }
        ActionLight();
    }
    private void ActionLight(){
        light.addAction(Actions.sequence(
                Actions.rotateBy(360,10f),
                GSimpleAction.simpleAction((d,a)->{
                    ActionLight();
                    return true;
                })
        ));
    }
    public void showfrmTutorial(int index,boolean turn){
        for (int i=0;i<arrIcon.size;i++){
            if(index==i){
                arrLb.get(i).setVisible(true);
                arrIcon.get(i).setVisible(true);
            }else {
                arrLb.get(i).setVisible(false);
                arrIcon.get(i).setVisible(false);
            }
        }
        if(turn==true) {
            frm.setVisible(true);
            gr.setVisible(true);
            gr.addAction(Actions.sequence(
                    Actions.moveBy(0, frm.getHeight(), 0.5f),
                    GSimpleAction.simpleAction((d, a) -> {
                        light.setVisible(true);
                        return true;
                    })
            ));
        }
        else {
            gr.addAction(Actions.sequence(
                    Actions.moveBy(0,-frm.getHeight(),0.5f),
                    GSimpleAction.simpleAction((d,a)->{
                        light.setVisible(false);
                        gr.setVisible(false);
                        return true;
                    })
            ));
        }

    }
    public void AddmoreSkill(int index){
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
        Label text = new Label(C.lang.lbwatch,new Label.LabelStyle(BitmapFontC.FontAlert,null));
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
                showAds(group,index);
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
    void showAds(Group group,int index){
        if(GMain.platform.isVideoRewardReady()) {
            GMain.platform.ShowVideoReward((boolean success) -> {
                if (success) {
//                    GMain.platform.ShowFullscreen();
                    UpdateSkill(index,1);
                    setAddmore();
                    group.clear();
                    group.remove();
                    SoundEffect.Play(SoundEffect.ChangeColor);


                }else {
//                    GMain.platform.ShowFullscreen();
                    group.clear();
                    group.remove();
                }
            });
        }else {
            Label notice = new Label(C.lang.lbCheckConnect,new Label.LabelStyle(BitmapFontC.robotoVi, Color.RED));
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




}
