package com.nativegame.flappybird.game;

import android.graphics.Canvas;
import android.widget.TextView;

import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.GameEvent;
import com.nativegame.flappybird.engine.GameObject;

public class ScoreGameObject extends GameObject {

    private static final int POINTS_GAINED_PER_PIPE_PASS = 1;

    private final TextView mText;
    private boolean mPointsHaveChanged;
    private int mPoints;

    public ScoreGameObject(GameEngine gameEngine, int viewResId) {
        mText = (TextView) gameEngine.mActivity.findViewById(viewResId);
    }

    @Override
    public void startGame() {
        mPoints = 0;
        mText.post(mUpdateTextRunnable);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mPointsHaveChanged) {
            mText.post(mUpdateTextRunnable);
            mPointsHaveChanged = false;
        }
    }

    private final Runnable mUpdateTextRunnable = new Runnable() {
        @Override
        public void run() {
            String text = String.valueOf(mPoints);
            mText.setText(text);
        }
    };

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.PASS_PIPE) {
            mPoints += POINTS_GAINED_PER_PIPE_PASS;   // Add one point if player pass a pipe
            mPointsHaveChanged = true;
        }
    }

}
