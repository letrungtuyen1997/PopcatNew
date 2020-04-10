package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.interfaces.SelectMode;

import java.util.ArrayList;

public class Board {
    private Pop[][] pops;
    private Group group;
    private ArrayList<Pop> chosenPops = null;
    private int countPop;
    public Board(Group group){
        this.group = group;
        Config.processConfig();
        create();

    }
    public void create() {
        pops = new Pop[Config.row][Config.col];
        int sp = (int)(Math.floor(Math.random()*(Config.defaultMaxPopCount)));
            for(int i = 0; i < Config.row; i++)
                for(int j = 0; j < Config.col; j++) {
                    pops[i][j] = new Pop(j,i,(int)(Math.random()*5),group,this);
                    System.out.println("check: pop:"+i+"-"+j+"---"+pops[i][j].row+"-"+pops[i][j].col);
//                    pops[i][j].addAction(Actions.moveTo(pops[i][j].getX(), cfg.y + cfg.PH * i, 2f - (cfg.row - i) * 0.05f - j*0.05f, Interpolation.smoother));
                }
     }
    void popClick(Pop pop) {
        if (chosenPops == null)
            selectPops(pop);
        else
        if (hasBeenChosen(pop))
            explodePops(Config.selectMode);
        else{
            selectPops(pop);
        }

    }


    private boolean hasBeenChosen(Pop pop) {
        for (Pop p : chosenPops)
            if (pop.row == p.row && pop.col == p.col)
                return true;
        return false;
    }

    private void selectPops(Pop pop){
        if (chosenPops != null) { // release chosen pops
            for (Pop p : chosenPops)
                p.changeState(false);
            chosenPops = null;
        }
        ArrayList<Pop> samePops = new ArrayList<>();

        switch (Config.selectMode){
            case AREA:
                Utils.area(pops, samePops, pop, pop.row, pop.col);
                break;
            case SQUARE:
                Utils.square(pops, samePops, pop, pop.row, pop.col);
                break;
            case VERTICAL:
                Utils.vertical(pops, samePops, pop, pop.row, pop.col);
                break;
        }

        if (samePops.size() >= 2) {
            SoundEffect.Play(SoundEffect.click);
            for (Pop p : samePops) p.changeState(true);
            chosenPops = samePops;
            countPop = samePops.size();
        }
    }


    private void explodePops(SelectMode selectMode){
        System.out.println("check:"+ selectMode);
        //Gdx.input.setInputProcessor(null);
        setTouch(Touchable.disabled);
        float explodeTime = 0;
        int index = 0;

        for (Pop pop : chosenPops){
            pops[pop.row][pop.col] = null;
            final int idxCtx = index++;
            if (selectMode == SelectMode.AREA)
                Tweens.setTimeout(group, explodeTime += 0.1f, () -> {
                    pop.explode();
                });
            else {
                Tweens.setTimeout(group, explodeTime += 0.05f, () -> {
                    pop.explode();
                });
            }
        }
        Tweens.setTimeout(group,explodeTime,()->{
            nullSlice();
        });
        Tweens.setTimeout(group, explodeTime + 0.1f + Config.sliceDuration, ()->{
            blankShift();
        });
        Tweens.setTimeout(group, explodeTime + 0.1f + Config.sliceDuration + Config.shiftDuration, () ->{
            setTouch(Touchable.enabled);
            chosenPops = null;
        });

    }
    private void nullSlice(){
        for (int i = 0; i < pops[0].length; i++){ //bug contain
            ArrayList<Tuple<Pop, D>> slices = new ArrayList<>();
            if (Utils.nullSlice(pops, slices, Config.sliceAnchor, i, Config.sliceDirection) >=1){
                for (int j = slices.size() - 1; j >= 0; j--){
                    Pop p = slices.get(j).obj;
                    D d = slices.get(j).delta;
                    p.pop.addAction(Actions.sequence(
                            Actions.moveTo(p.pop.getX() - d.dc*p.pop.getWidth(), p.pop.getY() - d.dr*p.pop.getHeight(), Config.sliceDuration, Config.sliceStyle),
                            GSimpleAction.simpleAction((dt,a)->{
                                p.popChosse.setX(p.pop.getX());
                                p.popChosse.setY(p.pop.getY());
                                return true;
                            })
                    ));
                    updateArrayPosition(d.dr, d.dc, p);
                }
                Tweens.setTimeout(group, Config.sliceDuration, () -> {
                    SoundEffect.Play(SoundEffect.Fall);
                });
            }
        }
        for (int i=0;i<Config.row;i++){
            for (int j=0; j<Config.col;j++){
                if(pops[i][j]!=null)
                    System.out.println("check: pop:"+i+"-"+j+"---"+pops[i][j].row+"-"+pops[i][j].col);

            }
        }
    }
    private void blankShift() {
        ArrayList<Tuple<Pop, D>> shifts = new ArrayList<>();
        if (Config.sliceDirection == Config.Direction.VERTICAL && Utils.nullSliceH(pops, shifts, Config.shiftAnchor, Config.sliceAnchor) >= 1)
            for (int i = shifts.size() - 1; i >= 0; i--){
                int dc = shifts.get(i).delta.dc;
                int col = shifts.get(i).obj.col;
                for (int j = 0; j < pops.length; j++)
                    if (pops[j][col] != null){
                        Pop u = updateArrayPosition(0, dc, pops[j][col]);
                        u.pop.addAction(Actions.sequence(
                                Actions.moveTo(u.pop.getX() + dc*u.pop.getWidth(), u.pop.getY(), Config.shiftDuration, Interpolation.fastSlow)
                        ));
                    }
            }
        else if (Config.sliceDirection == Config.Direction.HORIZON && Utils.nullSliceV(pops, shifts, Config.shiftAnchor, Config.sliceAnchor) >= 1)
            for (int i = shifts.size() - 1; i >= 0; i--){
                int dr = shifts.get(i).delta.dr;
                int row = shifts.get(i).obj.row;
                for (int j = 0; j < pops[0].length; j++)
                    if (pops[row][j] != null){
                        Pop u = updateArrayPosition(dr, 0, pops[row][j]);
                        u.pop.addAction(Actions.sequence(
                                Actions.moveTo(u.pop.getX(), u.pop.getY() +dr*u.pop.getHeight(), Config.shiftDuration, Config.shiftStyle)
                        ));
                    }
            }
    }
    private Pop updateArrayPosition(int dr, int dc, Pop p){
        pops[p.row][p.col] = null;
        pops[p.row += dr][p.col += dc] = p;
        return p;
    }
    private void setTouch(Touchable set){
        for (int i=0;i<Config.row;i++){
            for (int j=0; j<Config.col;j++){
                if(pops[i][j]!=null)
                    pops[i][j].pop.setTouchable(set);

            }
        }
    }

}
