package com.imianammar.rememberthelocation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imianammar.rememberthelocation.Adapter.NotesAdapter;
import com.imianammar.rememberthelocation.database.DatabaseHelper;
import com.imianammar.rememberthelocation.Model.Note;
import com.imianammar.rememberthelocation.R;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;
    private Button addNote;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // Initialize UI elements
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        addNote = findViewById(R.id.addNote);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve list of notes from the database
        notesList = databaseHelper.getAllNotes();

        // Set up RecyclerView and adapter
        notesAdapter = new NotesAdapter(notesList);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(notesAdapter);

        // Set up click listener for editing notes
        notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Open EditNoteActivity for editing the selected note
                Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
                intent.putExtra("note_id", notesList.get(position).getId());
                startActivity(intent);
            }
        });

        // Set up long click listener for deleting notes
        notesAdapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                // Delete the selected note
                databaseHelper.deleteNoteById(notesList.get(position).getId());
                Toast.makeText(NoteListActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

                // Update the list of notes and refresh the RecyclerView
                notesList = databaseHelper.getAllNotes();
                notesAdapter.setNotesList(notesList);
            }
        });
    }
}
