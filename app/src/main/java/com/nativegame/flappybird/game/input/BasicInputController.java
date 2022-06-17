package com.nativegame.flappybird.game.input;

import android.view.MotionEvent;
import android.view.View;

import com.nativegame.flappybird.R;
import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.InputController;

/**
 * Created by Oscar Liang on 2022/06/09
 */

public class BasicInputController extends InputController {

    private final GameEngine mGameEngine;

    public BasicInputController(GameEngine gameEngine) {
        mGameEngine = gameEngine;
        gameEngine.mActivity.findViewById(R.id.game_view).setOnTouchListener(new BasicOnTouchListener());
    }

    private class BasicOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                if (!mGameEngine.isPaused()) {
                    mIsFlying = true;
                }
            }

            return true;
        }
    }

}
