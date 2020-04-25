package com.ss.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GAssetsManager;

public class effectWin extends Actor{
    private static FileHandle Destroy = Gdx.files.internal("effects/Destroy2");
    private static FileHandle Bomb = Gdx.files.internal("effects/bomb");
    private static FileHandle rocket = Gdx.files.internal("effects/rocket");
    private static FileHandle light = Gdx.files.internal("effects/light");
    private static FileHandle unlock = Gdx.files.internal("effects/unlock");
    private static FileHandle lightPop = Gdx.files.internal("effects/lightPop");
    private static FileHandle lightBomb = Gdx.files.internal("effects/lightBomb");

    public ParticleEffect effect;
    private Actor parent = this.parent;

    public effectWin(int id,int id2, float f, float f2) {
        this.effect = new ParticleEffect();
        setX(f);
        setY(f2);
            if(id==1){
                TextureAtlas atlas = new TextureAtlas();
                AssetManager assetManager = new AssetManager();
                assetManager.load("effects/Chosse"+id2+".png",Texture.class);
                assetManager.finishLoading();
                TextureRegion texture;
                texture= new TextureRegion(assetManager.get("effects/Chosse"+id2+".png",Texture.class));
                atlas.addRegion("party", texture);
//                this.effect.load(Destroy, Gdx.files.internal("particle"));
                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(0.7f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else if(id==2){
                this.effect.load(Bomb, Gdx.files.internal("effects"));
//                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(1f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
//                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else if(id==3){
                this.effect.load(rocket, Gdx.files.internal("effects"));
//                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(1f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
//                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else if(id==4){
                this.effect.load(light, Gdx.files.internal("effects"));
//                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(1f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
//                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else if(id==5){
                this.effect.load(unlock, Gdx.files.internal("effects"));
//                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(4f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
//                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else if(id==6){
                this.effect.load(lightBomb, Gdx.files.internal("effects"));
//                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(1f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
//                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }
            else if(id==7){
                this.effect.load(lightPop, Gdx.files.internal("effects"));
//                this.effect.load(Destroy, atlas);
                this.effect.scaleEffect(0.8f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
//                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }


        this.effect.setPosition(f, f2);


    }

    public void act(float f) {
        super.act(f);
        this.effect.setPosition(getX(), getY());
        this.effect.update(f);
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        if (!this.effect.isComplete()) {
            this.effect.draw(batch);
            return;
        }
        this.effect.dispose();


    }

    public void setScale(float ratio){
        this.effect.scaleEffect(ratio);
    }

    public void setScale(float ratioX, float ratioY){
        this.effect.scaleEffect(ratioX, ratioY);
    }

    public void start() {
        this.effect.start();
    }
}
