package com.example.internship.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.room.*;

public class AddNoteActivity extends AppCompatActivity {
AppCompatImageButton back,share,menu,style;
EditText editTextDescription,editTextTitle;
Button savebtn;
    private NoteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        back =findViewById(R.id.back_btn);
        share =findViewById(R.id.share_btn);
        style =findViewById(R.id.style_btn);
        menu =findViewById(R.id.menu_btn);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        savebtn = findViewById(R.id.save_btn);
        db = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, "notes-database").build();

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        share.setOnClickListener(v -> shareClicked());
        style.setOnClickListener(v -> styleClicked());
        menu.setOnClickListener(v -> menuClicked());
        savebtn.setOnClickListener(v -> saveClicked());
    }
    private void saveClicked(){
        final String title = editTextTitle.getText().toString();
        final String description = editTextDescription.getText().toString();
        if (!title.isEmpty() && !description.isEmpty()) {
            final Note note = new Note(title, description);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    db.noteDao().insert(note);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish(); // Close the activity and return to MainActivity
                        }
                    });
                }
            });
        }
    }
    private void shareClicked(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (!title.isEmpty() || !description.isEmpty()) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Title: " + title + "\n\nDescription: " + description);
            shareIntent.setType("text/plain");

            Intent chooser = Intent.createChooser(shareIntent, "Share note via");
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }
        }
    }
    private void styleClicked(){
        final String[] styles = {"Bold", "Italic", "Underline"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Text Style")
                .setItems(styles, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            applyStyleToText(android.graphics.Typeface.BOLD);
                            break;
                        case 1:
                            applyStyleToText(android.graphics.Typeface.ITALIC);
                            break;
                        case 2:
                            applyUnderlineToText();
                            break;
                    }
                });
        builder.create().show();
    }
    private void applyStyleToText(int style) {
        int start = editTextDescription.getSelectionStart();
        int end = editTextDescription.getSelectionEnd();

        Spannable spannableText = editTextDescription.getText();
        spannableText.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void applyUnderlineToText() {
        int start = editTextDescription.getSelectionStart();
        int end = editTextDescription.getSelectionEnd();

        Spannable spannableText = editTextDescription.getText();
        spannableText.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void menuClicked(){

    }
}