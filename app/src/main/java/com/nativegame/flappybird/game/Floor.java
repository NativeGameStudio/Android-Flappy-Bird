package com.nativegame.flappybird.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.GameEvent;
import com.nativegame.flappybird.engine.GameObject;

/**
 * Created by Oscar Liang on 2022/06/09
 */

public class Floor extends GameObject {

    private final int mScreenWidth;
    private final int mScreenHeight;
    private final int mWidth;
    private final int mHeight;
    private float mX;
    private float mY;
    private float mSpeed;

    private final float mPixelFactor;
    private final Bitmap mBitmap;
    private final Matrix mMatrix = new Matrix();

    public Floor(GameEngine gameEngine, int drawableRes, float speed) {
        Drawable drawable = gameEngine.getContext().getResources().getDrawable(drawableRes);
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        mPixelFactor = gameEngine.mPixelFactor;

        mScreenWidth = gameEngine.mScreenWidth;
        mScreenHeight = gameEngine.mScreenHeight;
        mWidth = (int) (mBitmap.getWidth() * mPixelFactor);
        mHeight = (int) (mBitmap.getHeight() * mPixelFactor);

        mSpeed = speed * (gameEngine.mPixelFactor * 150) / 1000f;   // We want to move at 150 unit per second
    }

    @Override
    public void startGame() {
        mX = 0;
        mY = mScreenHeight - mHeight;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mX -= mSpeed * elapsedMillis;
        if (mX <= -mWidth) {
            mX = 0;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        mMatrix.reset();
        mMatrix.postScale(mPixelFactor, mPixelFactor);
        mMatrix.postTranslate(mX, mY);
        canvas.drawBitmap(mBitmap, mMatrix, null);

        mMatrix.reset();
        mMatrix.postScale(mPixelFactor, mPixelFactor);
        mMatrix.postTranslate(mX + mWidth, mY);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.HIT_PIPE) {
            mSpeed = 0;   // We stop the floor when hit the pipe
        }
    }

}
