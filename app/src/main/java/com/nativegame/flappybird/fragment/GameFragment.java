package com.nativegame.flappybird.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.nativegame.flappybird.R;
import com.nativegame.flappybird.engine.GameEngine;
import com.nativegame.flappybird.engine.GameEvent;
import com.nativegame.flappybird.engine.GameView;
import com.nativegame.flappybird.game.GameController;
import com.nativegame.flappybird.game.Floor;
import com.nativegame.flappybird.game.Player;
import com.nativegame.flappybird.game.ScoreGameObject;
import com.nativegame.flappybird.game.input.BasicInputController;

/**
 * Created by Oscar Liang on 2022/06/09
 */

public class GameFragment extends BaseFragment {

    private GameEngine mGameEngine;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    protected void onLayoutCompleted() {
        startGame();
    }

    private void startGame() {
        GameView gameView = (GameView) getView().findViewById(R.id.game_view);

        // Init engine
        mGameEngine = new GameEngine(getMainActivity(), gameView);
        mGameEngine.setSoundManager(getMainActivity().getSoundManager());

        // Add all the object to engine
        mGameEngine.setInputController(new BasicInputController(mGameEngine));
        mGameEngine.addGameObject(new ScoreGameObject(mGameEngine, R.id.score), 0);
        mGameEngine.addGameObject(new Player(mGameEngine), 1);
        mGameEngine.addGameObject(new Floor(mGameEngine, R.drawable.floor, 1), 2);
        mGameEngine.addGameObject(new GameController(mGameEngine, this), 0);
        // mGameEngine.addGameObject(new FPSCounter(mGameEngine), 2);

        mGameEngine.startGame();

        updateUI();
    }

    private void updateUI() {
        // Init pause button
        ImageButton btnPause = (ImageButton) getMainActivity().findViewById(R.id.pause);
        btnPause.setVisibility(View.INVISIBLE);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGameEngine.isPaused()) {
                    mGameEngine.resumeGame();
                    btnPause.setBackgroundResource(R.drawable.pause);
                } else {
                    mGameEngine.pauseGame();
                    btnPause.setBackgroundResource(R.drawable.resume);
                }
            }
        });

        // Init play button
        ImageButton btnPlay = (ImageButton) getMainActivity().findViewById(R.id.play);
        btnPlay.setVisibility(View.VISIBLE);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);

                mGameEngine.onGameEvent(GameEvent.START_GAME);   // Start the game
            }
        });
    }

    public void startNewGame() {
        // Exit the current game
        mGameEngine.stopGame();
        // Start a new one
        startGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGameEngine != null && mGameEngine.isRunning() && mGameEngine.isPaused()) {
            mGameEngine.resumeGame();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()) {
            mGameEngine.pauseGame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        getMainActivity().finish();
        return true;
    }

}
