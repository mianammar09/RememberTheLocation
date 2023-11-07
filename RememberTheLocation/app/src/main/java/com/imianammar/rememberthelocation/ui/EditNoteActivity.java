package com.imianammar.rememberthelocation.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.imianammar.rememberthelocation.R;
import com.imianammar.rememberthelocation.database.DatabaseHelper;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editTextLocationName;
    private EditText editTextDescription;
    private ImageView imageViewNoteImage;
    private Button buttonSaveChanges;

    private DatabaseHelper databaseHelper;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Initialize UI elements
        editTextLocationName = findViewById(R.id.editTextLocationName);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageViewNoteImage = findViewById(R.id.imageViewNoteImage);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get the note details and ID from intent
        String locationName = getIntent().getStringExtra("location_name"); // Replace with actual key
        String description = getIntent().getStringExtra("description"); // Replace with actual key
        noteId = getIntent().getIntExtra("note_id", -1); // Replace with actual key and default value

        // Set the UI elements with the retrieved data
        editTextLocationName.setText(locationName);
        editTextDescription.setText(description);

        // Load the image using a library like Picasso or Glide if available

        // Set up OnClickListener for saving changes
        buttonSaveChanges.setOnClickListener(v -> {
            String newLocationName = editTextLocationName.getText().toString();
            String newDescription = editTextDescription.getText().toString();

            // Perform update logic in your DatabaseHelper class
            boolean updateSuccessful = databaseHelper.updateNote(noteId, newLocationName, newDescription, null);

            if (updateSuccessful) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Finish this activity after successful note update
            } else {
                Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
