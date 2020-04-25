package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;
import java.util.MissingResourceException;

public class C {

    public static class remote {
        public static int adsTime = 50;
        static void initRemoteConfig() {

        }
    }

    public static class lang {
        private static I18NBundle locale;
        public static String title = "";
        public static String adsTimeLbl = "";
        public static String popend = "";
        public static String restPend1 = "";
        public static String restPend2 = "";
        public static String score = "";
        public static String alert = "";
        public static String altGmother = "";
        public static String lbwatch = "";
        public static String lbwatch1 = "";
        public static String idcontry="";
        public static String lbtoturial="";


        static void initLocalize() {
            FileHandle specFilehandle = Gdx.files.internal("i18n/lang_" + "id");
            FileHandle baseFileHandle = Gdx.files.internal("i18n/lang");

            try {
                locale = I18NBundle.createBundle(specFilehandle, new Locale(""));
                idcontry = locale.get("icontry");
            }
            catch (MissingResourceException e) {
                locale = I18NBundle.createBundle(baseFileHandle, new Locale(""));
            }

            title = locale.get("title");
            adsTimeLbl = locale.format("adsTime", remote.adsTime);
            popend = locale.get("popend");
            restPend1 = locale.get("quantitypopend1");
            restPend2 = locale.get("quantitypopend2");
            score = locale.get("score");
            alert = locale.get("alert");
            altGmother = locale.get("gmother");
            lbwatch = locale.get("lbwatch");
            lbwatch1 = locale.get("lbwatch1");
            lbtoturial = locale.get("lbTutorial");
        }
    }

    public static void init() {
        remote.initRemoteConfig();
        lang.initLocalize();
    }
}
