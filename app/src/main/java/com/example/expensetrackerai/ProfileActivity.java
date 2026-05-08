package com.example.expensetrackerai;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvTotalExpense;

    Button btnLogout;

    FirebaseAuth auth;

    // Bottom Nav
    LinearLayout navHome, navProfile;

    ImageView imgHome, imgProfile;

    TextView txtHome, txtProfile;

    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        // Firebase
        auth = FirebaseAuth.getInstance();

        // Views
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);

        btnLogout = findViewById(R.id.btnLogout);

        // Bottom Nav Views
        navHome = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);

        imgHome = findViewById(R.id.imgHome);
        imgProfile = findViewById(R.id.imgProfile);

        txtHome = findViewById(R.id.txtHome);
        txtProfile = findViewById(R.id.txtProfile);

        addButton = findViewById(R.id.Addbutton);

        // PROFILE ACTIVE
        imgProfile.setColorFilter(
                getResources().getColor(R.color.purple_500));

        txtProfile.setTextColor(
                getResources().getColor(R.color.purple_500));

        // HOME NORMAL
        imgHome.setColorFilter(
                getResources().getColor(android.R.color.darker_gray));

        txtHome.setTextColor(
                getResources().getColor(android.R.color.darker_gray));

        // Current User
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {

            // Name
            if (user.getDisplayName() != null) {

                tvName.setText(
                        "Hi, " +
                                user.getDisplayName() +
                                " 👋");

            } else {

                tvName.setText("Hi User 👋");
            }

            // Email
            tvEmail.setText(user.getEmail());

            // Total Expense
            FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(user.getUid())
                    .child("Expenses")
                    .addValueEventListener(
                            new ValueEventListener() {

                                @Override
                                public void onDataChange(
                                        @NonNull DataSnapshot snapshot) {

                                    int total = 0;

                                    for (DataSnapshot dataSnapshot :
                                            snapshot.getChildren()) {

                                        ExpenseModel model =
                                                dataSnapshot.getValue(
                                                        ExpenseModel.class);

                                        if (model != null) {

                                            total += model.getAmount();
                                        }
                                    }

                                    tvTotalExpense.setText("₹" + total);
                                }

                                @Override
                                public void onCancelled(
                                        @NonNull DatabaseError error) {

                                }
                            });
        }

        // HOME CLICK
        navHome.setOnClickListener(v -> {

            Intent intent =
                    new Intent(ProfileActivity.this,
                            MainActivity.class);

            startActivity(intent);

            finish();
        });

        // ADD BUTTON
        addButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(ProfileActivity.this,
                            ExpenseAdd.class);

            startActivity(intent);
        });

        // Logout
        // Logout
        btnLogout.setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Logout")
                    .setMessage("Do you want to logout?")
                    .setCancelable(true)

                    // YES BUTTON
                    .setPositiveButton("Yes", (dialog, which) -> {

                        auth.signOut();

                        Intent intent =
                                new Intent(ProfileActivity.this,
                                        login.class);

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);

                        finish();
                    })

                    // NO BUTTON
                    .setNegativeButton("No", (dialog, which) -> {

                        dialog.dismiss();
                    })

                    .show();
        });
    }
}