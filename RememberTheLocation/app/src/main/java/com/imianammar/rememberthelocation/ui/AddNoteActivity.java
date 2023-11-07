package com.imianammar.rememberthelocation.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.imianammar.rememberthelocation.database.DatabaseHelper;
import com.imianammar.rememberthelocation.Model.Note;
import com.imianammar.rememberthelocation.R;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextLocationName;
    private EditText editTextDescription;
    private ImageView imageViewNoteImage;
    private Button buttonCaptureImage;
    private Button buttonSelectImage;
    private Button buttonAddNote;

    private DatabaseHelper databaseHelper;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;

    private Bitmap capturedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Initialize UI elements
        editTextLocationName = findViewById(R.id.editTextLocationName);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageViewNoteImage = findViewById(R.id.imageViewNoteImage);
        buttonCaptureImage = findViewById(R.id.buttonCaptureImage);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonAddNote = findViewById(R.id.buttonAddNote);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set up OnClickListener for capturing an image
        buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Set up OnClickListener for selecting an image
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchSelectPictureIntent();
            }
        });

        // Set up OnClickListener for adding a note
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String locationName = editTextLocationName.getText().toString();
                String description = editTextDescription.getText().toString();

                // Perform adding note
                boolean addNoteSuccessful = performAddNote(locationName, description);

                if (addNoteSuccessful) {
                    Toast.makeText(AddNoteActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish this activity after successful note addition
                } else {
                    Toast.makeText(AddNoteActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean performAddNote(String locationName, String description) {
        // Check if location name and description are provided
        if (locationName.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please provide location name and description", Toast.LENGTH_SHORT).show();
            return false; // Note addition failed
        }

        // Create a new note and add to the database
        Note note = new Note();
        note.setLocationName(locationName);
        note.setDescription(description);

        if (capturedImageBitmap != null) {
            // Save the image if available
            // Note: In a real application, you would save the image file and store the file path in the database
            note.setImagePath("path_to_saved_image"); // Replace with actual file path
        }

        long result = databaseHelper.addNote(locationName, description, note.getImagePath());

        return result != -1; // Note addition successful if note was added to the database
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectPictureIntent() {
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPictureIntent, REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                capturedImageBitmap = (Bitmap) extras.get("data");
                imageViewNoteImage.setImageBitmap(capturedImageBitmap);
            } else if (requestCode == REQUEST_IMAGE_SELECT) {
                // Handle selected image
            }
        }
    }
}
