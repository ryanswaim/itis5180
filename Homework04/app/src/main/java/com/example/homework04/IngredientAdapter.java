package com.example.homework04;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    ArrayList<String> myData;
    private static int MAX_INGREDIENTS = 5;

    public IngredientAdapter(ArrayList<String> myData) {
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

        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_item, viewGroup, false);

        EditText editText = view.findViewById(R.id.ingredient_item_editText);

        //capture changes in the recycler view items' edit texts
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myData.set(index, s.toString());
            }
        });

        final FloatingActionButton addRemoveFab = view.findViewById(R.id.add_remove_indredient_fab);

        //set up the add/remove floating action button
        addRemoveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the item is the last one and not at the max number of ingredients
                if(index == getItemCount() - 1 && getItemCount() < MAX_INGREDIENTS) {
                    if (getItemCount() < MAX_INGREDIENTS) {
                        //change image of the button to the remove drawable
                        addRemoveFab.setImageResource(R.drawable.remove);
                        //add new item and update adapter
                        myData.add("");
                        IngredientAdapter.this.notifyDataSetChanged();
                    }
                } else {
                    //else it is not the last item in the list or it is at the max ingredients
                    //so it is a REMOVE button now
                    //remove the current item
                    myData.remove(index);
                    IngredientAdapter.this.notifyDataSetChanged();
                }
            }
        });

        //Log.d("demo", "onCreateViewHolder: " + index);
        //Log.d("demo", "onCreateViewHolder: " + getItemCount());

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        //ensure the text is correct
        viewHolder.editText.setText(myData.get(position));

        //if the position of this item is the last and not at the max ingredients
        if(position == getItemCount() - 1 && getItemCount() < MAX_INGREDIENTS) {
            //ensure the drawable is ADD
            viewHolder.addRemoveFab.setImageResource(R.drawable.add);
        } else {
            //else it is not the last item or it is at the max ingredients
            //ensure the drawable is REMOVE
            viewHolder.addRemoveFab.setImageResource(R.drawable.remove);
        }
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText editText;
        FloatingActionButton addRemoveFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = itemView.findViewById(R.id.ingredient_item_editText);
            addRemoveFab = itemView.findViewById(R.id.add_remove_indredient_fab);
        }
    }
}