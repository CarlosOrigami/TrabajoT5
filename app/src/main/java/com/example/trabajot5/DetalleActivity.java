package com.example.trabajot5;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class DetalleActivity extends AppCompatActivity {
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private WebView webView;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        videoView = findViewById(R.id.videoView);
        webView = findViewById(R.id.webView);
        btnVolver = findViewById(R.id.btnVolver);

        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String url = getIntent().getStringExtra("url");
        String tipo = getIntent().getStringExtra("tipo");

        setTitle(titulo);

        if (tipo.equals("VIDEO")) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(url));
            videoView.start();
        } else if (tipo.equals("AUDIO")) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tipo.equals("WEB")) {
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        }

        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
