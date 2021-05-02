package com.ray3k.javapoet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.ray3k.javapoet.Resources.*;

public class Core extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    Array<Entity> entities;
    static Vector2 temp = new Vector2();
    
    @Override
    public void create () {
        spriteBatch = new SpriteBatch();
        entities = new Array<>();
        loadAssets();
        
        for (int i = 0; i < 15; i++) {
            Entity entity = new Entity();
            temp.set(MathUtils.random(50), 0)
                    .rotateDeg(MathUtils.random(360));
            entity.x = 10 + MathUtils.random(Gdx.graphics.getWidth() - 20);
            entity.y = 10 + MathUtils.random(Gdx.graphics.getHeight() - 20);
            entity.deltaX = temp.x;
            entity.deltaY = temp.y;
            entity.region = regionBalloon;
            entities.add(entity);
        }
    }
    
    @Override
    public void render () {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        
        spriteBatch.begin();
        for (Entity entity : entities) {
            entity.act(delta);
            entity.draw();
        }
        spriteBatch.end();
    }
    
    @Override
    public void dispose () {
    }
    
    public class Entity {
        TextureRegion region;
        float x;
        float y;
        float deltaX;
        float deltaY;
        
        public void act(float delta) {
            if (x < 0) {
                deltaX *= -1;
                region = regionBalloon;
                sfxFart.play();
            } else if (x > Gdx.graphics.getWidth()) {
                deltaX *= -1;
                region = regionUpset;
                sfxGrunt.play();
            }
            
            if (y < 0) {
                deltaY *= -1;
                region = regionShocked;
                sfxExcuseMe.play();
            } else if (y > Gdx.graphics.getHeight()) {
                deltaY *= -1;
                region = regionExclaim;
                sfxHey.play();
            }
            
            x += deltaX * delta;
            y += deltaY * delta;
        }
        
        public void draw() {
            spriteBatch.draw(region, x, y);
        }
    }
}