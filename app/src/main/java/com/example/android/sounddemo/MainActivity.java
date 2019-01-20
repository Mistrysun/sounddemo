package com.example.android.sounddemo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public void play (View view) {

        mediaPlayer.start();
    }

    public void pause (View view) {

        mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // create a variable to access the max volume
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mediaPlayer = MediaPlayer.create(this, R.raw.gta5);

        //setting resource for volume SeekBar
        SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeSeekBar);

        //setting the volume to max
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currentVolume);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("SeekBar Changed", Integer.toString(i));

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar scrubSeekBar = (SeekBar) findViewById(R.id.scrubSeekBar);

        //sets the max duration of the audio file to the seek bar
        scrubSeekBar.setMax(mediaPlayer.getDuration());

        // as you move the slider, it will log the value at its position
        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("Scrub seekbar moved", Integer.toString(i));

                // do something when the seekbar is moved back and forward
                // stuttering is caused here because it constantly seeks and sets the seekbar
                //seekbar has moved.
                //mediaPlayer.seekTo(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(scrubSeekBar.getProgress());
                mediaPlayer.start();

            }
        });

        //schedules a timer to run at a given iteration
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //sets the progress when the seek bar is moved.
                scrubSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
            //allows the progress bar to move along once progress bar has been set
        }, 0, 50);
    }
}
