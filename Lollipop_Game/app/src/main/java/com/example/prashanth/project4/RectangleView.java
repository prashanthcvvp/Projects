package com.example.prashanth.project4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.View;


import java.io.IOException;
import java.util.Random;

/**
 * Created by prashanth on 7/11/15.
 */
public class RectangleView extends View {
    private int x = 0, top_align = 0, bottom = 0;
    private int y = 700, top_align_y = 0, bottom_y = 0;


    private Paint paint = new Paint();
    private Random ran = new Random();
    private boolean start = true;

    private boolean if_jump = false;


    private boolean if_jump_process = false;
    int jump_loop = 0;
    private boolean stop_loop = false;

    private int y_top_image = 0, y_bottom_image = 0, jump_limit = 0;
    private Drawable image_lollipop;

    private boolean is_game_over = false;
    private Bitmap game_over, waves_bitmap,back_ground_bitmap;

    private boolean if_reset = false;

    private Activity main_game_activity;

    private int score = 0;


    private Rect rect_red, rect_blue, rect_image_frame;


    SoundPool soundPool;
    int sound_jump = -1, sound_game_over = -1;
    AssetManager assetManager;
    AssetFileDescriptor descriptor_jump, disc_game_over;


    boolean if_play = false;

    public void soundInitialize() {
        this.main_game_activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        assetManager = this.main_game_activity.getAssets();

        try {
            descriptor_jump = assetManager.openFd("jump.wav");
            sound_jump = soundPool.load(descriptor_jump, 1);

            disc_game_over = assetManager.openFd("gameover_sound.wav");
            sound_game_over = soundPool.load(disc_game_over, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public RectangleView(Context context, Activity main_game_activity) {
        super(context);

        rect_red = new Rect();
        rect_blue = new Rect();
        rect_image_frame = new Rect();

        image_lollipop = context.getResources().getDrawable(R.drawable.lollipop);
        Resources res = getContext().getResources();
        game_over = BitmapFactory.decodeResource(res, R.drawable.gameover);
        waves_bitmap = BitmapFactory.decodeResource(res, R.drawable.waves);
        back_ground_bitmap= BitmapFactory.decodeResource(res, R.drawable.dark_sky);
        this.main_game_activity = main_game_activity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (start) {
            top_align = this.getBottom() / 2;
            top_align_y = this.getBottom() / 2;
            y_top_image = top_align + 150;
            y_bottom_image = top_align + 300;

            jump_limit = top_align_y - bottom_y + 300;
            image_lollipop.setBounds(70, y_top_image, 160, y_bottom_image);
            start = false;
        }
        canvas.drawBitmap(back_ground_bitmap,0,0,paint);


        /////////////////////////////////// GAME LOOP /////////////////////////////////////////
        if (!is_game_over) {

            canvas.drawBitmap(waves_bitmap, 0, canvas.getHeight() - waves_bitmap.getHeight() / 2, paint);

            //////////////////////////// BUILDING ONE /////////////////////////////////////
            paint.setColor(Color.RED);
            //canvas.drawRect(x, 0, x + 300, top_align - bottom, paint);
            rect_red.set(x, top_align + 300 - bottom, x + 300, this.getBottom());
            canvas.drawRect(rect_red, paint);
            if (x > -canvas.getWidth() / 2) {
                x = x - 8;
            } else {
                x = canvas.getWidth();
                top_align = this.getBottom() / 2;
                bottom = ran.nextInt(top_align) / 2;
                score++;
                if (!isIf_jump_process()) {
                    is_game_over = true;
                    setIf_play(true);
                }
                jump_limit = top_align_y - bottom_y + 300;


            }

            //////////////////////////// BUILDING TWO /////////////////////////////////////
            paint.setColor(Color.BLUE);
            // canvas.drawRect(y, 0, y + 500, top_align_y - bottom_y, paint);
            rect_blue.set(y, top_align_y + 300 - bottom_y, y + 500, this.getBottom());
            canvas.drawRect(rect_blue, paint);

            if (y > -canvas.getWidth() / 2) {
                y = y - 8;
            } else {
                y = canvas.getWidth();
                top_align_y = this.getBottom() / 2;
                bottom_y = ran.nextInt(top_align_y) / 2;
                score++;
                if (!isIf_jump_process()) {
                    is_game_over = true;
                    setIf_play(true);
                }
                jump_limit = top_align - bottom + 300;
            }

            ////////////////////////// jump logic //////////////////////
            if (isIf_jump()) {
                if (isIf_play()) {
                    setIf_play(false);
                    soundPool.play(sound_jump, 1, 1, 0, 0, 1);
                }
                y_bottom_image = y_bottom_image - 10;
                y_top_image = y_top_image - 10;
                jump_loop++;
                setIf_jump_process(true);
            }
            if (jump_loop == 30) {
                y_bottom_image = y_bottom_image + 10;
                y_top_image = y_top_image + 10;
                setIf_jump(false);
                if_reset = true;
            }
            if ((y_bottom_image > jump_limit) && if_reset) {
                if_reset = false;
                jump_loop = 0;
                y_bottom_image = jump_limit;
                y_top_image = jump_limit - 150;
                setIf_jump_process(false);
            }
            //// jump logic ends //////////////////////

            //// Collision Detection //////////////////////
            if (Rect.intersects(rect_blue, rect_image_frame) || Rect.intersects(rect_red, rect_image_frame)) {
                is_game_over = true;
                setIf_play(true);
            }
            //// Collision Detection //////////////////////

            rect_image_frame.set(70 + 35, y_top_image, 160 + 80, y_bottom_image);
            image_lollipop.setBounds(70, y_top_image, 160, y_bottom_image);
            image_lollipop.draw(canvas);


        } else { ////////// GAME OVER LOGIC ////////////////////////////////////

            if (isIf_play()) {
                setIf_play(false);
                soundPool.play(sound_game_over, 1, 1, 0, 0, 1);
            }

            //////////////////////////// LOLLIPOP IMAGE /////////////////////////////////////
            y_bottom_image = y_bottom_image + 10;
            y_top_image = y_top_image + 10;

            rect_image_frame.set(70 + 35, y_top_image, 160 + 80, y_bottom_image);
            image_lollipop.setBounds(70, y_top_image, 160, y_bottom_image);
            image_lollipop.draw(canvas);

            //////////////////////////// WAVES IMAGE /////////////////////////////////////
            canvas.drawBitmap(waves_bitmap, 0, canvas.getHeight() - waves_bitmap.getHeight() / 2, paint);

            //////////////////////////// BUILDING ONE /////////////////////////////////////
            paint.setColor(Color.RED);
            // canvas.drawRect(x, 0, x + 300, top_align - bottom, paint);
            canvas.drawRect(rect_red, paint);

            //////////////////////////// BUILDING TWO /////////////////////////////////////
            paint.setColor(Color.BLUE);
            //canvas.drawRect(y, 0, y + 500, top_align_y - bottom_y, paint);
            canvas.drawRect(rect_blue, paint);

            if (y_top_image > canvas.getHeight()) {
                stop_loop = true;
            }
            int game_over_img_width = (canvas.getWidth() - game_over.getWidth()) / 2;
            int game_over_img_height = (canvas.getHeight() - game_over.getHeight()) / 2;
            canvas.drawBitmap(game_over, game_over_img_width, game_over_img_height, paint);
        }
        if (!isStop_loop()) {
            invalidate();
        } else {
            GlobalApplicationData.setScore(score - 1);
            GlobalApplicationData.setIf_show_score(true);
            main_game_activity.finish();
        }
    }

    public boolean isStop_loop() {
        return stop_loop;
    }

    public void setStop_loop(boolean stop_loop) {
        this.stop_loop = stop_loop;
    }


    public boolean isIf_jump() {
        return if_jump;
    }

    public void setIf_jump(boolean if_jump) {
        this.if_jump = if_jump;
    }


    public boolean isIf_play() {
        return if_play;
    }

    public void setIf_play(boolean if_play) {
        this.if_play = if_play;
    }

    public boolean isIf_jump_process() {
        return if_jump_process;
    }

    public void setIf_jump_process(boolean if_jump_process) {
        this.if_jump_process = if_jump_process;
    }

}
