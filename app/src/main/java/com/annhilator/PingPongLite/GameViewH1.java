package com.annhilator.PingPongLite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import java.util.Random;

public class GameViewH1 extends View {
    Boolean audioState;
    Bitmap ball;
    float ballX;
    float ballY;
    Context context;
    int dHeight;
    int dWidth;
    Handler handler;
    int life = 1;
    MediaPlayer mpHit;
    MediaPlayer mpMiss;
    MediaPlayer mpWin;
    float oldPaddleX;
    float oldX;
    Bitmap paddle;
    float paddleX;
    float paddleY;
    int points = 0;
    Bitmap paddleai;
    float paddleaiX;
    float paddleaiY;
    Bitmap paddlepost;
    Bitmap  background21 ;
    Rect rect;
    Random random;
    Runnable runnable;
    SharedPreferences sharedPreferences;
    Velocity velocity = new Velocity(-25, 32);

    Velocity velocity7 = new Velocity(0,0);


    public GameViewH1(Context context4) {
        super(context4);
        context = context4;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball3);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        paddleai =BitmapFactory.decodeResource(getResources(),R.drawable.paddle);
        paddlepost=BitmapFactory.decodeResource(getResources(),R.drawable.paddlesuper3);
        background21 = BitmapFactory.decodeResource(getResources(), R.drawable.pinger54);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                GameViewH1.this.invalidate();
            }
        };
        mpHit = MediaPlayer.create(context4, R.raw.hit);
        mpMiss = MediaPlayer.create(context4, R.raw.miss);
        mpWin =MediaPlayer.create(context4,R.raw.smb_stage_clear);

        Display defaultDisplay = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        Random random2 = new Random();
        random = random2;
        ballX = (float) random2.nextInt(dWidth);
        ballY=(float) (dHeight)/2;
        paddleY = (float) ((dHeight * 7) / 8);
        paddleaiX =(float) 0.0f;
        paddleaiY=(float) ((dHeight)/15);
        paddleX = (float) ((dWidth / 2) - (paddle.getWidth() / 2));
        SharedPreferences sharedPreferences2 = context4.getSharedPreferences("my_pref", 0);
        sharedPreferences = sharedPreferences2;
        audioState = Boolean.valueOf(sharedPreferences2.getBoolean("audioState", true));
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        ballX += (float) velocity.getX();
        ballY += (float) velocity.getY();
        if (ballX >= ((float) (dWidth - ball.getWidth())) || ballX <= 0.0f) {
            Velocity velocity2 = velocity;
            velocity2.setX(velocity2.getX() * -1);
        }
        if(ballY<=0){
            ballY=dHeight/2;
            if(mpWin != null && audioState.booleanValue()){
                mpWin.start();
            }
            Intent intent= new Intent(this.context,GameOver.class);
            intent.putExtra("points", points);
            intent.putExtra("won",true);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        if (ballY > paddleY + ((float) paddle.getHeight())) {
            ballX = (float) (random.nextInt((dWidth - ball.getWidth()) - 1) + 1);
            ballY =0.0f;
            if (mpMiss != null && audioState.booleanValue()) {
                mpMiss.start();
            }
            velocity.setX(xVelocity());
            velocity.setY(32);
            int i = life - 1;
            life = i;
            if (i == 0) {
                Intent intent= new Intent(this.context,GameOver.class);
                intent.putExtra("points", points);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }
        float width = ballX + ((float)ball.getWidth());
        float f = paddleX;
        if (width >= f && ballX <= f + ((float)paddle.getWidth()) && ballY + ((float) ball.getHeight()) >= paddleY && ballY + ((float) ball.getHeight()) <= paddleY + ((float) paddle.getHeight())) {
            if (mpHit != null && audioState.booleanValue()) {
                mpHit.start();
            }
            Velocity velocity4 = velocity;
            velocity4.setX(velocity4.getX() + 3);
            Velocity velocity5 = velocity;
            velocity5.setY((velocity5.getY() + 3) * -1);
            points++;

        }

        float f1 = paddleaiX;
        if (width >= f1 && ballX <= f1 + ((float)paddleai.getWidth()) && ballY + ((float) ball.getHeight()) >= paddleaiY && ballY + ((float) ball.getHeight()) <= paddleaiY + ((float) paddleai.getHeight())) {
            if (mpHit != null && audioState.booleanValue()) {
                mpHit.start();
            }
            Velocity velocity8 = velocity;
            velocity8.setX(velocity8.getX());
            Velocity velocity9 = velocity;
            velocity9.setY((velocity9.getY()) * -1);
        }
        if(paddleaiX>=dWidth-(paddleai.getWidth())){
            velocity7.setX(-paddleai.getWidth());
            paddleaiX=velocity7.getX();
        }
        if(paddleaiX<dWidth-(paddleai.getWidth())){
            velocity7.setX(velocity7.getX()+20);
            paddleaiX=velocity7.getX();
        }


        float middle=dHeight/2;
        Paint paint =new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(background21,null,rect,null);
        canvas.drawLine(0,middle,dWidth,middle, paint);
        canvas.drawBitmap(ball, ballX, ballY,  null);
        canvas.drawBitmap(paddle, paddleX, paddleY,  null);
        //canvas.drawBitmap(paddlepost,(dWidth-250f),((dHeight*3)/4),null);
        canvas.drawBitmap(paddleai,paddleaiX,paddleaiY,null);
        handler.postDelayed(runnable, 30);

    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        if (motionEvent.getY() < paddleY) {
            return true;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            oldX = motionEvent.getX();
            oldPaddleX = paddleX;
        }
//
        float f = oldPaddleX - (oldX - x);
        if (f <= 0.0f) {
            paddleX = 0.0f;
            return true;
        } else if (f >= ((float) (dWidth -paddle.getWidth()))) {
            paddleX = (float) (dWidth - paddle.getWidth());
            return true;
        } else {
            paddleX = f;
            return true;
        }
    }

    private int xVelocity() {
        return new int[]{-35, -30, -25, 25, 30, 35}[this.random.nextInt(6)];
    }
}
