package com.ray3k.javapoet;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Resources {
    public static AssetManager assetManager;

    public static Sound sfxExcuseMe;

    public static Sound sfxFart;

    public static Sound sfxGrunt;

    public static Sound sfxHey;

    public static TextureAtlas.AtlasRegion regionBalloon;

    public static TextureAtlas.AtlasRegion regionExclaim;

    public static TextureAtlas.AtlasRegion regionShocked;

    public static TextureAtlas.AtlasRegion regionUpset;

    public static void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("sfx/excuse-me.mp3", Sound.class);
        assetManager.load("sfx/fart.mp3", Sound.class);
        assetManager.load("sfx/grunt.mp3", Sound.class);
        assetManager.load("sfx/hey.mp3", Sound.class);
        assetManager.load("atlas/textures.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        sfxExcuseMe = assetManager.get("sfx/excuse-me.mp3");
        sfxFart = assetManager.get("sfx/fart.mp3");
        sfxGrunt = assetManager.get("sfx/grunt.mp3");
        sfxHey = assetManager.get("sfx/hey.mp3");
        TextureAtlas textureAtlas = assetManager.get("atlas/textures.atlas");
        regionBalloon = textureAtlas.findRegion("balloon");
        regionExclaim = textureAtlas.findRegion("exclaim");
        regionShocked = textureAtlas.findRegion("shocked");
        regionUpset = textureAtlas.findRegion("upset");
    }
}
