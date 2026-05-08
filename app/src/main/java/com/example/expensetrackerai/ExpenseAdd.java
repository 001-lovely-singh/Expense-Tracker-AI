package com.example.expensetrackerai;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseAdd extends AppCompatActivity {

    EditText etAmount, etNote;
    TextView tvDateTime;

    // 🔥 category views
    LinearLayout catFood, catTravel, catShopping, catBills, catOther;

    String selectedCategory = "Food"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // 🔹 find views
        etAmount = findViewById(R.id.etAmount);
        etNote = findViewById(R.id.etNote);
        tvDateTime = findViewById(R.id.tvDateTime);

        // 🔥 category find
        catFood = findViewById(R.id.catFood);
        catTravel = findViewById(R.id.catTravel);
        catShopping = findViewById(R.id.catShopping);
        catBills = findViewById(R.id.catBills);
        catOther = findViewById(R.id.catOther);

        // 🔥 category click setup
        setCategory(catFood, "Food");
        setCategory(catTravel, "Travel");
        setCategory(catShopping, "Shopping");
        setCategory(catBills, "Bills");
        setCategory(catOther, "Other");

        // ✅ default select
        catFood.performClick();

        // 🔥 AUTO DATE + TIME
        String currentDate = new SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()).format(new Date());

        String currentTime = new SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()).format(new Date());

        tvDateTime.setText(currentDate + ", " + currentTime);

        // 🔹 SAVE BUTTON
        findViewById(R.id.btnSave).setOnClickListener(v -> {

            String amountStr = etAmount.getText().toString();
            String note = etNote.getText().toString();

            // ❌ validation
            if (TextUtils.isEmpty(amountStr)) {
                etAmount.setError("Enter amount");
                return;
            }

            int amount = Integer.parseInt(amountStr);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String id = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userId)
                    .child("Expenses")
                    .push()
                    .getKey();

            // 🔥 MODEL
            ExpenseModel model = new ExpenseModel(
                    id,
                    note,
                    selectedCategory,
                    amount,
                    currentDate,
                    currentTime);

            // 🔥 FIREBASE SAVE
            FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userId)
                    .child("Expenses")
                    .child(id)
                    .setValue(model)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ExpenseAdd.this, "Expense Added ✅", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ExpenseAdd.this, "Error ❌", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // 🔥 reset all categories
    private void resetCategories() {
        catFood.setBackgroundResource(R.drawable.category_default);
        catTravel.setBackgroundResource(R.drawable.category_default);
        catShopping.setBackgroundResource(R.drawable.category_default);
        catBills.setBackgroundResource(R.drawable.category_default);
        catOther.setBackgroundResource(R.drawable.category_default);
    }

    // 🔥 handle selection
    private void setCategory(LinearLayout view, String categoryName) {
        view.setOnClickListener(v -> {
            selectedCategory = categoryName;

            // sabko normal karo
            resetCategories();

            // selected highlight
            view.setBackgroundResource(R.drawable.category_selected);
        });
    }

}