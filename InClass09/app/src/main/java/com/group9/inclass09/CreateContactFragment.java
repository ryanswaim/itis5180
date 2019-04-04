package com.group9.inclass09;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

//InClass09
//Group 9
//Rockford Stoller
//Ryan Swaim

public class CreateContactFragment extends Fragment {

    public static int TOOK_PICTURE = 1;

    OnCreateContactFragmentInteractionListener mListener;

    private ImageView profilePictureImageView;
    private String downloadUrl = null;

    public CreateContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_contact, container, false);

        getActivity().setTitle("Create New Contact");

        profilePictureImageView = view.findViewById(R.id.profile_picture_imageView);

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, TOOK_PICTURE);
            }
        });

        final EditText nameEditText = view.findViewById(R.id.name_on_create_contact_editText);
        final EditText emailEditText = view.findViewById(R.id.email_on_create_contact_editText);
        final EditText phoneEditText = view.findViewById(R.id.phone_on_create_contact_editText);

        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEditText.length() > 0 && emailEditText.length() > 0 && phoneEditText.length() > 0) {
                    Log.d("demo", "in submit button");
                    Contact contact = new Contact();

                    contact.name = nameEditText.getText().toString();
                    contact.email = emailEditText.getText().toString();
                    contact.phoneNumber = phoneEditText.getText().toString();
                    contact.imageUrl = downloadUrl;

                    mListener.addContact(contact);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == TOOK_PICTURE) {
            if(data.getExtras() != null) {
                Bitmap bitmap = data.getExtras().getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] byteData = baos.toByteArray();

                MainActivity mainActivityContext = (MainActivity) getContext();

                final StorageReference imageRef = mainActivityContext.storageRef.child("images/" + bitmap.toString());
                UploadTask uploadTask = imageRef.putBytes(byteData);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("demo", "onFailure: " + byteData);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        Log.d("demo", "onSuccess: " + imageRef.getDownloadUrl());
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Log.d("demo", "onSuccess: " + downloadUrl);
                                Picasso.get().load(downloadUrl).placeholder(R.drawable.loading).into(profilePictureImageView);
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateContactFragment.OnCreateContactFragmentInteractionListener) {
            mListener = (CreateContactFragment.OnCreateContactFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateContactFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCreateContactFragmentInteractionListener {
        // TODO: Update argument type and name
        void addContact(Contact contact);
    }
}
