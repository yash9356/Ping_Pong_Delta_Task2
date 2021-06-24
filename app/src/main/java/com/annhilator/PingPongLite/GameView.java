package com.annhilator.PingPongLite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import java.util.Random;

public class GameView extends View {
    float TEXT_SIZE = 120.0f;
    final long UPDATE_MILLIS = 30;
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
    Bitmap  background21 ;
    Rect rect;
    Random random;
    Runnable runnable;
    SharedPreferences sharedPreferences;
    Paint textPaint =new Paint();
    Velocity velocity = new Velocity(25, 32);

    public GameView(Context context2) {
        super(context2);
        context = context2;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ping_pong_ball66);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        background21 = BitmapFactory.decodeResource(getResources(), R.drawable.pinger54);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                GameView.this.invalidate();
            }
        };
        mpHit = MediaPlayer.create(context2, R.raw.hit);
        mpMiss = MediaPlayer.create(context2, R.raw.miss);
        Display defaultDisplay = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        Random random2 = new Random();
        random = random2;
        ballX = (float) random2.nextInt(dWidth);
        paddleY = (float) ((dHeight * 4) / 5);
        paddleX = (float) ((dWidth / 2) - (paddle.getWidth() / 2));
        SharedPreferences sharedPreferences2 = context2.getSharedPreferences("my_pref", 0);
        sharedPreferences = sharedPreferences2;
        audioState = Boolean.valueOf(sharedPreferences2.getBoolean("audioState", true));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        ballX += (float) velocity.getX();
        ballY += (float) velocity.getY();
        if (ballX >= ((float) (dWidth - ball.getWidth())) || ballX <= 0.0f) {
            Velocity velocity2 = velocity;
            velocity2.setX(velocity2.getX() * -1);
        }
        if (ballY <= 0.0f) {
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
            velocity4.setX(velocity4.getX() + 1);
            Velocity velocity5 = velocity;
            velocity5.setY((velocity5.getY() + 1) * -1);
            points++;
        }
        canvas.drawBitmap(background21,null,rect,null);
        canvas.drawBitmap(ball, ballX, ballY, (Paint) null);
        canvas.drawBitmap(paddle, paddleX, paddleY, (Paint) null);
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
        if (action != 2) {
            return true;
        }
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
