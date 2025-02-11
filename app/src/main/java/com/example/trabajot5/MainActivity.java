package com.example.trabajot5;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MultimediaItem> listaMultimedia;
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarLista();

        adapter = new Adapter(this, listaMultimedia);
        recyclerView.setAdapter(adapter);
    }

    private void cargarLista() {
        listaMultimedia = new ArrayList<>();

        listaMultimedia.add(new MultimediaItem("Video 1", "Un paisaje cubierto de nieve.",
                "android.resource://" + getPackageName() + "/" + R.raw.video1, MultimediaItem.TipoMultimedia.VIDEO));

        listaMultimedia.add(new MultimediaItem("Video 2", "Deliciosa preparación de una pizza.",
                "android.resource://" + getPackageName() + "/" + R.raw.video2, MultimediaItem.TipoMultimedia.VIDEO));


        listaMultimedia.add(new MultimediaItem("Audio 1", "Ritmo vibrantes de dancehall.",
                "android.resource://" + getPackageName() + "/" + R.raw.audio1, MultimediaItem.TipoMultimedia.AUDIO));

        listaMultimedia.add(new MultimediaItem("Audio 2", "Ritmo perfecto para una fiesta.",
                "android.resource://" + getPackageName() + "/" + R.raw.audio2, MultimediaItem.TipoMultimedia.AUDIO));

        listaMultimedia.add(new MultimediaItem("Página Web 1", "Google", "https://www.google.com", MultimediaItem.TipoMultimedia.WEB));
        listaMultimedia.add(new MultimediaItem("Página Web 2", "Wikipedia", "https://www.wikipedia.org", MultimediaItem.TipoMultimedia.WEB));
    }
}
