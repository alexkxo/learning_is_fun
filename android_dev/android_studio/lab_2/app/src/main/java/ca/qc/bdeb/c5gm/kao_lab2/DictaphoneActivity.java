package ca.qc.bdeb.c5gm.kao_lab2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

// SOURCE: https://developer.android.com/guide/topics/media/mediarecorder#java

public class DictaphoneActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    //    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    //    private PlayButton   playButton = null;
    private MediaPlayer   player = null;

    private FloatingActionButton fbtn_enregistrer;
    private FloatingActionButton fbtn_jouer;

    // Demande des permissions pour enregistrer
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    // Boolean pour savoir si l'enregistrement est en cours
    private boolean enTrainDenregistrer = false;

    // Boolean pour savoir si l'audio joue
    private boolean enTrainDeJouerAudio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictaphone);
        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        fbtn_enregistrer = findViewById(R.id.btn_enregistrer);
        fbtn_jouer = findViewById(R.id.btn_jouer);

        fbtn_enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enTrainDenregistrer)
                {
                    stopRecording();
                    Toast.makeText(DictaphoneActivity.this,
                            "Arr??t de l'enregistrement ...", Toast.LENGTH_SHORT).show();

                    enTrainDenregistrer = false;
                    Log.d("etatEnregistrer", "enTrainDenregistrer?: " + enTrainDenregistrer);

                    // l'??cran reste allum??
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Log.d("etatEnregistrer", "ECRAN RESTE PAS ALLUME");
                }
                else
                {
                    startRecording();
                    Toast.makeText(DictaphoneActivity.this,
                            "D??marrage de l'enregistrement ...", Toast.LENGTH_SHORT).show();

                    enTrainDenregistrer = true;
                    Log.d("etatEnregistrer", "enTrainDenregistrer?: " + enTrainDenregistrer);

                    // l'??cran reste allum??
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Log.d("etatEnregistrer", "ECRAN RESTE ALLUME");

                }
            }
        });

        fbtn_jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enTrainDeJouerAudio)
                {
                    stopPlaying();
                }
                else
                {
                    startPlaying();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    /**
     * D??termine l'??tat de l'enregistrement
     * @param start
     */
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {

        fbtn_jouer.setImageResource(R.drawable.ic_baseline_stop_24);
        Toast.makeText(DictaphoneActivity.this,
                "Jouer ...", Toast.LENGTH_SHORT).show();
        enTrainDeJouerAudio = true;
        Log.d("etatJouer", "onClick: " + enTrainDeJouerAudio);

        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {

        Toast.makeText(DictaphoneActivity.this,
                "Arr??ter ...", Toast.LENGTH_SHORT).show();
        enTrainDeJouerAudio = false;
        Log.d("etatJouer", "onClick: " + enTrainDeJouerAudio);
        fbtn_jouer.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        player.release();
        player = null;
    }

    private void startRecording() {
        // changement d'image ?? stop
        fbtn_enregistrer.setImageResource(R.drawable.ic_baseline_stop_24);


        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        // changement d'image ?? record
        fbtn_enregistrer.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);

        recorder.stop();
        recorder.release();
        recorder = null;
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean enTrainDenregistrer = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(enTrainDenregistrer);
                if (enTrainDenregistrer) {
                    setText("Arr??ter l'enregistrement");
                } else {
                    setText("D??marrer l'enregistrement");
                }
                enTrainDenregistrer = !enTrainDenregistrer;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Commencer l'enregistrement");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Arr??ter l'??coute de l'enregistrement");
                } else {
                    setText("D??marrer l'??coute de l'enregistrement");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("D??marrer l'??coute de l'enregistrement");
            setOnClickListener(clicker);
        }
    }




    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}