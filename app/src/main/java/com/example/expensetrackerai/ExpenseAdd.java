package com.example.expensetrackerai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseAdd extends AppCompatActivity {

    Button savebtn;
    EditText descriptionEditText;
    EditText amountEditText;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        savebtn = findViewById(R.id.Savebutton);
        descriptionEditText = findViewById(R.id.Description);
        amountEditText = findViewById(R.id.Amount);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(descriptionEditText.getText().toString().isEmpty() ||
                        amountEditText.getText().toString().isEmpty()) {
                    Toast.makeText(ExpenseAdd.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String desc = descriptionEditText.getText().toString().toLowerCase();
                int amount = Integer.parseInt(amountEditText.getText().toString());
                String category;

                if(desc.contains("pizza") || desc.contains("burger") || desc.contains("food") || desc.contains("milk")) {
                    category = "Food";
                }
                else if(desc.contains("bus") || desc.contains("auto") || desc.contains("train")) {
                    category = "Travel";
                }
                else {
                    category = "Other";
                }

                String userId = auth.getCurrentUser().getUid();
                String id = reference.push().getKey();

                ExpenseModel expense = new ExpenseModel(id, desc, category, amount, currentDate, currentTime);

                reference.child(userId).child("Expenses").child(id).setValue(expense)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(ExpenseAdd.this, "Expense Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ExpenseAdd.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(ExpenseAdd.this, MainActivity.class);
                        startActivity(intent);
                        finish();

            }
        });

    }
}