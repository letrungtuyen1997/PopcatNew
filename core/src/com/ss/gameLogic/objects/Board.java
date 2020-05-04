package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
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
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GScreenShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.action.exAction.GTemporalAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.interfaces.SelectMode;
import com.ss.scenes.GameScene;
import com.ss.scenes.StartScene;

import java.util.ArrayList;

public class Board {
    private Pop[][] pops;
    private Group group= new Group();
    private Group group1= new Group();
    private Group grlb= new Group();
    private Group grParticle= new Group();
    private Group grClrStage= new Group();
    private ArrayList<Pop> chosenPops = null;
    private int countPop;
    private int quantityPop=0;
    private int count=0;
    private Header header;
    private fotter fotter;
    private Label lbPopEnd, lbBonus;
    private boolean checkWin=false;
    public boolean efSkill=false;
    private int tic=0;
    private GShapeSprite overlap;
    private GameScene gameScene;
    private long bonusScore = Config.BonusScore;
    public Board(int Lv, Header header, fotter fotter, GameScene gameScene){
        this.header = header;
        this.fotter= fotter;
        this.gameScene = gameScene;
        GStage.addToLayer(GLayer.ui,group);
        GStage.addToLayer(GLayer.top,group1);
        GStage.addToLayer(GLayer.top,grlb);
        GStage.addToLayer(GLayer.top,grParticle);
        header.group.addActor(grClrStage);
//        GStage.addToLayer(GLayer.top,grClrStage);
        Config.processConfig();
        quantityPop=setQuantityPop(Lv);
        fotter.loadSkill();
        overlap = new GShapeSprite();
        overlap.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        overlap.setColor(0,0,0,0.6f);
        group1.addActor(overlap);
        overlap.setVisible(false);

        create();
    }
    private int setQuantityPop(int lv){
        switch (lv){
            case 1:
                return 3;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 7;
        }
    }
    public void create() {
        pops = new Pop[Config.row][Config.col];
      //  GMain.prefs.putBoolean("isnewgame",false);
        //GMain.prefs.flush();
        boolean IsOldGame = GMain.prefs.getBoolean("isnewgame");
        if(IsOldGame==false){
            for(int i = 0; i < Config.row; i++) {
                for (int j = 0; j < Config.col; j++) {
                    count++;
                    int finalI = i;
                    int finalJ = j;
                    Tweens.setTimeout(group, Config.flyPop2 * count, () -> {
                        pops[finalI][finalJ] = new Pop(finalJ, finalI, (int) (Math.random() * quantityPop), group,group1, this, fotter, Config.flyPop);

                    });

                }
            }

        }else {
            loadData();
            for(int i = 0; i < Config.row; i++) {
                for (int j = 0; j < Config.col; j++) {
                    count++;
                    int finalI = i;
                    int finalJ = j;
                    int id = GMain.prefs.getInteger("pop" + i + j);
                    if(id == -1) {
                        pops[i][j] = null;
                    } else {
                        Tweens.setTimeout(group, Config.flyPop2 * count, () -> {
                            pops[finalI][finalJ] = new Pop(finalJ, finalI, id, group,group1, this, fotter, Config.flyPop);

                        });
                    }
                }
            }
        }

        Tweens.setTimeout(group,Config.flyPop2*(count/2+2),()->{
            for (int ii=0;ii<(Config.row+3);ii++){
                int finalIi = ii;
                Tweens.setTimeout(group,0.12f*ii,()->{
                    SoundEffect.Play(SoundEffect.Fall);
                    if(finalIi ==Config.row+2){
                        setTouch(Touchable.enabled);
                        saveData();
                        if (isDeadEnd())
                            endGame();
                    }

                });
            }

        });

     }
    void popClick(Pop pop) {


        if (chosenPops == null)
        {
            selectPops(pop);
            if (chosenPops!=null)
                explodePops(Config.selectMode);
        }
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
            setTouchExploxe(Touchable.enabled);
            for (Pop p : chosenPops){
                p.changeState(false);
                if(Config.selectMode==SelectMode.VERTICAL)
                    p.disposeEffect();
            }
            chosenPops = null;

        }
        ArrayList<Pop> samePops = new ArrayList<>();

        switch (Config.selectMode){
            case AREA:
                Utils.area(pops, samePops, pop, pop.row, pop.col);
                break;
            case SQUARE:
                Utils.square(pops, samePops, pop, pop.row, pop.col);
                fotter.setDone();
                fotter.showfrmTutorial(2,false);
                overlap.setVisible(true);
                break;
            case VERTICAL:
                Utils.vertical(pops, samePops, pop, pop.row, pop.col);
                fotter.setDone();
                fotter.showfrmTutorial(1,false);
                overlap.setVisible(true);
                break;
        }

        if (samePops.size() >= 2) {
            SoundEffect.Play(SoundEffect.click);
            for (Pop p : samePops){
                p.changeState(true);
                p.Action();
                if(Config.selectMode==SelectMode.VERTICAL)
                    p.effect();
            }
            chosenPops = samePops;
            countPop = samePops.size();

        }else {
            setTouch(Touchable.enabled);
        }


    }


    private void explodePops(SelectMode selectMode){
        float explodeTime = 0;
        int index = 0;
        setPopScore();
        for (Pop pop : chosenPops){
            pops[pop.row][pop.col] = null;
            final int idxCtx = index++;
                if (selectMode == SelectMode.AREA) {

                    int finalIndex = index;
                    Tweens.setTimeout(group, explodeTime += 0.07f, () -> {
                        CountScore(pop.pop.getX(),pop.pop.getY());
                        if(idxCtx==(chosenPops.size()-1)){
                            TotalScore(chosenPops.size(),pop.pop.getX(),pop.pop.getY());
                        }
                        pop.explode();
                        SoundEffect.explode(idxCtx);
                    });
                }else if(selectMode == SelectMode.SQUARE){
                    if(efSkill==false){
                        fotter.setQuantitySkill(2);
                        efSkill=true;
                        explodeTime=1.4f;
                        effectWin ef = new effectWin(2,0,Config.xSkill,Config.ySkill);
                        group1.addActor(ef);
                        ef.start();
                    }
//                    pop.effect();
                    Tweens.setTimeout(group,1,()->{
                        group.addAction(GScreenShakeAction.screenShake(0.5f,GLayer.ui,GLayer.top));
                        pop.explode();
                        CountScore(pop.pop.getX(),pop.pop.getY());
                        if(idxCtx==(chosenPops.size()-1)){
                            effectWin ef = new effectWin(6,0,Config.xSkill,Config.ySkill);
                            group1.addActor(ef);
                            ef.start();
                            SoundEffect.Play(SoundEffect.Bomb);
                            TotalScore(chosenPops.size(),pop.pop.getX(),pop.pop.getY());
                        }
                    });
                }
                else if(selectMode == SelectMode.VERTICAL){
                    if(efSkill==false){
                        fotter.setQuantitySkill(1);
                        SoundEffect.Play(SoundEffect.Rocket);
                        efSkill=true;
                        Group gr = new Group();
                        GStage.addToLayer(GLayer.top,gr);
                        effectWin ef = new effectWin(3,0,0,0);
                        gr.addActor(ef);
                        gr.setPosition(Config.xSkill,Config.ScreenH);
                        gr.addAction(Actions.sequence(
                                Actions.moveBy(0,-Config.ScreenH,Config.row*0.2f),
                                GSimpleAction.simpleAction((d,a)->{
                                    gr.clear();
                                    gr.remove();
                                    return true;
                                })
                        ));
                    }
                    int finalIndex1 = index;
                    Tweens.setTimeout(group, explodeTime += 0.1f, () -> {
                        CountScore(pop.pop.getX(),pop.pop.getY());
                        if(idxCtx==(chosenPops.size()-1)){
                            TotalScore(chosenPops.size(),pop.pop.getX(),pop.pop.getY());
                        }
                        pop.explode();
                        SoundEffect.explode(idxCtx);
                    });
                }

        }
        Tweens.setTimeout(group, explodeTime, () -> {
            if (chosenPops.size() >= Config.coolLevel && chosenPops.size()<Config.wowLevel){
                SoundEffect.Play(SoundEffect.Good);
            }else if (chosenPops.size() >= Config.wowLevel){
                SoundEffect.Play(SoundEffect.Great);
            }
        });
        Tweens.setTimeout(group,explodeTime,()->{ nullSlice(); });
        Tweens.setTimeout(group, explodeTime + 0.1f + Config.sliceDuration, ()->{ blankShift(); });
        Tweens.setTimeout(group, explodeTime + 0.1f + Config.sliceDuration + Config.shiftDuration, () ->{
            setTouch(Touchable.enabled);
            if (isDeadEnd())
                endGame();
            chosenPops = null;
            Config.selectMode = SelectMode.AREA;
            efSkill=false;
            overlap.setVisible(false);
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
                   // SoundEffect.Play(SoundEffect.Fall);
                });
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
                                Actions.moveTo(u.pop.getX() + dc*u.pop.getWidth(), u.pop.getY(), Config.shiftDuration, Interpolation.fastSlow),
                                GSimpleAction.simpleAction((d,a)->{
                                    u.popChosse.setX(u.pop.getX());
                                    u.popChosse.setY(u.pop.getY());
                                    return true;
                                })
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
                                Actions.moveTo(u.pop.getX(), u.pop.getY() +dr*u.pop.getHeight(), Config.shiftDuration, Config.shiftStyle),
                                GSimpleAction.simpleAction((d,a)->{
                                    u.popChosse.setX(u.pop.getX());
                                    u.popChosse.setY(u.pop.getY());
                                    return true;
                                })
                        ));
                    }
            }
    }
    public void changeOnePop(Pop pop){
        pop.changeState(true);
        pop.effect();
        quantityPop=setQuantityPop(Config.Level);
        Group gr = new Group();
        GStage.addToLayer(GLayer.top,gr);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        gr.addActor(blackOverlay);
        Image frm = GUI.createImage(TextureAtlasC.Fottergame,"frmSkill1");
        frm.setPosition(Config.ScreenW/2,Config.ScreenH/2,Align.center);
        gr.addActor(frm);
        for (int i=0;i<quantityPop;i++){
            Image img = GUI.createImage(TextureAtlasC.uigame,"Static"+i);
            img.setPosition(frm.getX()+frm.getWidth()/2+img.getWidth()/2-img.getWidth()*quantityPop/2+img.getWidth()*i,frm.getY()+frm.getHeight()/2,Align.center);
            gr.addActor(img);
            int finalI = i;
            img.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    SoundEffect.Play(SoundEffect.click);
                    System.out.println("checkkk: "+ finalI);
                    changeColorPop(finalI,pop);
                    gr.clear();
                    gr.remove();
                    Config.selectMode=SelectMode.AREA;
                    fotter.setDone();
                    fotter.setQuantitySkill(0);
                    fotter.showfrmTutorial(0,false);
                }
            });
        }

    }
    private void changeColorPop(int index,Pop pop){
        SoundEffect.Play(SoundEffect.ChangeColor);
        pops[pop.row][pop.col] = new Pop(pop.col, pop.row,index,group,group1,this,fotter,0);
        pop.explode();
//        setTouch(Touchable.enabled);

    }
    private Pop updateArrayPosition(int dr, int dc, Pop p){
        pops[p.row][p.col] = null;
        pops[p.row += dr][p.col += dc] = p;
        return p;
    }
    public void setTouch(Touchable set){
        for (int i=0;i<Config.row;i++){
            for (int j=0; j<Config.col;j++){
                if(pops[i][j]!=null){
                    pops[i][j].pop.setTouchable(set);
                }

            }
        }
    }
    public void setTouchExploxe(Touchable set){
        if(chosenPops!=null){
            for (Pop p : chosenPops)
                p.pop.setTouchable(set);
        }

    }

    private boolean isDeadEnd(){
        for (Pop[] row : pops)
            for (Pop pop : row)
                if (pop != null && Utils.haveAdjacent(pops, pop.row, pop.col, pop))
                    return false;
        return true;
    }
    private void endGame() {
        setTouch(Touchable.disabled);
        final float totalAnimationTime = animationOut();
        Tweens.setTimeout(group, totalAnimationTime, () -> {
                lbPopEnd.remove();
                grlb.addAction(Actions.sequence(
                        Actions.moveTo(grlb.getX(),header.LbScore.getY()+100,1f),
                        GSimpleAction.simpleAction((d,a)->{
                            counterUp(header.LbScore,bonusScore);
                            return true;
                        }),
                        Actions.delay(Config.DuraCountDown),
                        GSimpleAction.simpleAction((d,a)->{
                            CheckLvComplete();
                            if(Config.Score>=(Config.targetScore*Config.Level)){
                                nextLv();
                            }else {
                                SoundEffect.Play(SoundEffect.Lose);
                                GameOver();
                            }
                            return true;
                        })
                ));
        });
    }
    private float animationOut()  {
        float explodeTime = 1.1f/*blink time*/ + 0f;

        final ArrayList<Pop> remainPops = new ArrayList<>();
        Utils.remain(pops, remainPops);
        bonusEndGame(remainPops.size());
        for (Pop p : remainPops) p.blink();

        for (int idx = 0;  idx < remainPops.size() && idx < 10; idx++, explodeTime += 0.2f) {
            final Pop context = remainPops.get(remainPops.size()-idx-1);
            Tweens.setTimeout(group, explodeTime, () -> {
                context.explode();
                SoundEffect.explode(2);
                remainPops.remove(context);
                bonusScore-=200;
                lbBonus.setText(C.lang.popend+bonusScore);

//                bonus.score -= 200;
            });
        }

        float burstTime = (remainPops.size() > 0) ? finalBurst(explodeTime , remainPops) : 0;
        return explodeTime + burstTime + 0.5f;
    }
    private float finalBurst(float after, ArrayList<Pop> remainPops){
        Tweens.setTimeout(group, after, () -> {
//            SoundEffect.play(SoundEffect.BURST);
            for (Pop p : remainPops) p.explode();
        });
        return 0.5f;
    }
    private void setPopScore(){
        Config.PopScore=10;
        if(chosenPops.size()>=3)
            Config.PopScore+=5*(chosenPops.size()-2);
    }
    private void CountScore(float x , float y){
        Group gr = new Group();
        GStage.addToLayer(GLayer.top,gr);
        Label lb = new Label("+"+Config.PopScore,new Label.LabelStyle(BitmapFontC.FontYellow, Color.ORANGE));
        lb.setFontScale(0.5f);
        lb.setAlignment(Align.center);
        lb.setOrigin(Align.center);
        lb.setPosition(0,0,Align.center);
        gr.addActor(lb);
        gr.setPosition(x,y,Align.center);
        gr.setScale(0);
        gr.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(1.2f,1.2f,0.4f),
                        Actions.moveTo(Config.ScreenW/2,header.LbScore.getY(),2f)
                ),
                GSimpleAction.simpleAction((d,a)->{
                    Config.Score+=Config.PopScore;
                    header.updateScore(Config.Score);
                    CheckLvComplete();
                    gr.clear();
                    gr.remove();
                    return true;
                })
        ));
    }
    private void TotalScore(int size, float x, float y){
        Group gr = new Group();
        GStage.addToLayer(GLayer.top,gr);
        Label lb = new Label("+"+Config.PopScore*size,new Label.LabelStyle(BitmapFontC.NerwynOrange, null));
        lb.setAlignment(Align.center);
        lb.setOrigin(Align.center);
        lb.setPosition(0,0,Align.center);
        gr.addActor(lb);
        gr.setPosition(x,y,Align.center);
        gr.setScale(2.5f);
        gr.addAction(Actions.sequence(
                Actions.scaleTo(0,0,1.5f),
                GSimpleAction.simpleAction((d,a)->{
                    gr.clear();
                    gr.remove();
                    return true;
                })
        ));

    }
    private void bonusEndGame(int Popend){
        grlb.setOrigin(Align.center);
        grlb.setPosition(Config.ScreenW/2,Config.ScreenH/2);
        ////// label bunus score//////
        lbBonus = new Label(C.lang.popend+Config.BonusScore,new Label.LabelStyle(BitmapFontC.FontAlert,Color.RED));
        lbBonus.setFontScale(0.6f);
        lbBonus.setOrigin(Align.center);
        lbBonus.setAlignment(Align.center);
        lbBonus.setPosition(0,0,Align.center);
        grlb.addActor(lbBonus);
        ////// label pop end quantity///////
        lbPopEnd = new Label(C.lang.restPend1+Popend+" "+C.lang.restPend2,new Label.LabelStyle(BitmapFontC.FontAlert,Color.RED));
        lbPopEnd.setFontScale(0.8f);
        lbPopEnd.setOrigin(Align.center);
        lbPopEnd.setAlignment(Align.center);
        lbPopEnd.setPosition(0,lbBonus.getHeight()*2,Align.center);
        grlb.addActor(lbPopEnd);
        lbPopEnd.addAction(Actions.sequence(
                Actions.alpha(0,0.2f),
                Actions.alpha(1,0.2f),
                Actions.alpha(0,0.2f),
                Actions.alpha(1,0.2f),
                Actions.alpha(0,0.2f),
                Actions.alpha(1,0.2f)
        ));

    }
    private void CheckLvComplete(){
        saveData();
        if(Config.Score>=(Config.targetScore*Config.Level)&&checkWin==false){
            checkWin=true;
            SoundEffect.Play(SoundEffect.complete);
            AniStageClear();
        }
    }
    private void AniStageClear(){
        grClrStage.setZIndex(header.group.getChildren().get(0).getZIndex()+1);
        String type="stageClearVn";
        if(C.lang.idcontry.equals("en"))
            type="stageClearEn";
        Image logo = GUI.createImage(TextureAtlasC.Fottergame,type);
        logo.setPosition(0,0,Align.center);
        grClrStage.addActor(logo);
        grClrStage.setPosition(Config.ScreenW/2,Config.ScreenH/2);
        grClrStage.setOrigin(Align.center);
        grClrStage.addAction(Actions.parallel(
                Actions.moveTo(Config.ScreenW-logo.getWidth()/4,header.lbLevel.getY(),0.7f),
                Actions.scaleTo(0.4f,0.4f,0.7f)
        ));
    }
    private void saveData(){
        Config.HighScore = GMain.prefs.getLong("HighScore");
        if(Config.Score>Config.HighScore){
            Config.HighScore=Config.Score;
            GMain.platform.ReportScore(Config.HighScore);
        }
        GMain.prefs.putLong("Score",Config.Score);
        GMain.prefs.putLong("HighScore",Config.HighScore);
        GMain.prefs.putInteger("Level",Config.Level);
        GMain.prefs.putBoolean("isnewgame",true);
        /////// array pop save////
        int idStoragePop;
        for(int i = 0; i < Config.row; i++) {
            for(int j = 0; j < Config.col; j++) {
                if(pops[i][j] == null) {
                    idStoragePop = -1;
                }
                else {
                    idStoragePop = pops[i][j].id;
                }
                GMain.prefs.putInteger("pop" + i + j, idStoragePop);
            }
        }
        GMain.prefs.flush();
    }
    private void loadData(){
        Config.Level = GMain.prefs.getInteger("Level");
        Config.Score = GMain.prefs.getLong("Score");
        Config.HighScore = GMain.prefs.getLong("HighScore");
        header.updateLevel();
        header.updateScore(Config.Score);
        header.updateTargetSc(Config.targetScore*Config.Level);
        header.updateHighSc(Config.HighScore);
    }
    private void nextLv(){
        GMain.platform.ShowFullscreen();
        GMain.prefs.putBoolean("isnewgame",false);
        GMain.prefs.flush();
        Config.Level++;
        header.updateTargetSc(Config.targetScore*Config.Level);
        header.updateLevel();
        header.updateHighSc(Config.HighScore);
        bonusScore = Config.BonusScore;
        Config.DuraCountDown=1;
        group.clear();
        group.remove();
        grlb.clear();
        grlb.remove();
        grClrStage.clear();
        grClrStage.remove();
        new Board(Config.Level,header,fotter,gameScene);
    }
    void counterUp(Label object, long target){
        if(target>0){
            SoundEffect.Play(SoundEffect.Result);
            Config.DuraCountDown=1;
        }else {
            Config.DuraCountDown=0;
            System.out.println("0000000000");
        }
        group.addAction(
                GTemporalAction.add(Config.DuraCountDown, (percent, actor) -> {
                    tic++;
                    if(tic==9){
                        tic=0;
                        long up= Math.round(target/6);
                        System.out.println("cong!!!!! : "+up);

                        bonusScore-=up;
                        if(bonusScore<10)
                            bonusScore=0;
                        lbBonus.setText(""+bonusScore);
                        Config.Score+=up;
                        object.setText(""+ Config.Score);
                    }
                }));

    }
    private void GameOver(){
        Config.DuraCountDown=1;
        SoundEffect.Stopmusic(1);
        GMain.platform.ShowFullscreen();
        GMain.prefs.putBoolean("isnewgame",false);
        GMain.prefs.flush();
        bonusScore = Config.BonusScore;
        Group gr = new Group();
        GStage.addToLayer(GLayer.top,gr);
        gr.setPosition(Config.ScreenW/2,Config.ScreenH/2);
        gr.setScale(0);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        gr.addActor(blackOverlay);
        ////////////// frm////////////
        Image frm = GUI.createImage(TextureAtlasC.Fottergame,"frmGameOver");
        frm.setPosition(0,0,Align.center);
        gr.addActor(frm);
        //////////// Score ///////////
        Label lbSc = new Label(C.lang.score,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        lbSc.setFontScale(1f);
        lbSc.setOrigin(Align.center);
        lbSc.setAlignment(Align.center);
        lbSc.setPosition(0,0,Align.center);
        gr.addActor(lbSc);

        Label sc = new Label(""+Config.Score,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        sc.setFontScale(1f);
        sc.setOrigin(Align.center);
        sc.setAlignment(Align.center);
        sc.setPosition(0,100,Align.center);
        gr.addActor(sc);
        /////// btn restart//////
        Image btnRestart = GUI.createImage(TextureAtlasC.Fottergame,"btnRestart");
        btnRestart.setOrigin(Align.center);
        btnRestart.setPosition(-btnRestart.getWidth()/2,frm.getHeight()/2-btnRestart.getHeight()*1.2f,Align.center);
        gr.addActor(btnRestart);
        ////// lbRestart//////
        Label lbRestart = new Label(C.lang.lbRestart,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        lbRestart.setFontScale(0.5f);
        lbRestart.setOrigin(Align.center);
        lbRestart.setAlignment(Align.center);
        lbRestart.setPosition(btnRestart.getX()+btnRestart.getWidth()/2,btnRestart.getY()+btnRestart.getHeight()/2,Align.center);
        gr.addActor(lbRestart);


        ////// btn Home /////////
        Image btnHome = GUI.createImage(TextureAtlasC.Fottergame,"btnHome");
        btnHome.setOrigin(Align.center);
        btnHome.setPosition(btnHome.getWidth()/2,frm.getHeight()/2-btnHome.getHeight()*1.2f,Align.center);
        gr.addActor(btnHome);
        ////// lbHome//////
        Label lbHome = new Label(C.lang.lbHome,new Label.LabelStyle(BitmapFontC.FontAlert,null));
        lbHome.setFontScale(0.5f);
        lbHome.setOrigin(Align.center);
        lbHome.setAlignment(Align.center);
        lbHome.setPosition(btnHome.getX()+btnHome.getWidth()/2,btnHome.getY()+btnHome.getHeight()/2,Align.center);
        gr.addActor(lbHome);
        gr.addAction(Actions.scaleTo(1,1,0.5f,Interpolation.swingOut));
        lbRestart.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                restart(gr);
            }
        });
        lbHome.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                SoundEffect.Play(SoundEffect.click);
                gameScene.setScreen(new StartScene());
            }
        });
    }
    private void restart(Group gr){
        SoundEffect.Playmusic(1);
        GMain.platform.ShowFullscreen();
        Config.Level=1;
        Config.Score=0;
        bonusScore = Config.BonusScore;
        gr.clear();
        gr.remove();
        group.clear();
        group.remove();
        grlb.clear();
        grlb.remove();
        grClrStage.clear();
        grClrStage.remove();
        header.updateLevel();
        header.updateScore(Config.Score);
        header.updateTargetSc(Config.targetScore*Config.Level);
        header.updateHighSc(Config.HighScore);
        new Board(Config.Level,header,fotter,gameScene);
    }
}
