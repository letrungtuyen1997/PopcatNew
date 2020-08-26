package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.Header;
import com.ss.gameLogic.objects.Pop;
import com.ss.gameLogic.objects.Setting;
import com.ss.gameLogic.objects.fotter;
import com.ss.interfaces.DisplayElement;

import java.util.ArrayList;

public class GameScene extends GScreen {
    public Group MainGroup = new Group();
    private Group grSetting = new Group();
    private ArrayList<DisplayElement> elements = new ArrayList<>();
    private Header header;
    private fotter fotter;
    private Image bg;


    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        checkconnect();
        Config.Score = GMain.prefs.getLong("Score");
        Config.Level = GMain.prefs.getInteger("Level");
        initGroup();
        renderBg();
        header = new Header();
        fotter = new fotter(header);
        new Board(Config.Level,header,fotter,this);
        ///////////// setting/////////
        Image frm = GUI.createImage(TextureAtlasC.Fottergame,"frmSetting");
        frm.setPosition(frm.getWidth()*0.7f,fotter.group.getY()+frm.getHeight()/2+5, Align.center);
        grSetting.addActor(frm);
        GStage.addToLayer(GLayer.top,grSetting);
        Image btnSetting = GUI.createImage(TextureAtlasC.Fottergame,"btnPause");
        btnSetting.setPosition(frm.getX()+frm.getWidth()/2,frm.getY()+frm.getHeight()/2, Align.center);
        grSetting.addActor(btnSetting);
        btnSetting.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                new Setting(grSetting,btnSetting,GameScene.this);
                //UpdateViewPort();
            }
        });
        if(Config.checkConnet){
            UpdateViewPort();
            GMain.platform.ShowBanner(true);
        }
    }
    private void UpdateViewPort(){
//        Config.paddingY*=-1;
        for (int i=0;i<header.group.getChildren().size;i++){
            header.group.getChildren().get(i).addAction(Actions.moveBy(0,Config.paddingY));
        }
        bg.setWidth(Config.ScreenW);
        bg.setHeight(Config.ScreenH-Config.paddingY);
        bg.setPosition(0,Config.paddingY);
       // System.out.println("checkY: "+header.LbScore.getX());

    }
    private void initGroup(){
        GStage.addToLayer(GLayer.ui,MainGroup);

    }


    @Override
    public void run() {
    }

    @Override
    public void render(float var1) {
        super.render(var1);
//        elements.forEach();
    }

    private void renderBg() {
        bg = GUI.createImage(TextureAtlasC.uigame, "bg");
        bg.setWidth(Config.ScreenW);
        bg.setHeight(Config.ScreenH);
        MainGroup.addActor(bg);
    }
    private void checkconnect(){
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://www.facebook.com/");
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

            }

            @Override
            public void failed(Throwable t) {
                Config.checkConnet=false;

            }

            @Override
            public void cancelled() {

            }
        });
    }

}
