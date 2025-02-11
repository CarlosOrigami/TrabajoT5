package com.example.trabajot5;

public class MultimediaItem {
    private String titulo;
    private String descripcion;
    private String url;
    private TipoMultimedia tipo;

    public enum TipoMultimedia { VIDEO, AUDIO, WEB }

    public MultimediaItem(String titulo, String descripcion, String url, TipoMultimedia tipo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.tipo = tipo;
    }

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getUrl() { return url; }
    public TipoMultimedia getTipo() { return tipo; }
}
