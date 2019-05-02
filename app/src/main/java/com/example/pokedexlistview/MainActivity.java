package com.example.pokedexlistview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private List<Pokemon> pokemonList;
    private static final String TAG = "MainActivity";
    private ListView listView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        pokemonList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar2);
        DownloadDeDados downloadDeDados = new DownloadDeDados();
        String aux = "";
        try {
            aux = downloadDeDados.execute("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Seu codigo aqui
                Intent intent = new Intent(MainActivity.this, MostrarPokemonActivity.class);
                intent.putExtra("name", pokemonList.get(position).getName());
                intent.putExtra("height", pokemonList.get(position).getHeight());
                intent.putExtra("weight", pokemonList.get(position).getWeight());
                intent.putExtra("weakness", pokemonList.get(position).getWeakness().toString());
                intent.putExtra("type", pokemonList.get(position).getType().toString());
                intent.putExtra("url", pokemonList.get(position).getImg());
                startActivity(intent);
            }
        });

        Log.d(TAG, "onCreate: Final!");

    }

    public void downloadImage(List<Pokemon> pokemons){

        String TAG = "downloadImage";
        for(int i = 0; i<pokemons.size(); i++){
            String Url =  pokemons.get(i).getImg();
            String imgUrl = Url.replace("http", "https");
            Log.d(TAG, "downloadImage: progress " + i);
            ImageDownloader imageDownloader = new ImageDownloader();
            try{
                Bitmap imagem = imageDownloader.execute(imgUrl).get();
                pokemons.get(i).setImgBitmap(imagem);
            }   catch (Exception e){
                Log.e(TAG, "downloadImage: Erro ao baixar imagem"+e.getMessage());
            }
        }

    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

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

    private static List<Pokemon> leFaturasDeJSONString(String jsonString, List pokemonList) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray pokemons = json.getJSONArray("pokemon");

            for (int i = 0; i < pokemons.length(); i++) {
                JSONObject pokemon = pokemons.getJSONObject(i);

                Pokemon p = new Pokemon(
                        pokemon.getInt("id"),
                        pokemon.getString("name"),
                        pokemon.getString("img"),
                        pokemon.getString("height"),
                        pokemon.getString("weight")
                );

                JSONArray tipos = pokemon.getJSONArray("type");
                JSONArray fraquezas = pokemon.getJSONArray("weaknesses");

                for(int j = 0; j<tipos.length(); j++){
                    p.addTipo(tipos.getString(j));

                }
                for(int j=0;j<fraquezas.length();j++){
                    p.addFraqueza(fraquezas.getString(j));
                }
                pokemonList.add(p);
            }
        } catch (JSONException e) {
            System.err.println("Erro fazendo parse de String JSON: " + e.getMessage());
        }

        return pokemonList;
    }



    private class DownloadDeDados extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            leFaturasDeJSONString(s, pokemonList);
            //progressBar.setMax(pokemonList.size());
            downloadImage(pokemonList);
            PokedexViewAdapter pokeListAdapter = new PokedexViewAdapter(MainActivity.this,
                    R.layout.list_items, pokemonList);
            listView.setAdapter(pokeListAdapter);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "doInBackground: "+strings[0]);
            String json = downloadRSS(strings[0]);
            if (json == null){
                Log.e(TAG, "doInBackground: Erro baixando JSON");
            }

            return json;
        }

        private String downloadRSS(String urlString){
            StringBuilder jsonString = new StringBuilder();

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int resposta  = connection.getResponseCode();
                Log.e(TAG, "downloadRSS: Código de resposta: "+resposta );

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                int charsLidos;
                char[] InputBuffer = new char[500];
                while(true){
                    charsLidos = reader.read(InputBuffer);
                    if(charsLidos<0){
                        break;
                    }
                    if(charsLidos>0){
                        jsonString.append(String.copyValueOf(InputBuffer, 0, charsLidos));
                    }
                }
                reader.close();
                return jsonString.toString();
            }
            catch (MalformedURLException e){
                Log.e(TAG, "downloadRSS: URL é invalido" + e.getMessage());

            } catch (IOException e) {
                Log.e(TAG, "downloadRSS: Ocorreu um erro de IO ao baixar dados: "+e.getMessage() );

            }
            return null;
        }
    }
}
