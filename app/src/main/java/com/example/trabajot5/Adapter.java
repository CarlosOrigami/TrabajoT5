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
    private final ArrayList<MultimediaItem> lista; // Lista de elementos multimedia
    private final Context context; // Contexto de la aplicación
    private MediaPlayer mediaPlayer; // Reproductor de audio

    // Constructor que recibe el contexto y la lista de elementos
    public Adapter(Context context, ArrayList<MultimediaItem> lista) {
        this.context = context;
        this.lista = lista;
    }

    // Método para inflar la vista de cada ítem de la lista
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_multimedia, parent, false);
        return new ViewHolder(view);
    }

    // Método que asocia los datos del elemento con las vistas
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultimediaItem item = lista.get(position);
        holder.txtTitulo.setText(item.getTitulo()); // Asignar título
        holder.txtDescripcion.setText(item.getDescripcion()); // Asignar descripción

        // Asignar ícono según el tipo de multimedia
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

        // Al hacer clic en el ítem, se muestra un diálogo con detalles
        holder.itemView.setOnClickListener(v -> mostrarDialog(item));
    }

    // Retorna el número total de elementos en la lista
    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Clase interna para manejar las vistas de cada ítem
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

    // Método para mostrar un diálogo con detalles del elemento multimedia
    private void mostrarDialog(MultimediaItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_multimedia, null);
        builder.setView(dialogView);

        // Referencias a las vistas del diálogo
        TextView txtTitulo = dialogView.findViewById(R.id.dialogTitulo);
        TextView txtDescripcion = dialogView.findViewById(R.id.dialogDescripcion);
        VideoView videoView = dialogView.findViewById(R.id.dialogVideoView);
        WebView webView = dialogView.findViewById(R.id.dialogWebView);

        txtTitulo.setText(item.getTitulo()); // Asignar título en el diálogo
        txtDescripcion.setText(item.getDescripcion()); // Asignar descripción

        // Configuración según el tipo de multimedia
        switch (item.getTipo()) {
            case VIDEO:
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse(item.getUrl()));

                // Configurar controles de reproducción de video
                MediaController mediaController = new MediaController(context);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);

                videoView.requestFocus();
                videoView.setOnPreparedListener(mp -> {
                    mp.start(); // Reproducir automáticamente
                    mediaController.show(0); // Mostrar controles
                });
                break;

            case AUDIO:
                try {
                    mediaPlayer = new MediaPlayer(); // Inicializar MediaPlayer
                    mediaPlayer.setDataSource(item.getUrl()); // Establecer la fuente de audio
                    mediaPlayer.prepare(); // Preparar el audio
                    mediaPlayer.start(); // Reproducir
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case WEB:
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient()); // Configurar el WebView para abrir en la app
                webView.loadUrl(item.getUrl()); // Cargar la URL
                break;
        }

        // Botón para cerrar el diálogo y liberar recursos
        builder.setPositiveButton("Cerrar", (dialog, which) -> {
            if (mediaPlayer != null) {
                mediaPlayer.release(); // Liberar recursos del MediaPlayer
                mediaPlayer = null;
            }
            dialog.dismiss(); // Cerrar el diálogo
        });

        AlertDialog dialog = builder.create();
        dialog.show(); // Mostrar el diálogo
    }
}
