package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase   mFirebaseDatabase;
    private DatabaseReference  mDatabaseReference;
    private static final int PICTURE_RESULT = 42;    //THE ANSWER TO EVERYTHING//
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    TravelDeal deal;
    ImageView  imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
       txtTitle = (EditText) findViewById(R.id.editText);
       txtPrice = (EditText) findViewById(R.id.editText2);
       txtDescription = (EditText) findViewById(R.id.editText3);
       imageView = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null) {
            deal = new TravelDeal();

        }

         this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
          showImage(deal.getImageUrl());
        Button btn_img = findViewById(R.id.btn_img);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY , true);
                startActivityForResult(intent.createChooser(intent ,  "Insert Picture" ), PICTURE_RESULT);

            }
        });


    }


      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          switch (item .getItemId()) {
              case R.id.save_menu :
                  saveDeal();
                  Toast.makeText(this ,"Travel deals add",Toast.LENGTH_LONG).show();
                  clean();
                  backToList();
                  return  true;
              case R.id.delete_menu:
                  deleteDeal();
                  Toast.makeText(this , "Deal Delete" , Toast.LENGTH_LONG).show();
                  backToList();
                  return true;
                  default: return super.onOptionsItemSelected(item);

          }


      }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if (FirebaseUtil.isAdmin == true){
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditText(true);

        }
        else  {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditText(false);
        }
        return
                true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT &&  resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();

            StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());

            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            String url = task.getResult().toString();
                            deal.setImageUrl(url);
                            deal.setImageName(taskSnapshot.getStorage().getPath());
                           showImage(url);



                        }
                    });

                }
            });

        }


    }

    private void showImage(String url){
      if (url != null &&  url.isEmpty() == false)  {

          int width = Resources.getSystem().getDisplayMetrics().widthPixels;
          Picasso.get().load(url).resize(width,width*2/3).centerCrop().into(imageView);
      }

    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if (deal.getId() == null) {
            mDatabaseReference.push().setValue(deal);

        }
        else if (deal.getId()!= null) {
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }
    }

    private void deleteDeal() {
        if (deal == null) {
            Toast.makeText(this, "Save before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();
    }

    private void backToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


    private  void clean() {
        txtPrice.setText ("");
        txtTitle.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();
    }

    private void  enableEditText(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
    }

}

