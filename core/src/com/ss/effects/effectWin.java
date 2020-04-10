package com.ss.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class effectWin extends Actor{
    private static FileHandle angry = Gdx.files.internal("particle/angry");
    private static FileHandle smoke = Gdx.files.internal("particle/khoi");
    private static FileHandle water = Gdx.files.internal("particle/water");
    private static FileHandle coin1 = Gdx.files.internal("particle/coin1");
    private static FileHandle coin2 = Gdx.files.internal("particle/coin2");
    private static FileHandle coin3 = Gdx.files.internal("particle/coin3");
    private static FileHandle coinWin = Gdx.files.internal("particle/coinWin");

    public ParticleEffect effect;
    private Actor parent = this.parent;

    public effectWin(int id, float f, float f2) {
        this.effect = new ParticleEffect();
        setX(f);
        setY(f2);
            if(id==1){
                this.effect.load(angry, Gdx.files.internal("particle"));
                this.effect.scaleEffect(2.0f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else  if(id==2){
                this.effect.load(smoke, Gdx.files.internal("particle"));
                this.effect.scaleEffect(1.5f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }else  if(id==3){
                this.effect.load(water, Gdx.files.internal("particle"));
                this.effect.scaleEffect(0.8f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }
            else if(id == 4){
                this.effect.load(coin1, Gdx.files.internal("particle"));
                this.effect.scaleEffect(0.8f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    //((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }
            else if(id == 5){
                this.effect.load(coin2, Gdx.files.internal("particle"));
                this.effect.scaleEffect(0.8f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    //((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }
            else if(id == 6){
                this.effect.load(coin3, Gdx.files.internal("particle"));
                this.effect.scaleEffect(0.8f);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    //((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,false);
                }
            }
            else if(id == 7){
                this.effect.load(coinWin, Gdx.files.internal("particle"));
                this.effect.scaleEffect(2);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true,true);
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
