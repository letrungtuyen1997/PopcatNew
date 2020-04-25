package com.ss.effects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ss.core.util.GAssetsManager;

/* renamed from: com.ss.effect.SoundEffect */
public class SoundEffect {
  public static int MAX_COMMON = 38;
  public static Music bgSound = null;
  public static Music bgSound2 = null;
  public static Music bgSound3 = null;
  public static Sound[] commons = null;
  public static boolean music = false;
  public static boolean mute = false;

  public static int click = 0;
  public static int Fall = 1;
  public static int complete = 2;
  public static int Good = 3;
  public static int Great = 4;
  public static int Lose = 5;
  public static int Bomb = 6;
  public static int Rocket = 7;
  public static int ChangeColor = 8;
  public static int Result = 9;
  public static int Tick = 10;
  public static int Unlock = 11;
  private static Sound[] explode;


  public static void initSound() {
    explode = new Sound[14];
    for (int i = 0; i < 14; i++)
      explode[i] = GAssetsManager.getSound("eli" + (i + 1) + ".mp3");
    commons = new Sound[MAX_COMMON];
    commons[click] = GAssetsManager.getSound("click.mp3");
    commons[Fall] = GAssetsManager.getSound("EffectFall.mp3");
    commons[complete] = GAssetsManager.getSound("stageclear.mp3");
    commons[Good] = GAssetsManager.getSound("Good.mp3");
    commons[Great] = GAssetsManager.getSound("Great.mp3");
    commons[Lose] = GAssetsManager.getSound("gameOver2.mp3");
    commons[Bomb] = GAssetsManager.getSound("bomb.mp3");
    commons[Rocket] = GAssetsManager.getSound("rocket.mp3");
    commons[ChangeColor] = GAssetsManager.getSound("changColor.mp3");
    commons[Result] = GAssetsManager.getSound("result2.mp3");
    commons[Tick] = GAssetsManager.getSound("wheel_sound.mp3");
    commons[Unlock] = GAssetsManager.getSound("unlock.mp3");

//

////        commons[coins] = GAssetsManager.getSound("Coin.mp3");
////        commons[coins].setVolume(2,5);
    bgSound = GAssetsManager.getMusic("bghome.mp3");

  }

  public static long Play(int i) {
    long id = -1;
    if (!mute) {
      id = commons[i].play();
      commons[i].setVolume(id,0.5f);
    }
    return id;
  }
  public static void explode(int level) {
    if(!mute)
      explode[(level > 13) ? 13 : level].play();
  }

//  public static void Play(int i) {
//    if (!mute) {
//      commons[i].play();
//      commons[i].setVolume(i,0.5f);
//
//    }
//  }

  public static void Playmusic(int mode) {
    music = false;
    switch (mode) {
      case 1: {
        bgSound.play();
        bgSound.setLooping(true);
        bgSound.setVolume(0.8f);
        break;
      }
      case 2: {
        bgSound2.play();
        bgSound2.setLooping(true);
        bgSound2.setVolume(0.2f);
        break;
      }
      case 3: {
        bgSound3.play();
        bgSound3.setLooping(true);
        bgSound3.setVolume(0.2f);
        break;
      }
      default:{
        bgSound.play();
        bgSound.setLooping(true);
        bgSound.setVolume(0.2f);
        break;
      }
    }
  }

  public static void Stopmusic(int mode) {
    music = true;
    switch (mode){
      case 1: {
        bgSound.pause();
        break;
      }
      case 2: {
        bgSound2.stop();
        break;
      }
      case 3: {
        bgSound3.pause();
        break;
      }
      default:{
        bgSound.stop();
        break;
      }
    }
  }
}
