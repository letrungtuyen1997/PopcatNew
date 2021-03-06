package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;

public class Header {
    public Group group = new Group();
    public Label LbScore, lbTarget, lbHighSc, lbLevel;

    public Header(){
        GStage.addToLayer(GLayer.top,group);
//        Image header = GUI.createImage(TextureAtlasC.Fottergame,"header");
//        header.setWidth(Config.ScreenW);
//        header.setOrigin(Align.center);
//        header.setPosition(header.getWidth()/2,header.getHeight()/2,Align.center);
//        group.addActor(header);
        Image frmHighSc = GUI.createImage(TextureAtlasC.Fottergame,"frmHighSc");
        frmHighSc.setPosition(frmHighSc.getWidth()/2,frmHighSc.getHeight()/2,Align.center);
        group.addActor(frmHighSc);
        ////////// Label highSc/////
        Label lbHighS = new Label(C.lang.lbHighSc,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        lbHighS.setFontScale(0.4f);
        lbHighS.setOrigin(Align.center);
//        lbHighS.setAlignment(Align.center);
        lbHighS.setPosition(35,lbHighS.getPrefHeight()*1.5f,Align.left);
        group.addActor(lbHighS);
        ////////// Label Target/////
        Label lbtar = new Label(C.lang.lbTarget,new Label.LabelStyle(BitmapFontC.FontAlert, Color.GREEN));
        lbtar.setFontScale(0.4f);
        lbtar.setOrigin(Align.center);
//        lbtar.setAlignment(Align.center);
        lbtar.setPosition(35,lbHighS.getY()+lbHighS.getPrefHeight()*2.8f,Align.left);
        group.addActor(lbtar);
        ////////// Label Level/////
        Label lblv = new Label(C.lang.lbLv,new Label.LabelStyle(BitmapFontC.FontAlert, Color.ORANGE));
        lblv.setFontScale(0.4f);
        lblv.setOrigin(Align.center);
//        lblv.setAlignment(Align.center);
        lblv.setPosition(35,lbHighS.getY()+lbHighS.getPrefHeight()*4.5f,Align.left);
        group.addActor(lblv);

        ///////// frm Score////////
        Image frmScore = GUI.createImage(TextureAtlasC.Fottergame,"frmScore");
        frmScore.setPosition(Config.ScreenW/2,200,Align.center);
        group.addActor(frmScore);
        /////// label score/////
        LbScore = new Label(""+ Config.Score,new Label.LabelStyle(BitmapFontC.NerwynOrange,null));
        LbScore.setFontScale(1.5f);
        LbScore.setOrigin(Align.center);
        LbScore.setAlignment(Align.center);
        LbScore.setPosition(frmScore.getX()+frmScore.getWidth()/2,frmScore.getY()+frmScore.getHeight()/2, Align.center);
        group.addActor(LbScore);
        //////// label target score //////
        lbTarget = new Label(""+Config.targetScore*Config.Level,new Label.LabelStyle(BitmapFontC.robotoVi,null));
        lbTarget.setFontScale(0.7f);
        lbTarget.setAlignment(Align.center);
        lbTarget.setOrigin(Align.center);
        lbTarget.setPosition(frmHighSc.getX()+frmHighSc.getWidth()*0.6f,frmHighSc.getY()+frmHighSc.getHeight()*0.48f,Align.center);
        group.addActor(lbTarget);
        ////// label High Score ///////
        lbHighSc = new Label(""+Config.HighScore,new Label.LabelStyle(BitmapFontC.robotoVi,null));
        lbHighSc.setFontScale(0.7f);
        lbHighSc.setAlignment(Align.center);
        lbHighSc.setOrigin(Align.center);
        lbHighSc.setPosition(frmHighSc.getX()+frmHighSc.getWidth()*0.6f,frmHighSc.getY()+frmHighSc.getHeight()*0.25f,Align.center);
        group.addActor(lbHighSc);
        ////// label level/////
        lbLevel= new Label(""+Config.Level,new Label.LabelStyle(BitmapFontC.NerwynOrange,null));
        lbLevel.setFontScale(1.3f);
        lbLevel.setAlignment(Align.center);
        lbLevel.setOrigin(Align.center);
        lbLevel.setPosition(frmHighSc.getX()+frmHighSc.getWidth()*0.3f,frmHighSc.getY()+frmHighSc.getHeight()*0.7f,Align.center);
        group.addActor(lbLevel);
    }
    public void updateScore(long sc){
        LbScore.setText(""+sc);
    }
    public void updateTargetSc(long sc){
        lbTarget.setText(""+sc);
    }
    public void updateLevel(){
        lbLevel.setText(""+Config.Level);
    }
    public void updateHighSc(long sc){
        lbHighSc.setText(""+sc);
    }
}
