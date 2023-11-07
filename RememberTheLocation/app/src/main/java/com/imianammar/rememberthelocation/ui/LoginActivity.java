package com.imianammar.rememberthelocation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.imianammar.rememberthelocation.database.DatabaseHelper;
import com.imianammar.rememberthelocation.Model.User;
import com.imianammar.rememberthelocation.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView newAccount;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        newAccount = findViewById(R.id.textViewRegister);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set up OnClickListener for the login button
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Perform login
                boolean loginSuccessful = performLogin(username, password);

                if (loginSuccessful) {
                    // Redirect to NoteListActivity upon successful login
                    Intent intent = new Intent(LoginActivity.this, NoteListActivity.class);
                    startActivity(intent);
                    finish(); // Finish this activity so the user can't go back to it after logging in
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean performLogin(String username, String password) {
        // Check if username and password match a user in the database
        User user = databaseHelper.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            // User found, login successful
            // Implement session management (e.g., store user ID or username in SharedPreferences)
            return true;
        }

        return false; // Login failed
    }
}