package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateDeleteActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editValue;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnCancel;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        editName = findViewById(R.id.edit_name);
        editValue = findViewById(R.id.edit_value);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnCancel = findViewById(R.id.btn_edit_cancel);
        dbRef = FirebaseDatabase.getInstance().getReference();

        String productUid = getIntent().getStringExtra("uid");
        DatabaseReference products = dbRef.child("products").child(productUid);

        products.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    editName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    editValue.setText(Objects.requireNonNull(snapshot.child("value").getValue()).toString());
                } else {
                    Toast.makeText(UpdateDeleteActivity.this, "Não foi possível carregar o produto.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnEdit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editName.getText().toString())) {
                Toast.makeText(UpdateDeleteActivity.this, "Informe o nome do produto!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(editValue.getText().toString())) {
                Toast.makeText(UpdateDeleteActivity.this, "Informe o valor do produto!", Toast.LENGTH_SHORT).show();
            } else {
                Product p = new Product();
                p.setUid(productUid);
                p.setName(editName.getText().toString());
                p.setValue(editValue.getText().toString());

                products.setValue(p);

                clearInputs();
                startActivity(new Intent(UpdateDeleteActivity.this, ListActivity.class));
            }
        });

        btnDelete.setOnClickListener(v -> {
            products.removeValue();
            clearInputs();
            startActivity(new Intent(UpdateDeleteActivity.this, ListActivity.class));
        });

        btnCancel.setOnClickListener(v -> {
            clearInputs();
            startActivity(new Intent(UpdateDeleteActivity.this, ListActivity.class));
        });
    }

    private void clearInputs() {
        editName.setText("");
        editValue.setText("");
    }
}