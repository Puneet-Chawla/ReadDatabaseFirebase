package com.example.student.readdattabase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewActivity extends AppCompatActivity {
    private static final String TAG = "ViewActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userID;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mListView=(ListView)findViewById(R.id.listview);

        mAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        FirebaseUser user=mAuth.getCurrentUser();
        userID=user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextMessage("Successfully Signed In");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    TextMessage("Successfully Signed Out");
                }
                // ...
            }
        };

        //called whenever their is change made to database called as soon as activity begins
myRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
// method below reads the data
        showData(dataSnapshot);
        //dataSnapshot creates the snapshot of the database
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
    }
    public void TextMessage(String text)
    {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void showData(DataSnapshot dataSnapshot)
    {
        // iterate through all the users
        for(DataSnapshot ds:dataSnapshot.getChildren())
        {
            userInfo uInfo=new userInfo();
            uInfo.setName(ds.child(userID).getValue(userInfo.class).getName());
            uInfo.setEmail(ds.child(userID).getValue(userInfo.class).getEmail());
            uInfo.setPhno(ds.child(userID).getValue(userInfo.class).getPhno());
        }
    }
}
