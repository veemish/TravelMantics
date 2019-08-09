package com.example.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase   mFirebaseDatabase;
    private DatabaseReference  mDatabaseReference;
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    TravelDeal deal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
       txtTitle = (EditText) findViewById(R.id.editText);
       txtPrice = (EditText) findViewById(R.id.editText2);
       txtDescription = (EditText) findViewById(R.id.editText3);
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null) {
            deal = new TravelDeal();

        }

         this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
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
        return
                true;

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

}

