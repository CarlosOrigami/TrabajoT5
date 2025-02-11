package com.example.trabajot5;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<MultimediaItem> lista;
    private Context context;
    private MediaPlayer mediaPlayer; // 🚩 Declaramos mediaPlayer como una variable de clase

    public Adapter(Context context, ArrayList<MultimediaItem> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_multimedia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultimediaItem item = lista.get(position);
        holder.txtTitulo.setText(item.getTitulo());
        holder.txtDescripcion.setText(item.getDescripcion());

        holder.itemView.setOnClickListener(v -> mostrarDialog(item));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
        }
    }

    private void mostrarDialog(MultimediaItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_multimedia, null);
        builder.setView(dialogView);

        TextView txtTitulo = dialogView.findViewById(R.id.dialogTitulo);
        TextView txtDescripcion = dialogView.findViewById(R.id.dialogDescripcion);
        VideoView videoView = dialogView.findViewById(R.id.dialogVideoView);
        WebView webView = dialogView.findViewById(R.id.dialogWebView);

        // Botones de control para el vídeo
        Button btnPausarReanudar = dialogView.findViewById(R.id.btnPausarReanudar);
        Button btnAvanzar = dialogView.findViewById(R.id.btnAvanzar);
        Button btnRetroceder = dialogView.findViewById(R.id.btnRetroceder);
        View videoControls = dialogView.findViewById(R.id.videoControls);

        // Botones de control para el audio
        Button btnAudioAvanzar = dialogView.findViewById(R.id.btnAudioAvanzar);
        Button btnAudioRetroceder = dialogView.findViewById(R.id.btnAudioRetroceder);
        Button btnAudioDetener = dialogView.findViewById(R.id.btnAudioDetener);
        View audioControls = dialogView.findViewById(R.id.audioControls);

        txtTitulo.setText(item.getTitulo());
        txtDescripcion.setText(item.getDescripcion());

        switch (item.getTipo()) {
            case VIDEO:
                videoView.setVisibility(View.VISIBLE);
                videoControls.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse(item.getUrl()));

                // Reproducir automáticamente cuando esté listo
                videoView.setOnPreparedListener(MediaPlayer::start);

                // Botón Pausar/Reanudar
                btnPausarReanudar.setOnClickListener(v -> {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        btnPausarReanudar.setText("Reanudar");
                    } else {
                        videoView.start();
                        btnPausarReanudar.setText("Pausar");
                    }
                });

                // Botón Avanzar 5 segundos
                btnAvanzar.setOnClickListener(v -> {
                    int newPosition = videoView.getCurrentPosition() + 5000;
                    if (newPosition < videoView.getDuration()) {
                        videoView.seekTo(newPosition);
                    }
                });

                // Botón Retroceder 5 segundos
                btnRetroceder.setOnClickListener(v -> {
                    int newPosition = videoView.getCurrentPosition() - 5000;
                    if (newPosition > 0) {
                        videoView.seekTo(newPosition);
                    } else {
                        videoView.seekTo(0);
                    }
                });
                break;

            case AUDIO:
                audioControls.setVisibility(View.VISIBLE);

                try {
                    if (item.getUrl().startsWith("android.resource://")) {
                        int resId = context.getResources().getIdentifier(
                                item.getUrl().substring(item.getUrl().lastIndexOf("/") + 1),
                                "raw",
                                context.getPackageName()
                        );

                        mediaPlayer = MediaPlayer.create(context, resId);
                        mediaPlayer.start();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(item.getUrl());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Botón Detener
                btnAudioDetener.setOnClickListener(v -> {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null; // Liberar el recurso
                    }
                });

                // Botón Avanzar 5 segundos
                btnAudioAvanzar.setOnClickListener(v -> {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int newPosition = mediaPlayer.getCurrentPosition() + 5000;
                        if (newPosition < mediaPlayer.getDuration()) {
                            mediaPlayer.seekTo(newPosition);
                        }
                    }
                });

                // Botón Retroceder 5 segundos
                btnAudioRetroceder.setOnClickListener(v -> {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int newPosition = mediaPlayer.getCurrentPosition() - 5000;
                        mediaPlayer.seekTo(Math.max(newPosition, 0));
                    }
                });
                break;

            case WEB:
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(item.getUrl());
                break;
        }

        builder.setPositiveButton("Cerrar", (dialog, which) -> {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
