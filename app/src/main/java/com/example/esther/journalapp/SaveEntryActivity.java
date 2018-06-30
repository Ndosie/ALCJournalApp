package com.example.esther.journalapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esther.journalapp.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SaveEntryActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mContentEditText;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private String mUserId;
    private String mEntryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_entry);

        //initializing text fields
        mTitleEditText = (EditText) findViewById(R.id.title_edit_text);
        mContentEditText = (EditText) findViewById(R.id.content_edit_text);

        //checking if there are entra intent values
        Intent intentExtras = getIntent();
        mEntryId = intentExtras.getStringExtra("id");
        if(!TextUtils.isEmpty(mEntryId)){
            mTitleEditText.setText(intentExtras.getStringExtra("title"));
            mContentEditText.setText(intentExtras.getStringExtra("content"));
        }

        // Initialize Firebase Auth, Database reference and user id
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUserId = mFirebaseUser.getUid();

    }

    /**
     * Adds new user entry
     *
     */
    private void saveEntry(){
        //get current date and time
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String date = simpledateformat.format(calender.getTime());
        String title = mTitleEditText.getText().toString().trim();
        String content = mContentEditText.getText().toString().trim();

        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content)){
           showError(getString(R.string.save_error_message));
        }

        Entry entry = new Entry(title, content, date);
        if(!TextUtils.isEmpty(mEntryId)){
            DatabaseReference entryRef = mDatabaseReference.child("users")
                                                            .child(mUserId)
                                                            .child("entries")
                                                            .child(mEntryId);
            Map<String,Object> entryMap = new HashMap<String,Object>();
            entryMap.put("title", title);
            entryMap.put("content", content);
            entryMap.put("date", date);
            entryRef.updateChildren(entryMap);
            Toast.makeText(this, getString(R.string.update_success_message), Toast.LENGTH_SHORT).show();

        }else {
            mDatabaseReference.child("users")
                    .child(mUserId)
                    .child("entries")
                    .push()
                    .setValue(entry);
            Toast.makeText(this, getString(R.string.save_success_message), Toast.LENGTH_SHORT).show();
        }

        mTitleEditText.setText("");
        mContentEditText.setText("");

        //open EntriesActivity
        Intent intent = new Intent(SaveEntryActivity.this, EntriesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     *Shows error dialog
     *
     * @param message a message to be displayed
     */
    private void showError(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveEntryActivity.this);
        builder.setMessage(message)
                .setTitle(R.string.error_title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Menu inflation Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SaveEntryActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_save) {
            saveEntry();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
