package com.example.highlow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.animation.PathInterpolatorCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;

public class MainActivity extends AppCompatActivity {
    int limit = 30;
    int currentValue = 0;
    int valueToBeGuessed;
    int lastGuessedValue = 0;
    boolean gameStarted = false;
    int triesTookToGuess = 0;
    TextView numberTextView, headingTextView;
    ImageButton lowerButton, higherButton;
    Button mainButton;
    Interpolator smoothInterpolator = PathInterpolatorCompat.create(0.605f, 0.025f, 0.310f, 0.995f);
    KonfettiView viewKonfetti;

    protected int generateRandomNumber(int limit) {
        return (int) (Math.floor(Math.random() * limit) + 1);
    }

    public void handleHigher(View view) {
        if(!gameStarted) return;
        changeValue(+1);
        higherButton.setAlpha(1f);
        higherButton.animate().translationY(-10).scaleX(0.9f).setDuration(60).withEndAction(new Runnable() {
            @Override
            public void run() {
                higherButton.animate().translationY(0).scaleX(1).setDuration(40);
            }
        });
        lowerButton.animate().alpha(0.5f).setDuration(100);
        Log.i("Values", "handleHigher, currentValue: " + currentValue);
    }

    public void handleLower(View view) {
        if(!gameStarted) return;
        changeValue(-1);
        lowerButton.setAlpha(1f);
        lowerButton.animate().translationY(10).scaleX(0.9f).setDuration(60).withEndAction(new Runnable() {
            @Override
            public void run() {
                lowerButton.animate().translationY(0).scaleX(1).setDuration(40);
            }
        });
        higherButton.animate().alpha(0.5f).setDuration(100);
        Log.i("Values", "handleLower, currentValue: " + currentValue);
    }

    public void handleMainButton(View view) {
        if(!gameStarted)
            startGame();
        else {
            if(lastGuessedValue != currentValue)
                this.triesTookToGuess += 1;
            if(currentValue == valueToBeGuessed) {
                headingTextView.setText("YAY! You guessed it. You took " + triesTookToGuess + " tries to guess it was number " + valueToBeGuessed + ".");

                viewKonfetti.build()
                        .addColors(Color.parseColor("#33dbff"), Color.parseColor("#1c0828"), Color.parseColor("#33ff9d"))
                        .setDirection(0.0, 359.0)
                        .setSpeed(0.2f, 4f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .setPosition(-50f, viewKonfetti.getWidth() + 50f, 0f, -300f)
                        .streamFor(200, 2000);
                this.gameStarted = false;
                mainButton.setText("Restart Game");
                higherButton.setAlpha(0.5f);
                lowerButton.setAlpha(0.5f);
                return;
            }
            headingTextView.setText(currentValue > valueToBeGuessed ? "LOWER" : "HIGHER");
            headingTextView.animate().scaleX(1.75f).scaleY(1.75f).setDuration(200).setInterpolator(smoothInterpolator).withEndAction(new Runnable() {
                @Override
                public void run() {
                    headingTextView.animate().scaleX(1f).scaleY(1f).setDuration(120).setInterpolator(smoothInterpolator);
                }
            });
            lastGuessedValue = currentValue;
            Log.i("Values", "valueToBeGuessed: " + valueToBeGuessed + " currentValue: " + currentValue + " tookTriesToGuess: " + triesTookToGuess);
        }
    }

    public void changeValue(int value) {
        if(currentValue + value > 0 && currentValue + value <= limit)
            currentValue += value;
        numberTextView.setText(Integer.toString(currentValue));
    }

    public void startGame() {
        this.valueToBeGuessed = generateRandomNumber(limit);
        this.gameStarted = true;
        this.triesTookToGuess = 0;
        headingTextView.setText("We rolled a number between 1 to " + limit + ", start guessing.");
        headingTextView.setTextSize(20f);
        mainButton.setText("GUESS");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewKonfetti = findViewById(R.id.viewKonfetti);
        this.higherButton = findViewById(R.id.higherButton);
        this.numberTextView = findViewById(R.id.numberTextView);
        this.headingTextView = findViewById(R.id.headingTextView);
        this.lowerButton = findViewById(R.id.lowerButton);
        this.mainButton = findViewById(R.id.mainButton);
        this.currentValue = (Integer.parseInt(numberTextView.getText().toString()));


        higherButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    handleHigher(v);
                    return true;
                }
                return false;
            }
        });

        lowerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    handleLower(v);
                    return true;
                }
                return false;
            }
        });
    }
}
