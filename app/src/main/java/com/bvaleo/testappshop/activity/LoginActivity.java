package com.bvaleo.testappshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.model.User;
import com.bvaleo.testappshop.util.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valery on 11.02.2018.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etLoginR)
    EditText etLogin;
    @BindView(R.id.etPassR)
    EditText etPass;
    @BindView(R.id.login)
    Button btn_login;
    @BindView(R.id.signin)
    Button btn_signup;
    @BindView(R.id.pb_login)
    ProgressBar pbLogin;

    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        pbLogin.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseFirestore.getInstance();

        btn_login.setOnClickListener(view -> {
            if (!etLogin.getText().toString().equals("")
                    && !etPass.getText().toString().equals("")) {
                String curLogin = etLogin.getText().toString();
                String curPass = etPass.getText().toString();

                btn_login.setEnabled(false);
                pbLogin.setVisibility(View.VISIBLE);

                mDatabase.collection(Constants.DB_USER)
                        .whereEqualTo(Constants.USER_LOGIN, curLogin)
                        .get()
                        .addOnCompleteListener(task -> {

                            String login = "";
                            String pass = "";
                            String role = "";

                            for (DocumentSnapshot d : task.getResult()) {
                                login = (String) d.get(Constants.USER_LOGIN);
                                pass = (String) d.get(Constants.USER_PASS);
                                role = (String) d.get(Constants.USER_ROLE);
                                User user = new User(login, pass, role);
                                Log.d("MyLOG", user.toString());
                                break;
                            }

                            if (curPass.equals(pass)) {
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra(Constants.USER_ROLE, role);
                                startActivity(intent);
                            } else {
                                btn_login.setEnabled(true);
                                pbLogin.setVisibility(View.INVISIBLE);
                                Toast.makeText(this, "Неправильный логин или пароль. Повторите ввод данных", Toast.LENGTH_SHORT).show();
                            }});
            }
        });

        btn_signup.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        etLogin.setText("");
        etPass.setText("");
        btn_login.setEnabled(true);
        pbLogin.setVisibility(View.INVISIBLE);
    }
}
