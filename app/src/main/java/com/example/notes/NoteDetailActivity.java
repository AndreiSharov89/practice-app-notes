package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.OnReceiveContentListener;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NoteDetailActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText editTitle, editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        databaseHelper = new DatabaseHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);

        Intent intent = getIntent();
        int noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {
            editTitle.setText(intent.getStringExtra("noteTitle"));
            editContent.setText(intent.getStringExtra("noteContent"));
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> saveNote(noteId));
    }

    private void saveNote(int noteId) {
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Название и содержимое не могут быть пусты", Toast.LENGTH_SHORT).show();
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
        String timestamp = now.format(formatter);

        if (noteId == -1) {
            databaseHelper.insertNote(title, content, timestamp);
        } else {
            databaseHelper.updateNote(noteId, title, content, timestamp);
        }

        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NoteDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
