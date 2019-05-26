package com.example.android.tictactoe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ImageView b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private ArrayList<ImageView> imagesArray;
    private HashMap<ImageView, Integer> boardMap;
    private int vacant = 0, plyrId = 1, sysId = 2;
    private int turnId;
    private String username;
    private ProgressDialog dialog;
    long milliLeft = 0, min = 0, sec, total_milli = 1000 * 1000;
    CountDownTimer timer;
    View views;

    private TextView play_turn;
    private static final String TAG = "MyMainActivity";
    private ArrayList<ArrayList<ImageView>> allLines;
    private ArrayList<ImagesAndScores> rootsChildrenScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initComponents();
    }

    private void initComponents() {
        boardMap = new HashMap<>();
        b1 = (ImageView) findViewById(R.id.box1);
        b2 = (ImageView) findViewById(R.id.box2);
        b3 = (ImageView) findViewById(R.id.box3);
        b4 = (ImageView) findViewById(R.id.box4);
        b5 = (ImageView) findViewById(R.id.box5);
        b6 = (ImageView) findViewById(R.id.box6);
        b7 = (ImageView) findViewById(R.id.box7);
        b8 = (ImageView) findViewById(R.id.box8);
        b9 = (ImageView) findViewById(R.id.box9);
        play_turn = (TextView) findViewById(R.id.player1_text);

        this.timerStart(total_milli);
        imagesArray = new ArrayList<>();
        username = getIntent().getStringExtra("name");
        turnId = new Random().nextInt(3 - 1) + 1;
        play_turn.setText(username.toString());
        rootsChildrenScores = new ArrayList<>();
        this.clearBoard();

        this.setListener();
        initMap();
        initLines();
        // Button

    }

    private void initMap() {
        boardMap.put(b1, 0);
        boardMap.put(b2, 0);
        boardMap.put(b3, 0);
        boardMap.put(b4, 0);
        boardMap.put(b5, 0);
        boardMap.put(b6, 0);
        boardMap.put(b7, 0);
        boardMap.put(b8, 0);
        boardMap.put(b9, 0);
    }

    private void setListener() {
        if (turnId == 2) {
            startGame(views);
            this.timerStart(total_milli);
        }

        for (ImageView im : imagesArray) {
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    views = view;
                    startGame(view);
                }
            });
        }
    }


    private void initLines() {
        allLines = new ArrayList<>();
        ArrayList<ImageView> oneLine = new ArrayList<>();
        oneLine.add(b1);
        oneLine.add(b5);
        oneLine.add(b9);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b3);
        oneLine.add(b5);
        oneLine.add(b7);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b4);
        oneLine.add(b5);
        oneLine.add(b6);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b2);
        oneLine.add(b5);
        oneLine.add(b8);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b1);
        oneLine.add(b2);
        oneLine.add(b3);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b3);
        oneLine.add(b6);
        oneLine.add(b9);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b7);
        oneLine.add(b8);
        oneLine.add(b9);
        allLines.add(oneLine);

        oneLine = new ArrayList<>();
        oneLine.add(b1);
        oneLine.add(b4);
        oneLine.add(b7);
        allLines.add(oneLine);

    }

    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    private boolean hasXWon() {
        for (ArrayList<ImageView> line : allLines) {
            if (wonGame(line, plyrId)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOWon() {
        for (ArrayList<ImageView> line : allLines) {
            if (wonGame(line, sysId)) {
                //           Log.d(TAG,"System might loose!");
                return true;
            }
        }
        return false;
    }

    private ArrayList<ImageView> getAvailableStates() {
        ArrayList<ImageView> availableStates = new ArrayList<>();
        for (ImageView im : imagesArray)
            if (boardMap.get(im) == vacant) {
                availableStates.add(im);
            }

        return availableStates;
    }

    private void placeAMove(ImageView im) {
        rootsChildrenScores.clear();
        updateImage(im, sysId);
        markBox(im, sysId);
    }

    public ImageView returnBestMove() {
        int MAX = -100000;
        int best = -1;
        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).im;
    }

    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public void callMinimax(int depth, int turn) {
        minimax(depth, turn);
    }


    public int minimax(int depth, int turn) {
        if (hasOWon()) return +1;
        if (hasXWon()) return -1;

        ArrayList<ImageView> imagesAvailable = getAvailableStates();
        if (imagesAvailable.isEmpty()) return 0;

        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 0; i < imagesAvailable.size(); ++i) {

            ImageView im = imagesAvailable.get(i);
            if (turn == sysId) {
                if (boardMap.get(im) == vacant) boardMap.put(im, sysId);
                int currentScore = minimax(depth + 1, plyrId);
                scores.add(currentScore);
                if (depth == 0) {

                    rootsChildrenScores.add(new ImagesAndScores(currentScore, im, itos(im)));
                }
            } else if (turn == plyrId) {
                if (boardMap.get(im) == vacant) boardMap.put(im, plyrId);
                scores.add(minimax(depth + 1, sysId));
            }
            boardMap.put(im, vacant);
        }
        return turn == sysId ? returnMax(scores) : returnMin(scores);
    }

    private void whoWon() {
        if (isGameOver()) {
            if (hasOWon()) {

                Intent intent = new Intent(MainActivity.this, Result_Activity.class);
                intent.putExtra("name", username);
                intent.putExtra("time", timerStop());
                intent.putExtra("result", "Lost");
                startActivity(intent);
                finish();
                //lossScore.setText(String.valueOf(losses));
            } else if (hasXWon()) {

                Intent intent = new Intent(MainActivity.this, Result_Activity.class);
                intent.putExtra("name", username);
                intent.putExtra("time", timerStop());
                intent.putExtra("result", "Win");
                startActivity(intent);
                finish();
            } else if (isFull()) {

                Intent intent = new Intent(MainActivity.this, Result_Activity.class);
                intent.putExtra("name", username);
                intent.putExtra("time", timerStop());
                intent.putExtra("result", "Draw");
                startActivity(intent);
                finish();
            }
        }
    }

    private void startGame(View v) {
        if (isGameOver()) {
            return;
        }

        Random rand = new Random();
        if (isFull()) {
            return;
        }

        if (turnId == plyrId) {
            if (tryMarking(v))
                startGame(v);
            return;
        }

        if (turnId == sysId & isEmpty()) {
            ImageView im = imagesArray.get(rand.nextInt(10));
            timerPause();
            placeAMove(im);
            timerResume();
            this.turnId = plyrId;
            return;
        } else if (turnId == sysId & !isEmpty()) {
            timerPause();
            callMinimax(0, sysId);
            placeAMove(returnBestMove());
            timerResume();
            this.turnId = plyrId;
            return;
        }
    }

    private boolean isEmpty() {

        for (ImageView im : imagesArray)
            if (boardMap.get(im) != vacant)
                return false;
        return true;
    }


    private boolean isNotMarked(ImageView tv) {
        return boardMap.get(tv) == vacant;
    }


    private void markBox(ImageView tv, int turnId) {
        boardMap.put(tv, turnId);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                whoWon();
            }
        }, 3000);

    }


    private boolean tryMarking(View v) {
        ImageView calledTv = (ImageView) findViewById(v.getId());
        if (isNotMarked(calledTv) & turnId == plyrId) {
            updateImage(calledTv, plyrId);
            markBox(calledTv, turnId);
            this.turnId = sysId;
            return true;
        }
        return false;
    }


    private void updateImage(final ImageView tv, int turnId) {

        if (turnId == plyrId) tv.setImageResource(R.drawable.cctb);
        else {

            dialog = ProgressDialog.show(this, "", "AI is processing the next step",
                    true);
            dialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    tv.setImageResource(R.drawable.crosstb);
                }
            }, 3000);

        }
    }


    private boolean isFull() {
        for (ImageView im : imagesArray) {
            int id = boardMap.get(im);
            if (id == vacant) return false;
        }
        return true;
    }

    private boolean wonGame(ArrayList<ImageView> curLine, int turnId) {
        ImageView v1 = curLine.get(0);
        ImageView v2 = curLine.get(1);
        ImageView v3 = curLine.get(2);
        int id1 = boardMap.get(v1);
        int id2 = boardMap.get(v2);
        int id3 = boardMap.get(v3);
        if (id1 == turnId & id2 == turnId & id3 == turnId) return true;
        return false;
    }

    private void clearBoard() {

        imagesArray = new ArrayList<>();
        imagesArray.add(b1);
        imagesArray.add(b2);
        imagesArray.add(b3);
        imagesArray.add(b4);
        imagesArray.add(b5);
        imagesArray.add(b6);
        imagesArray.add(b7);
        imagesArray.add(b8);
        imagesArray.add(b9);
        rootsChildrenScores.clear();
        for (ImageView img : imagesArray) {
            img.setImageDrawable(null);
        }
        initMap();
        initLines();
    }

    private String itos(ImageView im) {
        if (im.equals(b1)) return "b1";
        else if (im.equals(b2)) return "b2";
        else if (im.equals(b3)) return "b3";
        else if (im.equals(b4)) return "b4";
        else if (im.equals(b5)) return "b5";
        else if (im.equals(b6)) return "b6";
        else if (im.equals(b7)) return "b7";
        else if (im.equals(b8)) return "b8";
        else if (im.equals(b9)) return "b9";
        return "None";
    }

    public void timerPause() {
        timer.cancel();
    }

    private void timerResume() {

        timerStart(milliLeft);
    }

    public void timerStart(long timeLengthMilli) {
        timer = new CountDownTimer(timeLengthMilli, 1000) {

            @Override
            public void onTick(long milliTillFinish) {
                milliLeft = milliTillFinish;
                min = (milliTillFinish / (1000 * 60));
                sec = ((milliTillFinish / 1000) - min * 60);
                Log.i("Tick", "Tock");
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();

    }

    private String timerStop() {
        long remaining = total_milli - milliLeft;
        min = (remaining / (1000 * 60));
        String secs = String.valueOf(((remaining / 1000) - min * 60));
        return secs;
    }

}
