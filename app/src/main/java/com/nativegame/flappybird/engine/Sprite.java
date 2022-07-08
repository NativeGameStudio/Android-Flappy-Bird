package com.nativegame.flappybird.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nativegame.flappybird.game.sound.SoundManager;

public abstract class Sprite extends GameObject {

    private static final boolean DEBUG_MODE = false;

    protected final int mScreenWidth;
    protected final int mScreenHeight;
    protected final int mWidth;
    protected final int mHeight;
    protected float mX;
    protected float mY;
    public float mRotation;
    public float mScale = 1;
    public int mAlpha = 255;

    protected Bitmap mBitmap;
    protected final float mPixelFactor;
    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();

    protected final SoundManager mSoundManager;

    public final Rect mBoundingRect = new Rect(-1, -1, -1, -1);

    protected Sprite(GameEngine gameEngine, int drawableRes) {
        Drawable drawable = gameEngine.getContext().getResources().getDrawable(drawableRes);
        mBitmap = getDefaultBitmap(drawable);
        mPixelFactor = gameEngine.mPixelFactor;

        mScreenWidth = gameEngine.mScreenWidth;
        mScreenHeight = gameEngine.mScreenHeight;
        mWidth = (int) (mBitmap.getWidth() * mPixelFactor);
        mHeight = (int) (mBitmap.getHeight() * mPixelFactor);

        mSoundManager = gameEngine.mSoundManager;
    }

    protected Bitmap getDefaultBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public boolean checkCollision(Sprite otherSprite) {
        return Rect.intersects(mBoundingRect, otherSprite.mBoundingRect);
    }

    public void onCollision(GameEngine gameEngine, Sprite otherObject) {
    }

    @Override
    public void onPostUpdate() {
        mBoundingRect.set(
                (int) mX,
                (int) mY,
                (int) mX + mWidth,
                (int) mY + mHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mX > canvas.getWidth()
                || mY > canvas.getHeight()
                || mX < -mWidth
                || mY < -mHeight) {
            return;
        }

        if (DEBUG_MODE) {
            mPaint.setColor(Color.YELLOW);
            canvas.drawRect(mBoundingRect, mPaint);
        }

        float scaleFactor = mPixelFactor * mScale;
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor);
        mMatrix.postTranslate(mX, mY);
        mMatrix.postRotate(mRotation, mX + mWidth / 2f, mY + mHeight / 2f);
        mPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

}
