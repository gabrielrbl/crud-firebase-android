package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ListView list;
    private Button btnToCreate;
    private DatabaseReference dbRef;
    private List<Product> listProducts = new ArrayList<>();
    private ArrayAdapter<Product> arrayAdapterProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = findViewById(R.id.list_products);
        btnToCreate = findViewById(R.id.btn_list_to_create);
        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProducts.clear();

                for (DataSnapshot objSnapshot:snapshot.getChildren()) {
                    Product p = objSnapshot.getValue(Product.class);
                    listProducts.add(p);
                }

                arrayAdapterProducts = new ArrayAdapter<Product>(ListActivity.this, android.R.layout.simple_list_item_1, listProducts);
                list.setAdapter(arrayAdapterProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        btnToCreate.setOnClickListener(v -> startActivity(new Intent(ListActivity.this, CreateActivity.class)));

        list.setOnItemClickListener((parent, view, position, id) -> {
            Product productSelected = (Product) parent.getItemAtPosition(position);

            Intent intent = new Intent(ListActivity.this, UpdateDeleteActivity.class);
            intent.putExtra("uid", productSelected.getUid());
            startActivity(intent);
        });
    }
}