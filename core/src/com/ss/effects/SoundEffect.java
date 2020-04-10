package com.ss.effects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ss.core.util.GAssetsManager;

/* renamed from: com.ss.effect.SoundEffect */
public class SoundEffect {
  public static int MAX_COMMON = 38;
  public static Music bgLobby = null;
  public static Music bgPlay = null;
  public static Sound[] commons = null;
  public static boolean music = true;
  public static boolean mute = false;

  public static int click = 0;
  public static int Fall = 1;


  public static void initSound() {
    commons = new Sound[MAX_COMMON];
    commons[click] = GAssetsManager.getSound("click.mp3");
    commons[Fall] = GAssetsManager.getSound("EffectFall.mp3");

//

////        commons[coins] = GAssetsManager.getSound("Coin.mp3");
////        commons[coins].setVolume(2,5);
//    bgSound3 = GAssetsManager.getMusic("soundBg3.mp3");

  }

  public static long Play(int i) {
    long id = -1;
    if (!mute) {
      id = commons[i].play();
      commons[i].setVolume(id,0.5f);
    }
    return id;
  }

  public static void Playmusic(int mode) {
    //if(music){
      switch (mode) {
        case 1: {
          bgLobby.play();
          bgLobby.setLooping(true);
          if(music)
            bgLobby.setVolume(0.5f);
          break;
        }
        case 2: {
          bgPlay.play();
          bgPlay.setLooping(true);
          if(music)
            bgPlay.setVolume(0.5f);
          break;
        }
        default:{
          bgLobby.play();
          bgLobby.setLooping(true);
          if(music)
            bgLobby.setVolume(0.5f);
          break;
        }
      }
    //}
  }

  public static void Stopmusic(int mode) {
    switch (mode){
      case 1: {
        bgLobby.stop();
        break;
      }
      case 2: {
        bgPlay.stop();
        break;
      }
      default:{
        bgLobby.stop();
        bgPlay.stop();
        break;
      }
    }
  }

  public static void pauseM(){
    bgLobby.setVolume(0);
    bgPlay.setVolume(0);
  }

  public static void unPause(){
    bgLobby.setVolume(0.5f);
    bgPlay.setVolume(0.5f);
  }
}
