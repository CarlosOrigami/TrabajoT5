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
    private VideoView videoView; // Vista para reproducir videos
    private MediaPlayer mediaPlayer; // Reproductor de audio
    private WebView webView; // Vista para mostrar contenido web
    private Button btnVolver; // Botón para volver a la actividad anterior

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle); // Establece el diseño de la actividad

        // Inicialización de las vistas
        videoView = findViewById(R.id.videoView);
        webView = findViewById(R.id.webView);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtiene los datos enviados desde la actividad anterior
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String url = getIntent().getStringExtra("url");
        String tipo = getIntent().getStringExtra("tipo");

        setTitle(titulo); // Establece el título de la actividad

        // Lógica para mostrar el contenido según el tipo
        if (tipo.equals("VIDEO")) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(url)); // Establece la URI del video
            videoView.start(); // Reproduce el video
        } else if (tipo.equals("AUDIO")) {
            mediaPlayer = new MediaPlayer(); // Inicializa el reproductor de audio
            try {
                mediaPlayer.setDataSource(url); // Establece la fuente de audio
                mediaPlayer.prepare(); // Prepara el audio para la reproducción
                mediaPlayer.start(); // Inicia la reproducción
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de errores en caso de fallo
            }
        } else if (tipo.equals("WEB")) {
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url); // Carga la URL en el WebView
        }

        // Configura el botón para volver a la actividad anterior
        btnVolver.setOnClickListener(v -> finish());
    }

    // Libera los recursos del reproductor de audio al destruir la actividad
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Libera los recursos del MediaPlayer
        }
    }
}
