package com.nativegame.flappybird.game;

import com.nativegame.flappybird.R;
import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.GameEvent;
import com.nativegame.flappybird.engine.Sprite;
import com.nativegame.flappybird.game.sound.SoundEvent;

/**
 * Created by Oscar Liang on 2022/06/09
 */

public class Pipe extends Sprite {

    private final GameController mController;
    private final float[] mRandomY = {0f, 0.3f, 0.5f, 0.7f};
    private final int mPipeGap;
    private float mSpeed;

    private boolean mIsPass;

    public Pipe(GameController gameController, GameEngine gameEngine, float speed) {
        super(gameEngine, R.drawable.pipe);

        mController = gameController;
        mSpeed = speed * (mPixelFactor * 150) / 1000f;   // We want to move at 150 unit per second
        mPipeGap = (int) (mPixelFactor * 150);   // Set the gap to 150 unit
    }

    public void initUpperPipe(int random) {
        mX = mScreenWidth;
        mY = (int) (-mHeight * mRandomY[random]);
        mRotation = 180;

        mIsPass = false;    // We only add score once
    }

    public void initLowerPipe(int random) {
        mX = mScreenWidth;
        mY = (int) (-mHeight * mRandomY[random]) + mHeight + mPipeGap;
        mRotation = 0;

        mIsPass = true;    // We only add score once
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mX -= mSpeed * elapsedMillis;

        // Add score if pipe has passed
        if (mX <= 0 && !mIsPass) {
            gameEngine.onGameEvent(GameEvent.PASS_PIPE);
            mSoundManager.playSoundForSoundEvent(SoundEvent.PASS_PIPE);
            mIsPass = true;
        }

        // Remove and return to pool
        if (mX <= -mWidth) {
            mController.returnToPool(this);
            gameEngine.removeGameObject(this);
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.HIT_PIPE) {
            mSpeed = 0;   // We stop the pipe
        }
    }

}
