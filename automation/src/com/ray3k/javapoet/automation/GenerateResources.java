package com.ray3k.javapoet.automation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

public class GenerateResources extends ApplicationAdapter {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		new Lwjgl3Application(new GenerateResources(), config);
	}
    
    @Override
    public void create() {
        FileHandle sfxPath = new FileHandle(Paths.get("core/assets/sfx").toFile());
        FileHandle[] sfxFiles = sfxPath.list("mp3");
        
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("atlas/textures.atlas"));
        Array<AtlasRegion> regions = textureAtlas.getRegions();
    
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("Resources")
                .addModifiers(Modifier.PUBLIC)
                .addField(AssetManager.class, "assetManager", Modifier.PUBLIC, Modifier.STATIC);
        
        for (FileHandle sfxFile : sfxFiles) {
            typeSpecBuilder.addField(Sound.class, toVariableName("sfx" + upperFirstChar(sfxFile.nameWithoutExtension())), Modifier.PUBLIC, Modifier.STATIC);
        }
        
        for (AtlasRegion region : textureAtlas.getRegions()) {
            typeSpecBuilder.addField(AtlasRegion.class, toVariableName("region" + upperFirstChar(region.name)), Modifier.PUBLIC, Modifier.STATIC);
        }
    
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("loadAssets")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("assetManager = new $T()", AssetManager.class);
        
        for (FileHandle sfxFile : sfxFiles) {
            methodSpecBuilder.addStatement("assetManager.load($S, $T.class)", "sfx/" + sfxFile.name(), Sound.class);
        }
        
        methodSpecBuilder.addStatement("assetManager.load(\"atlas/textures.atlas\", $T.class)", TextureAtlas.class)
                .addStatement("assetManager.finishLoading()");
    
        for (FileHandle sfxFile : sfxFiles) {
            methodSpecBuilder.addStatement("$L = assetManager.get($S)", toVariableName("sfx" + upperFirstChar(sfxFile.nameWithoutExtension())), "sfx/" + sfxFile.name());
        }
        
        methodSpecBuilder.addStatement("$T textureAtlas = assetManager.get(\"atlas/textures.atlas\")", TextureAtlas.class);
        for (AtlasRegion atlasRegion : regions) {
            methodSpecBuilder.addStatement("$L = textureAtlas.findRegion($S)", toVariableName("region" + upperFirstChar(atlasRegion.name)), atlasRegion.name);
        }
        
        typeSpecBuilder.addMethod(methodSpecBuilder.build());
        JavaFile javaFile = JavaFile.builder("com.ray3k.javapoet", typeSpecBuilder.build())
                .indent("    ")
                .build();
        
        FileHandle target = new FileHandle(Paths.get("core/src/com/ray3k/javapoet/Resources.java").toFile());
        target.writeString(javaFile.toString(), false);
        
        Gdx.app.exit();
    }
    
    private static String toVariableName(String name) {
        name = name.replaceAll("^[./]*", "").replaceAll("[\\\\/\\-\\s]", "_").replaceAll("['\"]", "");
        String[] splits = name.split("_");
        StringBuilder builder = new StringBuilder(splits[0]);
        for (int i = 1; i < splits.length; i++) {
            String split = splits[i];
            builder.append(Character.toUpperCase(split.charAt(0)));
            builder.append(split.substring(1));
        }
        
        return builder.toString();
    }
    
    private static String upperFirstChar(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
