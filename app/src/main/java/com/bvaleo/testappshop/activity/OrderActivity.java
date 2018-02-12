package com.bvaleo.testappshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.model.Order;
import com.bvaleo.testappshop.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valery on 11.02.2018.
 */

public class OrderActivity extends AppCompatActivity {


    @BindView(R.id.btn_order)
    Button btn_order;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_email)
    EditText etEmail;

    private FirebaseFirestore mDatabase;
    private String product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        mDatabase = FirebaseFirestore.getInstance();
        product = getIntent().getStringExtra(Constants.PRODUCT_ID_EXTRA);


        btn_order.setOnClickListener(view -> {

            String fullName = etName.getText().toString();
            String address = etAddress.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            
            if (!fullName.equals("") && !address.equals("") && !phone.equals("") && !email.equals("")) {
                mDatabase.collection(Constants.DB_ORDERS)
                        .add(new Order(product, fullName, phone, address, email))
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Заказ добавлен", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Log.e(Constants.FIRESTORE_ERROR, "Ошибка при добавлении документа", e.getCause()));
            } else Toast.makeText(this, "Заполните все поля и повторите попытку", Toast.LENGTH_SHORT).show();
        });


    }
}
