package com.example.musica;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTextView, currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay, nextBtn, previousBtn, musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTextView = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        musicIcon = findViewById(R.id.music_icon_big);

        songsList = (ArrayList<AudioModel>)getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();
        MusicPlayerActivity.this.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(mediaPlayer!=null)
                {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));
                    if(seekBar.getProgress() == seekBar.getMax())
                        PlayNextSong();
                }
                new Handler().postDelayed(this, 100);
                if(mediaPlayer.isPlaying())
                {

                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b)
                {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    void setResourcesWithMusic(){
    currentSong = songsList.get(MyMediaPlayer.currentIndex);
    titleTextView.setText(currentSong.getTitle());
    totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));
    pausePlay.setOnClickListener(v-> PausePlay());
    nextBtn.setOnClickListener(v-> PlayNextSong());
    previousBtn.setOnClickListener(v->PlayPreviousSong());
    titleTextView.setSelected(true);
    PlayMusic();

    }
    private void PlayMusic()
    {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());

        } catch (IOException e)
        {
            e.printStackTrace();

        }

    }
    private void PlayNextSong()
    {
        if(MyMediaPlayer.currentIndex==songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();


    } private void PlayPreviousSong()
    {
        if(MyMediaPlayer.currentIndex==0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void PausePlay()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);

        }
        else{ mediaPlayer.start();
            pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);

        }
    }
    public static String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis)% TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)% TimeUnit.MINUTES.toSeconds(1));

    }
}