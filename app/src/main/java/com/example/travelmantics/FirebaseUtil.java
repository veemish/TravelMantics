package com.example.travelmantics;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static  FirebaseDatabase       mFirebaseDatabase;
    public static  DatabaseReference       mDatabaseReference;
    private  static   FirebaseUtil       firebaseUtil;
    public static FirebaseAuth         mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener  mAuthListener;
    public static  ArrayList<TravelDeal>  mDeals;
    private static final int RC_SIGN_IN = 123;
    private  static  ListActivity caller;

    private FirebaseUtil(){}
    public static boolean  isAdmin;

    public static void openFbReference(String ref, final ListActivity callerActivity ){
        if (firebaseUtil==null){
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()== null ){
                        FirebaseUtil.singIn();
                    }
                      else {
                          String userId = firebaseAuth.getUid();
                          checkAdmin(userId);

                    }
                                                                                    {
                        Toast.makeText(callerActivity.getBaseContext(), "Welcome Back!" , Toast.LENGTH_LONG).show();

                    }

                }
            };


        }
            mDeals = new ArrayList();
            mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    private static void singIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    private static void checkAdmin( String uid) {
        firebaseUtil.isAdmin = false;
        DatabaseReference ref= mFirebaseDatabase.getReference().child("Admin").child(uid);
        ChildEventListener  listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);

    }

    public static void attachListener() {

        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }
    public static  void detachListener(){

        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
