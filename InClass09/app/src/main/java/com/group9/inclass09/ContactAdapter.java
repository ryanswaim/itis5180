package com.group9.inclass09;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//InClass09
//Group 9
//Rockford Stoller
//Ryan Swaim

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    ArrayList<Contact> myData;

    public ContactAdapter(ArrayList<Contact> myData) {
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

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);

        //set background to a border design
        view.setBackground(view.getContext().getApplicationContext().getDrawable(R.drawable.border));

        //get all inner views
        TextView nameTextView = view.findViewById(R.id.name_in_item_list_textView);
        TextView phoneTextView = view.findViewById(R.id.phone_in_item_list_textView);
        TextView emailTextView = view.findViewById(R.id.email_in_item_list_textView);
        ImageView profilePictureImageView = view.findViewById(R.id.profile_picture_in_item_list_imageButton);

        //get correct contact data
        final Contact contact = myData.get(index);

        //set text view text with contact data
        nameTextView.setText(contact.name);
        phoneTextView.setText(contact.phoneNumber);
        emailTextView.setText(contact.email);

        if(contact.imageUrl == null) {
            profilePictureImageView.setImageResource(R.drawable.default_photo);
        } else {
            Picasso.get().load(contact.imageUrl).placeholder(R.drawable.loading).into(profilePictureImageView);
        }

        //set item click listener
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myData.remove(index);
                notifyDataSetChanged();
                MainActivity mainActivity = (MainActivity) view.getContext();
                mainActivity.myRef.setValue(myData);
                return false;
            }
        });

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        //ensure all text displayed is correct for this contact
        final Contact contact = myData.get(position);
        viewHolder.nameTextView.setText(contact.name);
        viewHolder.phoneTextView.setText(contact.phoneNumber);
        viewHolder.emailTextView.setText(contact.email);

        if(contact.imageUrl == null) {
            viewHolder.profilePictureImageView.setImageResource(R.drawable.default_photo);
        } else {
            Picasso.get().load(contact.imageUrl).placeholder(R.drawable.loading).into(viewHolder.profilePictureImageView);
        }

        //set item click listener
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myData.remove(position);
                notifyDataSetChanged();
                MainActivity mainActivity = (MainActivity) viewHolder.nameTextView.getContext();
                mainActivity.myRef.setValue(myData);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, phoneTextView, emailTextView;
        ImageView profilePictureImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_in_item_list_textView);
            phoneTextView = itemView.findViewById(R.id.phone_in_item_list_textView);
            emailTextView = itemView.findViewById(R.id.email_in_item_list_textView);
            profilePictureImageView = itemView.findViewById(R.id.profile_picture_in_item_list_imageButton);
        }
    }
}