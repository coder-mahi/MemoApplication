package com.example.internship.myapplication;

//import android.content.Intent;
//import android.os.Bundle;
//
//import com.google.android.material.snackbar.Snackbar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.view.View;
//
//import androidx.core.view.WindowCompat;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.example.internship.myapplication.databinding.ActivityMainBinding;
//
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity {
//
//    private AppBarConfiguration appBarConfiguration;
//    private ActivityMainBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "New Note", Snackbar.LENGTH_LONG)
////                        .setAnchorView(R.id.fab)
////                        .setAction("Action", null).show();
//                Toast.makeText(getApplicationContext(),"New Note",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(),AddNoteActivity.class));
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;

    private RecyclerView recyclerViewNotes;
    private NoteDatabase db;
    private NoteAdapter adapter;
    private List<Note> noteList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        fab = findViewById(R.id.fab);

        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, "notes-database").build();

        loadNotes();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
    }

    private void loadNotes() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                noteList = db.noteDao().getAllNotes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new NoteAdapter(noteList, new NoteAdapter.OnNoteClickListener() {
                            @Override
                            public void onNoteClick(Note note) {
                                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                                intent.putExtra("noteId", note.id);
                                intent.putExtra("noteTitle", note.title);
                                intent.putExtra("noteDescription", note.description);
                                startActivityForResult(intent, EDIT_NOTE_REQUEST);
                            }
                        });
                        recyclerViewNotes.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_NOTE_REQUEST || requestCode == EDIT_NOTE_REQUEST) && resultCode == RESULT_OK) {
            loadNotes(); // Reload notes when returning from AddNoteActivity or EditNoteActivity
        }
    }
}