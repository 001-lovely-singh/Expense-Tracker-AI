package com.example.expensetrackerai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    Button addbtn;
    RecyclerView recyclerView;
    ArrayList<ExpenseModel> list;
    ExpenseAdapter adapter;
    DatabaseReference reference;
    TextView tvTotal, tvSuggestion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.expenseRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView = findViewById(R.id.expenseRecycler);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new ExpenseAdapter(list);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        tvTotal = findViewById(R.id.tvTotal);
        tvSuggestion = findViewById(R.id.tvSuggestion);


        if(auth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        reference.child(userId).child("Expenses")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        int total = 0;
                        int foodTotal = 0;
                        int travelTotal = 0;
                        int otherTotal = 0;

                        list.clear();

                        for(DataSnapshot data : snapshot.getChildren()){
                            ExpenseModel model = data.getValue(ExpenseModel.class);

                            if(model == null) continue;

                            list.add(model);

                            total += model.getAmount();

                            if("Food".equals(model.getCategory())){
                                foodTotal += model.getAmount();
                            }
                            else if("Travel".equals(model.getCategory())){
                                travelTotal += model.getAmount();
                            }
                            else {
                                otherTotal += model.getAmount();
                            }
                        }

                        adapter.notifyDataSetChanged();

                        tvTotal.setText("-" + total);

                        String suggestion;

                        if(total == 0){
                            suggestion = "Start tracking your expenses!";
                        }
                        else if(foodTotal > total * 0.4){
                            suggestion = "You are spending too much on Food 🍕";
                        }
                        else if(travelTotal > total * 0.3){
                            suggestion = "Travel expenses are high 🚗";
                        }
                        else {
                            suggestion = "Your spending is balanced 👍";
                        }

                        tvSuggestion.setText("🤖💭"+suggestion);
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        addbtn = findViewById(R.id.Addbutton);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ExpenseAdd.class);
                startActivity(intent);
            }
        });

    }
}