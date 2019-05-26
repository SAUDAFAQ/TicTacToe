package com.example.android.tictactoe;

import android.widget.ImageView;

public class ImagesAndScores {
    int score;
    ImageView im;
    String name;
    String TAG = "ImagesANDScores";

    ImagesAndScores(int score, ImageView im, String name) {
        this.score = score;
        this.im = im;
        this.name = name;
    }
}
