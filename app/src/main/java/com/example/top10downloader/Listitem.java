package com.example.top10downloader;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class Listitem extends AppCompatActivity {
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_record101);
        TextView name =  findViewById(R.id.Titletext);
        TextView artist = findViewById(R.id.Artisttext);
        TextView RD = findViewById(R.id.releasedate);
        TextView Suma = findViewById(R.id.summarytext);
        ImageView image = findViewById(R.id.imageView);
        Suma.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        x = intent.getIntExtra(" position", 0);
        FeedEntry FE = MainActivity.PA.getApplications().get(x);
        Suma.setText(FE.getSummary());
        name.setText(FE.getName());
        Picasso.get().load( FE.getImageURL()).into(image);

        artist.setText(FE.getArtist());
        RD.setText(FE.getReleaseDate());


    }


}
