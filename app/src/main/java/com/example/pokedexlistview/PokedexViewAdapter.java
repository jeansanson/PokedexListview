package com.example.pokedexlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PokedexViewAdapter extends ArrayAdapter {

    private static final String TAG = "PokemonListAdapter";
    private int layoutResource;
    private LayoutInflater layoutInflater;
    private List<Pokemon> pokemons;

    public PokedexViewAdapter(Context context, int resource, List<Pokemon> pokemons) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.pokemons = pokemons;
    }

    @Override
    public int getCount() {
        return pokemons.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: ");
        ViewHolder viewHolder;
        
        if (convertView == null) {
            Log.d(TAG, "getView: chamada com um convertView null");
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: recebeu um convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pokemon pokemonAtual = pokemons.get(position);

        viewHolder.tvNome.setText(pokemonAtual.getName());
        viewHolder.tvPeso.setText(pokemonAtual.getWeight());
        viewHolder.tvAltura.setText(pokemonAtual.getHeight());


        String tipos = "";
        for (String t : pokemonAtual.getType()) {
            tipos += t + '\n';
        }

        viewHolder.tvTipo.setText(tipos);


        Bitmap img = null;

        img = pokemonAtual.getImgBitmap();

        viewHolder.ivPokemonImg.setImageBitmap(img);



        return convertView;
    }

    private class ViewHolder {
        final TextView tvNome;
        final TextView tvPeso;
        final TextView tvAltura;
        final TextView tvTipo;
        final ImageView ivPokemonImg;

        ViewHolder(View v) {
            this.tvNome = v.findViewById(R.id.pokemonNome);
            this.tvPeso = v.findViewById(R.id.pokemonWeight);
            this.tvAltura = v.findViewById(R.id.pokemonHeight);
            this.ivPokemonImg = v.findViewById(R.id.pokemonImage);
            this.tvTipo = v.findViewById(R.id.pokemonType);
        }
    }
}
