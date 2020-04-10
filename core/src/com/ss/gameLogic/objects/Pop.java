package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.interfaces.Comparable;

public class Pop implements Comparable {
    public Image pop,popChosse;
    public String type;
    public int id;
    public int row,col;
    private Board board;
    public Pop(int col, int row,int id, Group group,Board board){
        this.id = id;
        this.row = row;
        this.col = col;
        this.board = board;
        pop = GUI.createImage(TextureAtlasC.uigame,"Static"+id);
        pop.setPosition(GStage.getWorldWidth()/2+pop.getWidth()/2-(pop.getWidth()* Config.col/2)+pop.getWidth()*col,GStage.getWorldHeight()-Config.paddingY- pop.getHeight()/2-pop.getHeight()*row, Align.center);
        group.addActor(pop);
        popChosse = GUI.createImage(TextureAtlasC.uigame,"Chosse"+id);
        popChosse.setPosition(pop.getX(),pop.getY());
        group.addActor(popChosse);
        popChosse.setVisible(false);
        AddTouch();
    }
    public void AddTouch(){
        pop.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                System.out.println("here!!!");
                board.popClick(Pop.this);
            }
        });
        popChosse.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                System.out.println("here!!!");
                board.popClick(Pop.this);
            }
        });
    }
    public void changeState(boolean set){
            popChosse.setVisible(set);
    }
    public void setTouch(Touchable set){
        pop.setTouchable(set);
        popChosse.setTouchable(set);
    }
    public void updateRowCol(int R, int C){
        this.row = R;
        this.col = C;
    }
    public  void  explode(){
      pop.clear();
      pop.remove();
      popChosse.clear();
      popChosse.remove();

    }

    @Override
    public boolean compare(Comparable p) {
        Pop t = (Pop)p;
        return (this.id == t.id);
    }

    @Override
    public int[] getRC() {
        return new int[] {row, col};
    }
}
