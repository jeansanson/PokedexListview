package com.example.pokedexlistview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MostrarPokemonActivity extends AppCompatActivity {

    private ImageView pokemonImage;
    private TextView pokemonName;
    private TextView pokemonType;
    private TextView pokemonWeakness;
    private TextView pokemonHeight;
    private TextView pokemonWeight;
    private static final String TAG = "MostrarPokemonActivity";

    public void voltar(View view) {
        Intent intent = new Intent(MostrarPokemonActivity.this, MainActivity.class);

        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_pokemon_);
        Log.d(TAG, "onCreate: ");

        pokemonImage = findViewById(R.id.pokemonImage);
        pokemonName = findViewById(R.id.pokemonName);
        pokemonType = findViewById(R.id.pokemonType);
        pokemonWeakness = findViewById(R.id.pokemonWeakness);
        pokemonHeight = findViewById(R.id.pokemonHeight);
        pokemonWeight = findViewById(R.id.pokemonWeight);

        Intent intent = getIntent();
        String url = intent.getSerializableExtra("url").toString();
        downloadImage(url);

        pokemonName.setText(intent.getSerializableExtra("name").toString());
        pokemonWeight.setText(intent.getSerializableExtra("weight").toString());
        pokemonHeight.setText(intent.getSerializableExtra("height").toString());
        pokemonType.setText(intent.getSerializableExtra("type").toString());
        pokemonWeakness.setText(intent.getSerializableExtra("weakness").toString());
    }

    public void downloadImage(String url){

        String TAG = "downloadImage";

            String imgUrl = url.replace("http", "https");
            MostrarPokemonActivity.ImageDownloader imageDownloader = new MostrarPokemonActivity.ImageDownloader();
            try{
                Bitmap imagem = imageDownloader.execute(imgUrl).get();
                pokemonImage.setImageBitmap(imagem);
            }   catch (Exception e){
                Log.e(TAG, "downloadImage: Erro ao baixar imagem"+e.getMessage());
            }


    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        private static final String TAG = "ImageDownloader";

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (Exception e){
                Log.e(TAG, "doInBackground: Erro ao baixar imagem" + e.getMessage());
            }
            return null;
        }
    }

}
