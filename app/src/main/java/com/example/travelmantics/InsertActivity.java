package com.example.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {
    private FirebaseDatabase   mFirebaseDatabase;
    private DatabaseReference  mDatabaseReference;
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
       mFirebaseDatabase = FirebaseDatabase.getInstance();
       mDatabaseReference = mFirebaseDatabase.getReference().child("TravelDeal");
       txtTitle = (EditText) findViewById(R.id.editText);
       txtPrice = (EditText) findViewById(R.id.editText2);
       txtDescription = (EditText) findViewById(R.id.editText3);


    }


      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          switch (item .getItemId()) {
              case R.id.save_menu :
                  savedeal();
                  Toast.makeText(this ,"Travel deals add",Toast.LENGTH_LONG).show();
                  clean();
                  return  true;
                  default: return super.onOptionsItemSelected(item);
          }



      }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return
                true;
    }
    private void savedeal() {
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String price = txtPrice.getText().toString();
        TravelDeal deal = new TravelDeal(title , description , price ,"");
        mDatabaseReference.push().setValue(deal);


    };
    private  void clean() {
        txtPrice.setText ("");
        txtTitle.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();
    };

}

