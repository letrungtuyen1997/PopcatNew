package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.config.Config;
import com.ss.interfaces.Comparable;
import com.ss.interfaces.SelectMode;

public class Pop implements Comparable {
    public Image pop,popChosse;
    public String type;
    public int id;
    public int row,col;
    private Board board;
    private Group group;
    private Group grEffect =new Group();
    public Pop(int col, int row,int id, Group group,Group group2,Board board,fotter fotter,float duration){
        GStage.addToLayer(GLayer.ui,grEffect);
        this.id = id;
        this.row = row;
        this.col = col;
        this.board = board;
        this.group = group;
        pop = GUI.createImage(TextureAtlasC.uigame,"Static"+id);
        pop.setOrigin(Align.center);
//        pop.setPosition(Config.ScreenW/2+pop.getWidth()/2-(pop.getWidth()* Config.col/2)+pop.getWidth()*col,Config.ScreenH- pop.getHeight()/2-pop.getHeight()*row, Align.center);
        pop.setPosition(Config.ScreenW/2+pop.getWidth()/2-(pop.getWidth()* Config.col/2)+pop.getWidth()*col,-pop.getHeight(), Align.center);
        group.addActor(pop);
        pop.addAction(Actions.sequence(
                Actions.moveTo(Config.ScreenW/2-(pop.getWidth()* Config.col/2)+pop.getWidth()*col,Config.ScreenH-fotter.group.getHeight()*1.1f- pop.getHeight()-pop.getHeight()*row,duration,Config.sliceStyle2),
                GSimpleAction.simpleAction((d,a)->{
//                    if(col==3)
//                        SoundEffect.Play(SoundEffect.Fall);
                    popChosse = GUI.createImage(TextureAtlasC.uigame,"Chosse"+id);
                    popChosse.setOrigin(Align.center);
                    popChosse.setPosition(pop.getX(),pop.getY());
                    group2.addActor(popChosse);
                    popChosse.setVisible(false);
                    pop.setTouchable(Touchable.disabled);
//                    group2.addActor(grEffect);
                    AddTouch();
                    if(row==Config.row-1&&col==Config.col-1||duration==0){
                        System.out.println("mo het");
                        board.setTouch(Touchable.enabled);
                    }
                    return true;
                })
        ));



    }
    public void AddTouch(){
        pop.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                board.setTouch(Touchable.disabled);
                SoundEffect.Play(SoundEffect.click);
                Config.xSkill=pop.getX()+pop.getWidth()/2;
                Config.ySkill=pop.getY()+pop.getHeight()/2;
                board.popClick(Pop.this);
//                board.setTouchExploxe(Touchable.disabled);
                if(Config.selectMode== SelectMode.CHANGECOLOR)
                    board.changeOnePop(Pop.this);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    public void Action(){
        pop.addAction(Actions.parallel(
                Actions.sequence(
                        Actions.scaleTo(1.1f,1.1f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ),
                Actions.sequence(
                        Actions.moveBy(0,-5,0.1f),
                        Actions.moveBy(0,5,0.1f)
                )
        ));
        popChosse.addAction(Actions.parallel(
                Actions.sequence(
                        Actions.scaleTo(1.1f,1.1f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ),
                Actions.sequence(
                        Actions.moveBy(0,-5,0.1f),
                        Actions.moveBy(0,5,0.1f)
                )
        ));
    }
    public void effect(){
        effectWin ef = new effectWin(7,id,pop.getX()+pop.getWidth()/2,pop.getY()+pop.getHeight()/2);
        grEffect.addActor(ef);
//        .start();
    }
    public void changeState(boolean set){
            popChosse.setVisible(set);
    }
    public void setTouch(Touchable set){
        pop.setTouchable(set);
        if(popChosse.isVisible()==true)
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
        if(board.efSkill==false){
            effectWin ef = new effectWin(1,id,pop.getX(),pop.getY());
            group.addActor(ef);
            ef.start();
        }
        disposeEffect();
    }
    public void disposeEffect(){
        grEffect.clear();
        grEffect.remove();
    }
    public void blink(){
        Tweens.Blink(pop, 1f,0.2f, 0.3f, 8, () -> { });
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
