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
import androidx.core.view.ViewCompat;
import java.util.Random;

public class GameViewH extends View {
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
    Velocity velocity = new Velocity(25, 32);


    public GameViewH(Context context3) {
        super(context3);
        context = context3;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball3);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        paddleai =BitmapFactory.decodeResource(getResources(),R.drawable.paddle);
        paddlepost=BitmapFactory.decodeResource(getResources(),R.drawable.paddlesuper3);
        background21 = BitmapFactory.decodeResource(getResources(), R.drawable.pinger54);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                GameViewH.this.invalidate();
            }
        };
        mpHit = MediaPlayer.create(context3, R.raw.hit);
        mpMiss = MediaPlayer.create(context3, R.raw.miss);
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
        paddleaiY=(float) ((dHeight)/15);
        paddleX = (float) ((dWidth / 2) - (paddle.getWidth() / 2));
        SharedPreferences sharedPreferences2 = context3.getSharedPreferences("my_pref", 0);
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
        if (ballY <= paddleaiY+(paddleai.getHeight())) {
            if (mpHit != null && audioState.booleanValue()) {
                mpHit.start();
            }
            Velocity velocity3 = velocity;
            velocity3.setY(velocity3.getY() * -1);
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
        paddleaiX =(float) (ballX);
        if(paddleaiX>=dWidth-paddleai.getWidth()){
            paddleaiX=dWidth-paddleai.getWidth();
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
//        if (action != 2) {
//            return true;
//        }
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
//        if(motionEvent.getRawX()>dWidth-250.0f){
//           if (motionEvent.getRawY()>dHeight*3/4){
//               if(motionEvent.getRawY()<((dHeight*3)/4+225.0f)){
//                   Powerup();
//               }
//
//           }
//        }
    }

    public void Powerup(){
        Paint paint1 =new Paint();
        paint1.setStrokeWidth(10);
        paint1.setColor(Color.BLUE);
        Canvas canvas1= new Canvas();
        canvas1.drawBitmap(background21,null,rect,null);
        canvas1.drawLine(0,dHeight/2,dWidth,dHeight/2, paint1);
        canvas1.drawBitmap(ball, ballX, ballY,  null);
        canvas1.drawBitmap(paddle, paddleX, paddleY,  null);
        canvas1.drawBitmap(paddlepost,(dWidth-250f),((dHeight*3)/4),null);
        canvas1.drawBitmap(paddleai,paddleaiX,paddleaiY,null);
    }
    private int xVelocity() {
        return new int[]{-35, -30, -25, 25, 30, 35}[this.random.nextInt(6)];
    }
}
