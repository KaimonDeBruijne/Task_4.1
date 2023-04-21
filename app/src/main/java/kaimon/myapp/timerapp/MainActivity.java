package kaimon.myapp.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView workRemain, restRemain;
    Button startStopButton;
    ProgressBar restProgress, workProgress;
    CountDownTimer work, rest;
    boolean timerOn;
    EditText workInput, restInput;
    long workMill, restMill, workTrack, restTrack;
    int i = 0;
    String off = "0:00";
    MediaPlayer endSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        endSound = MediaPlayer.create(this, R.raw.smallbeep1);

        workRemain = findViewById(R.id.workRemain);
        restRemain = findViewById(R.id.restRemain);
        workProgress = findViewById(R.id.workProgress);
        restProgress = findViewById(R.id.restProgress);
        startStopButton = findViewById(R.id.startStop);
        workInput = findViewById(R.id.worktimeInput);
        restInput = findViewById(R.id.restTimeInput);

        workRemain.setText(off);
        restRemain.setText(off);

        timerOn = false;

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String workTime  = workInput.getText().toString();
                workMill = Long.parseLong(workTime)*1000;

                String restTime = restInput.getText().toString();
                restMill = Long.parseLong(restTime)*1000;

                StartStop();
            }
        });
    }
    //when the start/stop button is pressed check if the timer is on,
    //if so stop the timer else start the timer
    private void StartStop() {
        if(timerOn){
            stopWorkTimer();
            stopRestTimer();
        } else{
            startWorkTimer();
        }
    }
    //Stop the work timer and update the display to reflect that.
    private void stopWorkTimer(){
        timerOn = false;
        work.cancel();
        workProgress.setProgress(0);
        i = 0;
        workRemain.setText(off);
        restRemain.setText(off);
    }
    //Stop the work timer and update the display to reflect that.
    //add a check so that the app doesn't crash if the rest timer is stopped
    //before it is created.
    private void stopRestTimer(){
        timerOn = false;
        if(rest != null){
            rest.cancel();
        }
        restProgress.setProgress(0);
        i = 0;
        restRemain.setText(off);
        workRemain.setText(off);
    }

    //create the rest timer and pass in the user input.
    //update the progress bar to match the timer.
    //when finished, play a sound, stop the timer to reset the display
    //and start the work timer
    private void startRestTimer() {
        timerOn = true;
        rest = new CountDownTimer(restMill, 1000) {
            @Override
            public void onTick(long l) {
                restTrack = l;
                updateRestTimer();
                i++;
                restProgress.setProgress((int)i*100/((int)restMill/1000));
            }
            @Override
            public void onFinish() {
                endSound.start();
                stopRestTimer();
                startWorkTimer();
            }
        }.start();
    }
    //create the work timer and pass in the user input.
    //update the progress bar to match the timer.
    //when finished, play a sound, stop the timer to reset the display
    //and start the rest timer. Also track the timer to update the display.
    private void startWorkTimer() {
        timerOn = true;
        work = new CountDownTimer(workMill, 1000) {
            @Override
            public void onTick(long l) {
                workTrack = l;
                i++;
                updateWorkTimer();
                workProgress.setProgress((int)i*100/((int)workMill/1000));
            }
            @Override
            public void onFinish() {
                endSound.start();
                stopWorkTimer();
                startRestTimer();
            }
        }.start();
    }
    //update the workRemain text to show how much time is left
    private void updateWorkTimer(){
        int minutes = (int) workTrack / 60000;
        int seconds = (int) workTrack % 60000 / 1000;

        String workTimeLeft;

        workTimeLeft = "" + minutes;
        workTimeLeft += ":";
        if(seconds < 10) workTimeLeft += "0";
        workTimeLeft += seconds;

        workRemain.setText(workTimeLeft);
    }
    //update the restRemain text to show how much time is left
    private void updateRestTimer(){
        int minutes = (int) restTrack / 60000;
        int seconds = (int) restTrack % 60000 / 1000;

        String restTimeLeft;

        restTimeLeft = "" + minutes;
        restTimeLeft += ":";
        if(seconds < 10) restTimeLeft += "0";
        restTimeLeft += seconds;

        restRemain.setText(restTimeLeft);
    }
}