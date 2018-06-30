package com.example.esther.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mDateTextView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    private String mEntryId;
    private String mTitle;
    private String mContent;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize Firebase Auth, Database reference and user id
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUserId = mFirebaseUser.getUid();

        //get text views references
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mContentTextView = (TextView) findViewById(R.id.content_text_view);
        mDateTextView = (TextView) findViewById(R.id.date_text_view);

        //Getting passed data
        Intent intentExtras = getIntent();
        String date = intentExtras.getStringExtra("date");
        mTitle = intentExtras.getStringExtra("title");
        mContent = intentExtras.getStringExtra("content");
        mEntryId = intentExtras.getStringExtra("id");

        //set texts
        mTitleTextView.setText(mTitle);
        mContentTextView.setText(mContent);
        mDateTextView.setText(date);
    }

    /**
     * Opens Save entry with user entry values
     */
    private void openSaveEntry(){
        Bundle bundle = new Bundle();
        bundle.putString("title", mTitle);
        bundle.putString("content", mContent);
        bundle.putString("id", mEntryId);

        Intent intent = new Intent(DetailActivity.this, SaveEntryActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Deletes users entry
     *
     */
    private void deleteEntry(){
        mDatabaseReference.child("users")
                .child(mUserId)
                .child("entries")
                .child(mEntryId)
                .removeValue();
        Toast.makeText(this, getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show();

        //open EntriesActivity
        Intent intent = new Intent(DetailActivity.this, EntriesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DetailActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_edit) {
            openSaveEntry();
            return true;
        }

        if (id == R.id.action_delete) {
            deleteEntry();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
