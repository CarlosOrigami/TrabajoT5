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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final ArrayList<MultimediaItem> lista;
    private final Context context;
    private MediaPlayer mediaPlayer;

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


        switch (item.getTipo()) {
            case VIDEO:
                holder.iconoMultimedia.setImageResource(R.drawable.ic_video);
                break;
            case AUDIO:
                holder.iconoMultimedia.setImageResource(R.drawable.ic_audio);
                break;
            case WEB:
                holder.iconoMultimedia.setImageResource(R.drawable.ic_web);
                break;
        }

        holder.itemView.setOnClickListener(v -> mostrarDialog(item));
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion;
        ImageView iconoMultimedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            iconoMultimedia = itemView.findViewById(R.id.iconoMultimedia);
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

        txtTitulo.setText(item.getTitulo());
        txtDescripcion.setText(item.getDescripcion());

        switch (item.getTipo()) {
            case VIDEO:
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse(item.getUrl()));

                // Configurar MediaController
                MediaController mediaController = new MediaController(context);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);

                videoView.requestFocus();
                videoView.setOnPreparedListener(mp -> {
                    mp.start();
                    mediaController.show(0);
                });
                break;

            case AUDIO:
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(item.getUrl());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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