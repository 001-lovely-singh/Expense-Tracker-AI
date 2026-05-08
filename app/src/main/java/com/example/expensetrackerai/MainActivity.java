package com.example.expensetrackerai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    FloatingActionButton addbtn;

    RecyclerView recyclerView, aiRecycler;

    ArrayList<AIModel> aiList;
    AIAdapter aiAdapter;

    ArrayList<ExpenseModel> list;
    ExpenseAdapter adapter;

    DatabaseReference reference;

    TextView tvTotal;

    // Bottom Navigation
    LinearLayout navHome, navAnalytics, navBudget, navProfile;

    ImageView imgHome, imgProfile;

    TextView txtHome, txtProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        // Views
        recyclerView = findViewById(R.id.expenseRecycler);
        aiRecycler = findViewById(R.id.aiRecycler);

        tvTotal = findViewById(R.id.tvTotal);

        addbtn = findViewById(R.id.Addbutton);

        // Bottom Nav
        navHome = findViewById(R.id.navHome);
        navAnalytics = findViewById(R.id.navAnalytics);
        navBudget = findViewById(R.id.navBudget);
        navProfile = findViewById(R.id.navProfile);

        imgHome = findViewById(R.id.imgHome);
        imgProfile = findViewById(R.id.imgProfile);

        txtHome = findViewById(R.id.txtHome);
        txtProfile = findViewById(R.id.txtProfile);

        // HOME ACTIVE
        imgHome.setColorFilter(getResources().getColor(R.color.purple_500));
        txtHome.setTextColor(getResources().getColor(R.color.purple_500));

        imgProfile.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        txtProfile.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // AI Recycler
        LinearLayoutManager aiLayout =
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false);

        aiRecycler.setLayoutManager(aiLayout);
        aiRecycler.setNestedScrollingEnabled(false);

        aiList = new ArrayList<>();
        aiAdapter = new AIAdapter(aiList);

        aiRecycler.setAdapter(aiAdapter);

        // Expense Recycler
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new ExpenseAdapter(list);

        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        aiRecycler.setItemAnimator(new DefaultItemAnimator());

        // Firebase
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {

            startActivity(
                    new Intent(MainActivity.this,
                            login.class));

            finish();
            return;
        }

        reference = FirebaseDatabase.getInstance()
                .getReference("Users");

        FirebaseUser user = auth.getCurrentUser();

        // Greeting
        TextView tvGreeting = findViewById(R.id.tvGreeting);

        if (user != null) {

            String name = user.getDisplayName();

            if (name != null) {

                tvGreeting.setText("Hi, " + name + " 👋");
            }
        }

        String userId = auth.getCurrentUser().getUid();

        // Firebase Data
        reference.child(userId)
                .child("Expenses")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        int total = 0;

                        int foodTotal = 0;
                        int travelTotal = 0;
                        int shoppingTotal = 0;
                        int billsTotal = 0;
                        int otherTotal = 0;

                        list.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            ExpenseModel model =
                                    data.getValue(ExpenseModel.class);

                            if (model == null)
                                continue;

                            list.add(model);

                            total += model.getAmount();

                            String category =
                                    model.getCategory()
                                            .trim()
                                            .toLowerCase();

                            if (category.equals("food")) {

                                foodTotal += model.getAmount();

                            } else if (category.equals("travel")) {

                                travelTotal += model.getAmount();

                            } else if (category.equals("shopping")) {

                                shoppingTotal += model.getAmount();

                            } else if (category.equals("bills")) {

                                billsTotal += model.getAmount();

                            } else {

                                otherTotal += model.getAmount();
                            }
                        }

                        adapter.notifyDataSetChanged();

                        tvTotal.setText("₹" + total);

                        // AI Cards
                        aiList.clear();

                        if (total == 0) {

                            aiList.add(
                                    new AIModel(
                                            "Start",
                                            "Start tracking expenses",
                                            "",
                                            R.drawable.ic_light
                                    )
                            );

                        } else // FOOD
                            if (foodTotal > total * 0.4) {

                                int percentfood = (foodTotal * 100) / total;

                                aiList.add(
                                        new AIModel(
                                                "Food",
                                                "You spend too much on Food",
                                                "+" + percentfood + "%",
                                                R.drawable.burger__1_
                                        )
                                );
                            }

// TRAVEL
                            else if (travelTotal > total * 0.3) {

                                int percenttravel = (travelTotal * 100) / total;

                                aiList.add(
                                        new AIModel(
                                                "Travel",
                                                "You spend too much on Travel",
                                                "+" + percenttravel + "%",
                                                R.drawable.airplane__1_
                                        )
                                );
                            }

// SHOPPING
                            else if (shoppingTotal > total * 0.35) {

                                int percentshopping = (shoppingTotal * 100) / total;

                                aiList.add(
                                        new AIModel(
                                                "Shopping",
                                                "Shopping expenses are high",
                                                "+" + percentshopping + "%",
                                                R.drawable.shopping_cart__1_
                                        )
                                );
                            }

// BILLS
                            else if (billsTotal > total * 0.5) {

                                int percentbills = (billsTotal * 100) / total;

                                aiList.add(
                                        new AIModel(
                                                "Bills",
                                                "Monthly bills are too high",
                                                "+" + percentbills + "%",
                                                R.drawable.bills_1
                                        )
                                );
                            }

// OTHER
                            else if (otherTotal > total * 0.25) {

                                int percentother = (otherTotal * 100) / total;

                                aiList.add(
                                        new AIModel(
                                                "Other",
                                                "Other expenses increasing",
                                                "+" + percentother + "%",
                                                R.drawable.others_1
                                        )
                                );
                            }

// BALANCED
                            else {

                                aiList.add(
                                        new AIModel(
                                                "Good",
                                                "Spending is balanced",
                                                "👍",
                                                R.drawable.ic_light
                                        )
                                );
                            }

                        // Extra cards
                        aiList.add(
                                new AIModel(
                                        "Trend",
                                        "Expenses increasing",
                                        "+18%",
                                        R.drawable.ic_trend
                                )
                        );

                        aiList.add(
                                new AIModel(
                                        "Tip",
                                        "Reduce shopping",
                                        "Save more",
                                        R.drawable.ic_light
                                )
                        );

                        aiAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        // Add Expense
        addbtn.setOnClickListener(view -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            ExpenseAdd.class);

            startActivity(intent);
        });

        // PROFILE CLICK
        navProfile.setOnClickListener(v -> {

            // Profile active
            imgProfile.setColorFilter(
                    getResources().getColor(R.color.purple_500));

            txtProfile.setTextColor(
                    getResources().getColor(R.color.purple_500));

            // Home inactive
            imgHome.setColorFilter(
                    getResources().getColor(android.R.color.darker_gray));

            txtHome.setTextColor(
                    getResources().getColor(android.R.color.darker_gray));

            Intent intent =
                    new Intent(MainActivity.this,
                            ProfileActivity.class);

            startActivity(intent);
            finish();
        });

    }
}