package com.bvaleo.testappshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.model.Product;
import com.bvaleo.testappshop.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valery on 11.02.2018.
 */

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_image)
    EditText etImage;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private Product product;
    private String id;
    private FirebaseFirestore mDatabase;

    private static final int ADD_CODE = 100;
    private static final int EDIT_CODE = 101;
    private static final int DELETE_CODE = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        mDatabase = FirebaseFirestore.getInstance();

        Intent receiveIntent = getIntent();

        if (receiveIntent.getExtras() != null) {
            product = receiveIntent.getParcelableExtra(Constants.PRODUCT_EXTRA);
            id = receiveIntent.getStringExtra(Constants.PRODUCT_ID_EXTRA);

            etName.setText(product.getName());
            etImage.setText(product.getImage());
            etDescription.setText(product.getDescription());
            etPrice.setText(product.getPrice());

            btnDelete.setOnClickListener(view -> deleteProduct());
            btnEdit.setOnClickListener(view -> editProduct());
            btnAdd.setVisibility(View.INVISIBLE);
        } else {
            btnEdit.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnAdd.setOnClickListener(view -> addProduct());
        }


    }

    private void deleteProduct() {
        mDatabase.collection(Constants.DB_PRODUCT)
                .document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Товар удалён из базы данных", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка удаления товара из БД", Toast.LENGTH_SHORT).show();
                    Log.e(Constants.FIRESTORE_ERROR, "Ошибка удаления", e.getCause());
                });

        Intent answer = new Intent();
        answer.putExtra(Constants.PRODUCT_ID_EXTRA, id);
        setResult(DELETE_CODE, answer);
        finish();
    }

    private void editProduct() {
        String name = etName.getText().toString();
        String image = etImage.getText().toString();
        String description = etDescription.getText().toString();
        String price = etPrice.getText().toString();

        if (!name.equals("") && !image.equals("")
                && !description.equals("")
                && !price.equals("")) {
            Product product = new Product(name, image, description, price);
            mDatabase.collection(Constants.DB_PRODUCT)
                    .document(id)
                    .set(product)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(this, "Товар успешно изменён", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Ошибка изменения товара в БД", Toast.LENGTH_SHORT).show();
                        Log.e(Constants.FIRESTORE_ERROR, "Ошибка изменения товара", e.getCause());
                    });

            Intent answer = new Intent();
            answer.putExtra(Constants.PRODUCT_EXTRA, product);
            answer.putExtra(Constants.PRODUCT_ID_EXTRA, id);

            setResult(EDIT_CODE, answer);
            finish();
        }
    }

    private void addProduct() {
        String name = etName.getText().toString();
        String image = etImage.getText().toString();
        String description = etDescription.getText().toString();
        String price = etPrice.getText().toString();

        if (!name.equals("") && !image.equals("")
                && !description.equals("")
                && !price.equals("")) {

            Product p = new Product(name, image, description, price);
            mDatabase.collection(Constants.DB_PRODUCT).add(p)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(this, "Товар успешно добавлен", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Ошибка добавления товара в БД", Toast.LENGTH_SHORT).show();
                        Log.e(Constants.FIRESTORE_ERROR, "Ошибка добавления товара", e.getCause());
                    });
            Intent answer = new Intent();
            answer.putExtra(Constants.PRODUCT_EXTRA, p);
            setResult(ADD_CODE, answer);
            finish();
        }


    }
}
