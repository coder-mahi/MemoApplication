package com.example.internship.myapplication;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")

public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;

    public Note() {
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }
}