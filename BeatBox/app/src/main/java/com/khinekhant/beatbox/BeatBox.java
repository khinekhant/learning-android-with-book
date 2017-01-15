package com.khinekhant.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${KhineKhant} on 0006,09/06/16.
 */
public class BeatBox {
    private static final String TAG="BeatBox";
    private static final String SOUNDS_FOLDER="sample_sound";
    private static final int MAX_SOUNDS=5;

    private AssetManager mAssetManager;
    private List<Sound> mSounds=new ArrayList<>();
    private SoundPool mSoundPool;


//can get assetmanager from any context
    public BeatBox(Context context) {
        mAssetManager=context.getAssets();
        mSoundPool=new SoundPool(MAX_SOUNDS,AudioManager.STREAM_MUSIC,0);
loadSounds();

    }

    //to get a list of sounds in asset
    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found" + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.d(TAG, "Could not list assets", e);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Log.i(TAG, "The assetPath is " + assetPath);
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound" + filename, ioe);
            }
        }
    }


    public List<Sound> getSounds() {
        return mSounds;
    }


    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd=mAssetManager.openFd(sound.getAssetPath());
        int soundId=mSoundPool.load(afd,1);
        sound.setSoundId(soundId);
    }


    public void play(Sound sound){
        Integer soundId=sound.getSoundId();
        if(soundId==null){
            return;
        }
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    public void release(){
        mSoundPool.release();
    }
}