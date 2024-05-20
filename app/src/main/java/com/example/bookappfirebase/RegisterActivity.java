package com.example.bookappfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button rgsBtn;
    ImageView bkBtn;
    EditText name, email, password, cPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rgsBtn = findViewById(R.id.registerBtn);
        bkBtn = findViewById(R.id.backBtn);
        name = findViewById(R.id.nameEt);
        email = findViewById(R.id.emailEt);
        password = findViewById(R.id.passwordEt);
        cPassword = findViewById(R.id.cPasswordEt);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rgsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            validatedata();
            }
        });
    }

    String sName = "",sEmail = "",sPassword = "", scPassword = "";
    private void validatedata() {
        sName = name.getText().toString().trim();
        sEmail = email.getText().toString().trim();
        sPassword = password.getText().toString().trim();
        scPassword = cPassword.getText().toString().trim();
        if(TextUtils.isEmpty(sName)){
            Toast.makeText(this, "Enter Your Name!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            Toast.makeText(this, "Invalid Mail!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(scPassword)) {
            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!sPassword.matches(scPassword)) {
            Toast.makeText(this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
        } else {
            createNewUser();
        }
    }

    private void createNewUser() {
        progressDialog.setMessage("Creating new User");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                updateUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
       progressDialog.setMessage("saving user info..");
       progressDialog.show();
       long timeStamp = System.currentTimeMillis();
       String uid = firebaseAuth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("email",sEmail);
        hashMap.put("name",sName);
        hashMap.put("profileImage","");
        hashMap.put("userType","user");
        hashMap.put("timestamp",timeStamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,DashboardUserActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

