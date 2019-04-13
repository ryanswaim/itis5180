package com.group9.inclass10;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

//In-Class10
//Group 9
//Rockford Stoller

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<Note> myData;

    MainActivity mainActivity;

    public NoteAdapter(ArrayList<Note> myData) {
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

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);

        //set background to a border design NOT USED ANYMORE SET IN LAYOUT FILE!!!!!
        //view.setBackground(view.getContext().getApplicationContext().getDrawable(R.drawable.border));

        //get all inner views
        TextView messageTextView = view.findViewById(R.id.message_text_in_item_list_textView);
        ImageView deleteImageViewButton = view.findViewById(R.id.delete_message_imageView_button);

        //get correct note data
        final Note note = myData.get(index);

        //set text view text with note data
        messageTextView.setText(note.noteText);
        messageTextView.setSingleLine(true);

        //get MainActivity instance
        mainActivity = (MainActivity) view.getContext();

        //set item on click listener
        //region
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToDisplayNoteFragment(note);
            }
        });
        //endregion

        //set delete image view click listener
        deleteImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete async
                //region
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId=" + note.noteId)
                        .header("x-access-token", mainActivity.userToken)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        Log.d("demo", "onFailure: get name failed");
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            String json = responseBody.string();
                            JSONObject root = new JSONObject(json);

                            Log.d("demo", "JSONObject: " + root.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                myData.remove(index);
                notifyDataSetChanged();
                //endregion
            }
        });

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        //ensure all text displayed is correct for this note
        final Note note = myData.get(position);
        viewHolder.messageTextView.setText(note.noteText);
        viewHolder.messageTextView.setSingleLine(true);

        //get MainActivity instance
        mainActivity = (MainActivity) viewHolder.deleteImageViewButton.getContext();

        //set item on click listener
        //region
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToDisplayNoteFragment(note);
            }
        });
        //endregion

        //set delete image view click listener
        viewHolder.deleteImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete async
                //region
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId=" + note.noteId)
                        .header("x-access-token", mainActivity.userToken)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        Log.d("demo", "onFailure: get name failed");
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            String json = responseBody.string();
                            JSONObject root = new JSONObject(json);

                            Log.d("demo", "JSONObject: " + root.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                myData.remove(position);
                notifyDataSetChanged();
                //endregion
            }
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        ImageView deleteImageViewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTextView = itemView.findViewById(R.id.message_text_in_item_list_textView);
            deleteImageViewButton = itemView.findViewById(R.id.delete_message_imageView_button);
        }
    }
}