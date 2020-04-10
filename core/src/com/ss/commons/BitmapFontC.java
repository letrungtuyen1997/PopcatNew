package com.ss.commons;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ss.core.util.GAssetsManager;

public class BitmapFontC {
  public static BitmapFont roboto;
  public static BitmapFont robotoVi;
  public static BitmapFont RobotoViGrey;
  public static BitmapFont NerwynGrey;
  public static BitmapFont NerwynOrange;
  public static BitmapFont RubikOne;

  public static void initBitmapFont(){
    roboto = GAssetsManager.getBitmapFont("RobotoCyan.fnt");
    robotoVi = GAssetsManager.getBitmapFont("RobotoVi.fnt");
    RobotoViGrey = GAssetsManager.getBitmapFont("RobotoViGrey.fnt");
    NerwynGrey = GAssetsManager.getBitmapFont("NerwynGrey.fnt");
    NerwynOrange = GAssetsManager.getBitmapFont("NerwynOrange.fnt");
    RubikOne = GAssetsManager.getBitmapFont("RubikOne.fnt");
  }
}
