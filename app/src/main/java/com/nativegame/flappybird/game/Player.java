package com.nativegame.flappybird.game;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.nativegame.flappybird.R;
import com.nativegame.flappybird.engine.AnimatedSprite;
import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.GameEvent;
import com.nativegame.flappybird.engine.Sprite;
import com.nativegame.flappybird.game.sound.SoundEvent;

/**
 * Created by Oscar Liang on 2022/06/09
 */

public class Player extends AnimatedSprite {

    private static final int TIME_PER_FRAME = 200;

    private final float mFlapDistance;
    private final int mFloorPosition;
    private float mGravity;
    private float mSpeed;

    private boolean mIsRunning = false;

    public Player(GameEngine gameEngine) {
        super(gameEngine, R.drawable.bird_mid);

        mFlapDistance = (mPixelFactor * 400) / 1000f;    // We want to move at 400 unit per second
        mFloorPosition = gameEngine.mScreenHeight - gameEngine.mScreenWidth / 3 - mHeight;

        // Init animated sprite
        Resources r = gameEngine.getContext().getResources();
        setAnimatedSpriteBitmaps(new Bitmap[]{
                getDefaultBitmap(r.getDrawable(R.drawable.bird_down)),
                getDefaultBitmap(r.getDrawable(R.drawable.bird_mid)),
                getDefaultBitmap(r.getDrawable(R.drawable.bird_up))});
        setTimePreFrame(TIME_PER_FRAME);
    }

    @Override
    public void startGame() {
        mX = mScreenWidth / 3f - mWidth / 2f;
        mY = mScreenHeight / 2f - mHeight / 2f;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        super.onUpdate(elapsedMillis, gameEngine);

        // Check flapping
        if (gameEngine.mInputController.mIsFlying && mIsRunning) {
            mSpeed = 0;   // We reset the speed
            mSpeed -= mFlapDistance;
            gameEngine.mInputController.mIsFlying = false;   // We fly for only one time
            gameEngine.onGameEvent(GameEvent.FLAPPING);
            mSoundManager.playSoundForSoundEvent(SoundEvent.FLAPPING);
        }

        // Update position
        mSpeed += mGravity;
        mY += mSpeed * elapsedMillis;
        if (mY <= 0) {
            mY = 0;
        }
        if (mY >= mFloorPosition) {
            mY = mFloorPosition;
            gameEngine.onGameEvent(GameEvent.GAME_OVER);
            mSoundManager.playSoundForSoundEvent(SoundEvent.GAME_OVER);
        }

        // Update angle
        mRotation = mSpeed * 30;
        if (mRotation >= 90) {
            mRotation = 90;
            mBitmap = mSpriteBitmaps[1];   // We stop the bird flapping when 90 degree
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, Sprite otherSprite) {
        if (otherSprite instanceof Pipe && mIsRunning) {
            gameEngine.onGameEvent(GameEvent.HIT_PIPE);
            mSoundManager.playSoundForSoundEvent(SoundEvent.HIT_PIPE);
            mSpeed = 0;
            mGravity = mGravity * 1.5f;   // Accelerate falling
            mIsRunning = false;
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.START_GAME) {
            mSoundManager.playSoundForSoundEvent(SoundEvent.START_GAME);
            mGravity = (mPixelFactor / 1200f) / 1000f;
            mIsRunning = true;
        }
    }

}
