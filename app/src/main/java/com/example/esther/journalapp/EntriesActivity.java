package com.example.esther.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.esther.journalapp.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class EntriesActivity extends AppCompatActivity {

    private static final String TAG = EntriesActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabaseReference;
    private String mUserId;

    private ArrayList mEntriesArrayList = new ArrayList<Entry>();
    private ListView mEntryListView;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageTextView;
    private TextView mEmptyMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        // Initialize Firebase Auth and Database reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Check if the user has signed in and load his/her entries
        if (mFirebaseUser == null) {
            loadSignInView();
        } else {
            mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            mEntryListView = (ListView) findViewById(R.id.entry_list_view);
            final EntryAdapter adapter = new EntryAdapter(this, mEntriesArrayList);
            mUserId = mFirebaseUser.getUid();

            //set onclick listener on the list
            mEntryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Entry currentEntry = (Entry) mEntryListView.getItemAtPosition(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("title", currentEntry.getTitle());
                    bundle.putString("content", currentEntry.getContent());
                    bundle.putString("date", currentEntry.getDate());
                    bundle.putString("id", currentEntry.getEntryId());

                    Intent intent = new Intent(EntriesActivity.this, DetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mProgressBar.setVisibility(View.VISIBLE);
            Query entriesQuery = mDatabaseReference.child("users").child(mUserId).child("entries");
            entriesQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot entrySnapshot, @Nullable String s) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mEntriesArrayList.add(new Entry((String) entrySnapshot.child("title").getValue(),
                            (String) entrySnapshot.child("content").getValue(),
                            (String) entrySnapshot.child("date").getValue(),
                            entrySnapshot.getKey()));

                    adapter.notifyDataSetChanged();

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
                    mErrorMessageTextView.setVisibility(View.VISIBLE);

                }
            });

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            mEntryListView.setAdapter(adapter);
        }
    }


    /**
     * Start GoogleSignInActivity if the user has not logged in
     */
    private void loadSignInView() {
        Intent intent = new Intent(EntriesActivity.this, GoogleSignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Start SaveEntryActivity
     *
     */
    private void loadNewEntryView(){
        Intent intent = new Intent(EntriesActivity.this, SaveEntryActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(EntriesActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_add) {
            loadNewEntryView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
