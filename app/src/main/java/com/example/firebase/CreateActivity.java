package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateActivity extends AppCompatActivity {
    private EditText createName;
    private EditText createValue;
    private Button btnCreate;
    private Button btnCancel;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        createName = findViewById(R.id.create_name);
        createValue = findViewById(R.id.create_value);
        btnCreate = findViewById(R.id.btn_create);
        btnCancel = findViewById(R.id.btn_create_cancel);
        dbRef = FirebaseDatabase.getInstance().getReference();

        btnCreate.setOnClickListener(v -> {
            try {
                if (TextUtils.isEmpty(createName.getText().toString())) {
                    Toast.makeText(CreateActivity.this, "Informe o nome do produto!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(createValue.getText().toString())) {
                    Toast.makeText(CreateActivity.this, "Informe o valor do produto!", Toast.LENGTH_SHORT).show();
                } else {
                    Product p = new Product();
                    p.setUid(UUID.randomUUID().toString());
                    p.setName(createName.getText().toString());
                    p.setValue(createValue.getText().toString());

                    dbRef.child("products").child(p.getUid()).setValue(p);

                    clearInputs();
                    startActivity(new Intent(CreateActivity.this, ListActivity.class));
                }
            } catch (Exception e) {
                Toast.makeText(CreateActivity.this, "Não foi possível cadastrar o produto.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            clearInputs();
            startActivity(new Intent(CreateActivity.this, ListActivity.class));
        });
    }

    private void clearInputs() {
        createName.setText("");
        createValue.setText("");
    }
}