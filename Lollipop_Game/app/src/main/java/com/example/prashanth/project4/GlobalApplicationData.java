package com.example.prashanth.project4;

import android.app.Application;

/**
 * Created by prashanth on 7/14/15.
 */
public class GlobalApplicationData extends Application {
    private static int score=0;
    private static boolean if_show_score=false;

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        GlobalApplicationData.score = score;
    }

    public static boolean isIf_show_score() {
        return if_show_score;
    }

    public static void setIf_show_score(boolean if_show_score) {
        GlobalApplicationData.if_show_score = if_show_score;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
