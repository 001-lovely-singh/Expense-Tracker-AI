package com.example.expensetrackerai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class singup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView silogin;
        EditText siname , siemail , sipassword;
        Button singupbutton;
        FirebaseAuth auth;
        FirebaseDatabase database;
        String emailpattern = "^[a-zA-Z0-9]([a-zA-Z0-9.+-]*[a-zA-Z0-9])?@gmail\\.com$";



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        silogin = findViewById(R.id.loginbuttoninsingin);
        siname = findViewById(R.id.singupname);
        siemail = findViewById(R.id.singupEmailAddress);
        sipassword = findViewById(R.id.singupPassword);
        singupbutton = findViewById(R.id.singupbut);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        silogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(singup.this, login.class);
                startActivity(intent);
                finish();
            }
        });


        singupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = siname.getText().toString();
                String emaill = siemail.getText().toString();
                String passs = sipassword.getText().toString();
                String status = "Hey! I'm using DoDo Chat";


                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(passs)){
                    Toast.makeText(singup.this, "Please Enter Valid Info", Toast.LENGTH_SHORT).show();
                } else if (!emaill.matches(emailpattern)) {
                    siemail.setError("Enter Correct Email");
                } else if (passs.length()<4) {
                    sipassword.setError("Password is too short");
                } else {
                    auth.createUserWithEmailAndPassword(emaill,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);

                                Users users = new Users(id,namee,emaill,passs);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(singup.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Toast.makeText(singup.this, "Erron in creating user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(singup.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}