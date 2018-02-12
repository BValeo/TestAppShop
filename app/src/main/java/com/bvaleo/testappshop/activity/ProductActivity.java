package com.bvaleo.testappshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.model.Product;
import com.bvaleo.testappshop.util.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valery on 11.02.2018.
 */

public class ProductActivity extends AppCompatActivity {


    @BindView(R.id.iv_product)
    ImageView ivProduct;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.tw_description)
    TextView twDescription;
    @BindView(R.id.tw_price_product)
    TextView twPriceProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Product product = intent.getParcelableExtra(Constants.PRODUCT_EXTRA);
        String idProduct = intent.getStringExtra(Constants.PRODUCT_ID_EXTRA);

        getSupportActionBar().setTitle(product.getName());

        Picasso.with(this)
                .load(product.getImage())
                .error(R.drawable.box)
                .into(ivProduct);
        twDescription.setText(product.getDescription());
        twPriceProduct.setText(product.getPrice().concat("$"));

        btnBuy.setOnClickListener(view -> {
            Toast.makeText(this, "Делается заказ", Toast.LENGTH_SHORT).show();
            Intent makeOrder = new Intent(this, OrderActivity.class);
            makeOrder.putExtra(Constants.PRODUCT_ID_EXTRA, idProduct);
            startActivity(makeOrder);
        });


    }
}
