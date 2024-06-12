package com.example.internship.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private RecyclerView recyclerViewNotes;
    private NoteDatabase db;
    private NoteAdapter adapter;
    private List<Note> noteList;
    private FloatingActionButton fab;
    private AppCompatImageButton settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        fab = findViewById(R.id.fab);
        settingBtn = findViewById(R.id.setting_btn);

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
        settingBtn.setOnClickListener(view -> settingClicked());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home_menu) {
                    showToast("Home Menu..!");
                } else if (id == R.id.textsize_menu) {
                    showToast("TextS ize Menu..!");
                } else if (id == R.id.fontstyle_menu) {
                    showToast("Font Style Menu..!");
                } else if (id == R.id.share_menu) {
                    showToast("Share Menu..!");
                } else if (id == R.id.follow_menu) {
                    showToast("Follow Us Menu..!");
                } else if (id == R.id.privacy_menu) {
                    showToast("Privacy Menu..!");
                } else if (id == R.id.about_menu) {
                    showToast("About Us Menu..!");
                }
                return true;
            }
        });
        //CLOSE ONCE CLICK ON ITEM
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    private void settingClicked() {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        showToast("Setting Activity");
    }
    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}