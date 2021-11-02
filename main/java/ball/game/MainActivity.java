package ball.game;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel, startLabel, missclicksLabel;
    private ImageView ball;

    private int screenWidth;
    private int screenHeight;
    private int frameHeight;

    private float ballX, ballY;
    private int ballSpeed;

    private int score;

    public int missclicks;

    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        missclicksLabel = findViewById(R.id.missclicksLabel);

        ball = findViewById(R.id.ball);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        ballSpeed = Math.round(screenWidth / 45.0f);

        scoreLabel.setText(getString(R.string.score, score));
        missclicksLabel.setText(getString(R.string.missclicks, missclicks));

        ball.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ball.setVisibility(View.GONE);
                score += 10;
                scoreLabel.setText(getString(R.string.score, score));
            }
        });

    }

    public void missclicks() {
        if (missclicks > 9) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            startLabel.setVisibility(View.VISIBLE);
            startLabel.setText(getString(R.string.result_title));
        }
    }

    public void changePos() {
        missclicks();
        Random rand = new Random();
        ballY += ballSpeed;
        if (ballY > screenHeight) {
            ballY = 0 - ball.getHeight();
            ballX = rand.nextInt(screenWidth - ball.getWidth());
            ball.setVisibility(View.VISIBLE);
        }
        ball.setX(ballX);
        ball.setY(ballY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!start_flg) {
            start_flg = true;

            FrameLayout frameLayout = findViewById(R.id.frame);
            frameHeight = frameLayout.getHeight();

            startLabel.setVisibility(View.GONE);

            findViewById(R.id.frame).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    missclicks += 1;
                    missclicksLabel.setText(getString(R.string.missclicks, missclicks));
                }

            });

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {}
}