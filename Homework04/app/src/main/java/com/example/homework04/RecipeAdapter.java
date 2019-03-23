package com.example.homework04;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    ArrayList<Recipe> myData;

    public RecipeAdapter(ArrayList<Recipe> myData) {
        this.myData = myData;
    }

    @Override
    public int getItemViewType(int position) {
        //altered the getItemViewType to return the position of the overall view
        //to have it as an index in the onCreateViewHolder
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int index = i;

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, viewGroup, false);

        //set background to a border design
        view.setBackground(view.getContext().getApplicationContext().getDrawable(R.drawable.border));

        //get all inner views
        TextView titleTextView = view.findViewById(R.id.actual_title_textView);
        TextView ingredientsTextView = view.findViewById(R.id.actual_ingredients_textView);
        TextView urlTextView = view.findViewById(R.id.actual_url_textView);
        ImageView thumbnailImageView = view.findViewById(R.id.thumbnail_imageView);

        //get correct recipe data
        final Recipe recipe = myData.get(index);

        //set text view text with recipe data
        titleTextView.setText(recipe.title);
        ingredientsTextView.setText(recipe.ingredients);
        //set the text color, and then underline and bold it, then set clickable and set the text
        urlTextView.setTextColor(Color.BLUE);
        urlTextView.setPaintFlags(urlTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        urlTextView.setClickable(true);
        urlTextView.setText(recipe.url);

        //setup the hyperlink for the text
        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when you click the link text box, create an intent to take you to the web page
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.url));
                //use the view to get to the application's context to start the activity
                view.getContext().getApplicationContext().startActivity(intent);
            }
        });

        //if the recipe does not have an image url
        if(recipe.image.equals("")) {
            //set the image to null
            thumbnailImageView.setImageBitmap(null);
        } else {
            //else it has an image url
            //load the image into the image view
            Picasso.get().load(recipe.image).placeholder(R.drawable.loading).into(thumbnailImageView);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        //ensure all text displayed is correct for this recipe
        Recipe recipe = myData.get(position);
        viewHolder.titleTextView.setText(recipe.title);
        viewHolder.ingredientsTextView.setText(recipe.ingredients);
        viewHolder.urlTextView.setText(recipe.url);

        //if the recipe does not have an image url
        if(recipe.image.equals("")) {
            //set the image to null
            viewHolder.thumbnailImageView.setImageBitmap(null);
        } else {
            //else it has an image url
            //load the image into the image view
            Picasso.get().load(recipe.image).placeholder(R.drawable.loading).into(viewHolder.thumbnailImageView);
        }
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, ingredientsTextView, urlTextView;
        ImageView thumbnailImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.actual_title_textView);
            ingredientsTextView = itemView.findViewById(R.id.actual_ingredients_textView);
            urlTextView = itemView.findViewById(R.id.actual_url_textView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_imageView);
        }
    }
}