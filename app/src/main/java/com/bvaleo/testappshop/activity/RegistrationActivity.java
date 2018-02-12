package com.bvaleo.testappshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.model.User;
import com.bvaleo.testappshop.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valery on 11.02.2018.
 */

public class RegistrationActivity extends AppCompatActivity {
    @BindView(R.id.etLoginR)
    EditText etLoginR;
    @BindView(R.id.etPassR)
    EditText etPassR;
    @BindView(R.id.reg)
    Button reg;
    
    private FirebaseFirestore mDatabase;
    private String login;
    private String pass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        mDatabase = FirebaseFirestore.getInstance();
        
        reg.setOnClickListener(view -> {
            login = etLoginR.getText().toString();
            pass = etPassR.getText().toString();
            
            if(!login.equals("") && !pass.equals("")){
                mDatabase.collection(Constants.DB_USER).whereEqualTo(Constants.USER_LOGIN, login).get()
                        .addOnCompleteListener(task -> {
                            if(task.getResult().size() == 0){
                                User user = new  User(login, pass, Constants.ROLE_USER);
                                mDatabase.collection(Constants.DB_USER).add(user)
                                        .addOnCompleteListener(res -> {
                                            Toast.makeText(this, "Вы успешно зарегестрированы", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(this, MainActivity.class);
                                            intent.putExtra(Constants.USER_ROLE, user.getRole());
                                            startActivity(intent);
                                            finish();

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                                            Log.e(Constants.FIRESTORE_ERROR, "Ошибка регистрации", e.getCause());
                                        });
                            } else {
                                Toast.makeText(this, "Пользователь с таким Логином уже зарегистрирован.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else Toast.makeText(this, "Заполните все данные", Toast.LENGTH_SHORT).show();
        });
    }
}
