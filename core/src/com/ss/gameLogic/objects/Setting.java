package com.ss.gameLogic.objects;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.platform.ToggleHandler;
import com.ss.GMain;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.ToggleBtn;
import com.ss.commons.Tweens;
import com.ss.commons._ToggleButton;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;
import com.ss.scenes.StartScene;

public class Setting implements ToggleHandler {
    private Image btnSetting;
    private Array<Image> arrOn = new Array<>();
    private Array<Image> arrOff = new Array<>();
    private Group group = new Group();
    private GameScene gameScene;

    public Setting(Group gr, Image btn, GameScene gameScene){
        this.gameScene = gameScene;
        gr.addActor(group);
        btnSetting=btn;
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
        blackOverlay.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                    group.clear();
                    group.remove();
            }
        });
        ////////////////// music///////////////////
        ////////// btn TurnOn /////////
        Image btnTurnOnMusic = GUI.createImage(TextureAtlasC.Fottergame,"turnOnMs");
        btnTurnOnMusic.setPosition(-btnTurnOnMusic.getWidth()/2,btnSetting.getY()-btnTurnOnMusic.getHeight(),Align.center);
        group.addActor(btnTurnOnMusic);
        arrOn.add(btnTurnOnMusic);
        ///////// btn TurnOff ///////
        Image btnTurnOffMusic = GUI.createImage(TextureAtlasC.Fottergame,"turnOffMs");
        btnTurnOffMusic.setPosition(-btnTurnOnMusic.getWidth()/2,btnSetting.getY()-btnTurnOnMusic.getHeight(),Align.center);
        group.addActor(btnTurnOffMusic);
        arrOff.add(btnTurnOffMusic);
        if(SoundEffect.music==false){
            btnTurnOffMusic.setVisible(false);
        }else {
            btnTurnOnMusic.setVisible(false);
        }
        new _ToggleButton(btnTurnOnMusic,btnTurnOffMusic,"music",this);
        /////////// Sound ////////
        ////////// btn TurnOn /////////
        Image btnTurnOnSound = GUI.createImage(TextureAtlasC.Fottergame,"turnOnSu");
        btnTurnOnSound.setPosition(-btnTurnOnMusic.getWidth()/2,btnSetting.getY()-btnTurnOnSound.getHeight()*2-10,Align.center);
        group.addActor(btnTurnOnSound);
        arrOn.add(btnTurnOnSound);
        ///////// btn TurnOff ///////
        Image btnTurnOffSound = GUI.createImage(TextureAtlasC.Fottergame,"turnOffSu");
        btnTurnOffSound.setPosition(-btnTurnOnMusic.getWidth()/2,btnSetting.getY()-btnTurnOnSound.getHeight()*2-10,Align.center);
        group.addActor(btnTurnOffSound);
        arrOff.add(btnTurnOffSound);
        if(SoundEffect.mute==false){
            btnTurnOffSound.setVisible(false);
        }else {
            btnTurnOnSound.setVisible(false);
        }
        new _ToggleButton(btnTurnOnSound,btnTurnOffSound,"sound",this);

        ////////// home ///////////
        Image btnHome = GUI.createImage(TextureAtlasC.Fottergame,"iconHome");
        btnHome.setPosition(-btnHome.getWidth()/2,btnSetting.getY()-btnHome.getHeight()*3-20,Align.center);
        group.addActor(btnHome);
        arrOn.add(btnHome);
        arrOff.add(btnHome);
        ActionIn();
        //// evnet btn home /////
        eventBack(btnHome);

    }
    private void ActionIn(){
        for (int i=0;i<arrOn.size;i++){
            int finalI = i;
            Tweens.setTimeout(group,0.1f*i,()->{
                arrOn.get(finalI).setOrigin(Align.center);
                arrOff.get(finalI).setOrigin(Align.center);
                arrOn.get(finalI).addAction(Actions.moveTo(btnSetting.getX(),arrOn.get(finalI).getY(),0.5f, Interpolation.swingOut));
                arrOff.get(finalI).addAction(Actions.moveTo(btnSetting.getX(),arrOff.get(finalI).getY(),0.5f,Interpolation.swingOut));

            });
        }
    }
    private void eventBack(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);

                gameScene.setScreen(new StartScene());
            }
        });
    }

    @Override
    public void activeHandler(String str) {
        if(str=="sound"){
            SoundEffect.mute = true;
        }
        if(str=="music"){
            SoundEffect.music = true;
            SoundEffect.Stopmusic(1);
        }

    }

    @Override
    public void deactiveHandler(String str) {
        if(str=="sound"){
            SoundEffect.mute = false;
        }
        if(str=="music"){
            SoundEffect.music = false;
            SoundEffect.Playmusic(1);
        }
    }
}
