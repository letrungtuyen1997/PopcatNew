package com.ss.scenes;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.Pop;
import com.ss.interfaces.DisplayElement;

import java.util.ArrayList;

public class GameScene extends GScreen {
    private Group MainGroup = new Group();
    private ArrayList<DisplayElement> elements = new ArrayList<>();

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        TextureAtlasC.initAtlas();
        initGroup();
        renderBg();
    }
    private void initGroup(){
        GStage.addToLayer(GLayer.top,MainGroup);

    }


    @Override
    public void run() {
    }

    @Override
    public void render(float var1) {
        super.render(var1);
//        elements.forEach();
    }

    private void renderBg(){
        Image bg = GUI.createImage(TextureAtlasC.uigame,"bg");
        MainGroup.addActor(bg);
        new Board(MainGroup);
    }

}
