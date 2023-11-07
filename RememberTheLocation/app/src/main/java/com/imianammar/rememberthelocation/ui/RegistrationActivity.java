package com.imianammar.rememberthelocation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.imianammar.rememberthelocation.database.DatabaseHelper;
import com.imianammar.rememberthelocation.Model.User;
import com.imianammar.rememberthelocation.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextPassword;
    private Button buttonRegister;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set up OnClickListener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String name = editTextName.getText().toString();
                String address = editTextAddress.getText().toString();
                String password = editTextPassword.getText().toString();

                // Perform registration
                boolean registrationSuccessful = performRegistration(name, address, password);

                if (registrationSuccessful) {
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish(); // Finish this activity after successful registration
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean performRegistration(String name, String address, String password) {
        // Check if the username is already taken
        if (databaseHelper.isUsernameTaken(name)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return false; // Registration failed
        }

        // Create a new user and add to the database
        long result = databaseHelper.addUser(name, address, password);

        return result != -1; // Registration successful if user was added to the database
    }
}
