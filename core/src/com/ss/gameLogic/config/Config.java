package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.core.util.GStage;
import com.ss.interfaces.SelectMode;

enum Anchor {
    LEFT, RIGHT, TOP, BOTTOM
}



enum ShiftAnchor {
    START, END
}
public class Config {


    public static int row=10;
    public static int col=8;
    public static int defaultPopCount = 4; //firebase
    public static int defaultMaxPopCount = 7;
    public static int defaultMaxSkill = 3; //firebase
    public static int timeChosse=0;
    public static float paddingY=80;
    public static float flyPop=1f;
    public static float flyPop2=0.02f;
    public static float ScreenW = GStage.getWorldWidth();
    public static float ScreenH = GStage.getWorldHeight();
    public static int Level=1;
    public static long Score=0;
    public static long PopScore=10;
    public static long BonusScore =2000;
    public static long targetScore =3000;
    public static long HighScore =0;
    public static float xSkill = 0;
    public static float ySkill = 0;
    public static boolean checkConnet =true;
    public static boolean checkWheel =false;
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
    public static float sliceDuration = 0.25f;
    public static float shiftDuration = 0.1f;
    public static float DuraCountDown = 1f;

    public static int coolLevel = 8;
    public static int wowLevel = 10;
    public static Direction sliceDirection;
    public static Interpolation sliceStyle = Interpolation.pow2In;
    public static Interpolation sliceStyle2 = Interpolation.pow2;
    public static Interpolation shiftStyle = Interpolation.pow3In;

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
    private static final FileHandle fh2 = Gdx.files.internal("data/other_games.json");
    public static String otherGameData = GMain.platform.GetConfigStringValue("crosspanel", fh2.readString());
    public static void loadjson(){
        FileHandle js = Gdx.files.internal("data/ConfigData.json");
        String jsonStr = js.readString();
//        JsonReader json = new JsonReader();
//        JsonValue jv = json.parse(jsonStr);
        String jv2 =GMain.platform.GetConfigStringValue("config",jsonStr);
        System.out.println("log: "+jv2);
        JsonReader json = new JsonReader();
        JsonValue jv = null;
        try {
            jv = json.parse(jv2);
            System.out.println("log:"+jv.get("row").asInt());
        }catch (Exception e){
            jv = json.parse(jsonStr);
        }
        row = jv.get("row").asInt();
        col = jv.get("col").asInt();
        paddingY = jv.get("paddingBaner").asFloat();
        flyPop = jv.get("drFlypop").asFloat();
        flyPop2 = jv.get("drFlypop2").asFloat();
        PopScore = jv.get("popScore").asLong();
        BonusScore = jv.get("bonusScore").asLong();
        targetScore = jv.get("targetScore").asLong();
        sliceDuration = jv.get("sliceDuration").asFloat();
        shiftDuration = jv.get("shiftDuration").asFloat();
        DuraCountDown = jv.get("DuraCountDown").asFloat();
        coolLevel = jv.get("coolLevel").asInt();
        wowLevel = jv.get("wowLevel").asInt();
    }
}
