package com.nativegame.flappybird.game;

import android.graphics.Canvas;

import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.GameEvent;
import com.nativegame.flappybird.engine.GameObject;
import com.nativegame.flappybird.fragment.GameFragment;

import java.util.ArrayList;

/**
 * Created by Oscar Liang on 2022/06/09
 */

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_PIPE = 2000;
    private static final int PIPE_POOL_SIZE = 10;
    private static final int PIPE_LAYER = 0;

    private final GameFragment mParent;
    private final ArrayList<Pipe> mPipePool = new ArrayList<>();

    private GameControllerState mState;
    private long mCurrentTime;

    public GameController(GameEngine gameEngine, GameFragment parent) {
        mParent = parent;
        // We initialize the pool of items now
        for (int i = 0; i < PIPE_POOL_SIZE; i++) {
            mPipePool.add(new Pipe(this, gameEngine, 1));
        }
    }

    @Override
    public void startGame() {
        mState = GameControllerState.WAITING;
        mCurrentTime = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (mState == GameControllerState.SPAWNING_PIPE) {
            mCurrentTime += elapsedMillis;
            if (mCurrentTime >= TIME_BETWEEN_PIPE) {
                spawnPipe(gameEngine);
                mCurrentTime = 0;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    private void spawnPipe(GameEngine gameEngine) {
        // Spawn new pipes
        Pipe upperPipe = mPipePool.remove(0);
        Pipe lowerPipe = mPipePool.remove(0);

        int r = (int) (Math.random() * 4);   // Generate random int from 0 to 3
        upperPipe.initUpperPipe(r);
        lowerPipe.initLowerPipe(r);

        gameEngine.addGameObject(upperPipe, PIPE_LAYER);
        gameEngine.addGameObject(lowerPipe, PIPE_LAYER);
    }

    public void returnToPool(Pipe pipe) {
        mPipePool.add(pipe);
    }

    private void gameOver() {
        mParent.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mParent.startNewGame();
            }
        });
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.START_GAME) {
            mState = GameControllerState.SPAWNING_PIPE;
        } else if (gameEvent == GameEvent.HIT_PIPE) {
            mState = GameControllerState.STOPPING_WAVE;
        } else if (gameEvent == GameEvent.GAME_OVER) {
            gameOver();
        }
    }

}
