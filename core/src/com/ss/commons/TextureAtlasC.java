package com.ss.commons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ss.core.util.GAssetsManager;

public class TextureAtlasC {
  public static TextureAtlas uigame;
  public static TextureAtlas Fottergame;
  public static TextureAtlas WhellAtlas;

  public static void initAtlas(){
    uigame = GAssetsManager.getTextureAtlas("uigame.atlas");
    Fottergame = GAssetsManager.getTextureAtlas("fotter.atlas");
    WhellAtlas = GAssetsManager.getTextureAtlas("wheel.atlas");
  }
}
