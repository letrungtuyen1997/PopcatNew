package com.ss.gameLogic.config;

import com.badlogic.gdx.math.Interpolation;
import com.ss.interfaces.SelectMode;

enum Anchor {
    LEFT, RIGHT, TOP, BOTTOM
}



enum ShiftAnchor {
    START, END
}
public class Config {
    public static int row=12;
    public static int col=8;
    public static int defaultPopCount = 4; //firebase
    public static int defaultMaxPopCount = 7;
    public static int defaultMaxSkill = 3; //firebase
    public static float paddingY=0;
    public enum Direction {
        HORIZON, VERTICAL
    }
    public enum Anchor {
        LEFT, RIGHT, TOP, BOTTOM
    }



    public enum ShiftAnchor {
        START, END
    }
    public static int sliceAnchor = 0;
    public static int shiftAnchor = 0;
    public static float sliceDuration = 0.4f;
    public static float shiftDuration = 0.15f;

    public static int coolLevel = 8;
    public static int wowLevel = 10;
    public static Direction sliceDirection;
    public static Interpolation sliceStyle = Interpolation.swingIn;
    public static Interpolation shiftStyle = Interpolation.fastSlow;

    public static SelectMode selectMode = SelectMode.AREA;
    public static Anchor _sliceAnchor = Anchor.BOTTOM;
    public static ShiftAnchor _shiftAnchor = ShiftAnchor.START;
//
//
    public static void processConfig() {
        if (_sliceAnchor == Anchor.BOTTOM || _sliceAnchor == Anchor.TOP) {
            sliceDirection = Direction.VERTICAL;
            if (_sliceAnchor == Anchor.BOTTOM)
                sliceAnchor = 0;
            else
                sliceAnchor = row - 1;
            if (_shiftAnchor == ShiftAnchor.START)
                shiftAnchor = 0;
            else
                shiftAnchor = row - 1;
        }
        else if (_sliceAnchor == Anchor.LEFT || _sliceAnchor == Anchor.RIGHT) {
            sliceDirection = Direction.HORIZON;
            if (_sliceAnchor == Anchor.LEFT)
                sliceAnchor = 0;
            else
                sliceAnchor = col - 1;
            if (_shiftAnchor == ShiftAnchor.START)
                shiftAnchor = 0;
            else
                shiftAnchor = col - 1;
        }
    }
}
