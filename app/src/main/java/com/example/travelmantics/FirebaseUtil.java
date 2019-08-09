package com.example.travelmantics;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static  FirebaseDatabase       mFirebaseDatabase;
    public static  DatabaseReference       mDatabaseReference;
    private  static   FirebaseUtil       firebaseUtil;
    public static FirebaseAuth         mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener  mAuthListener;
    public static  ArrayList<TravelDeal>  mDeals;

    private FirebaseUtil(){}

    public static void openFbReference(String ref){
        if (firebaseUtil==null){
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                }
            };


        }
            mDeals = new ArrayList();
            mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }
    public static  void detachListener(){
         mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
