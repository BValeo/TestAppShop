package com.bvaleo.testappshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.adapter.ProductAdapter;
import com.bvaleo.testappshop.adapter.clicklistener.RecyclerViewClickListener;
import com.bvaleo.testappshop.model.Product;
import com.bvaleo.testappshop.util.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;

    private FirebaseFirestore mDatabase;
    private ProductAdapter mAdapter;
    private List<String> idProducts = new ArrayList<>();
    private String role;
    private static final int CODE = 1;

    private static final int ADD_CODE = 100;
    private static final int EDIT_CODE = 101;
    private static final int DELETE_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new ProductAdapter(new ArrayList<>(0), this);
        role = getIntent().getStringExtra(Constants.USER_ROLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);

        rvMain.setLayoutManager(layoutManager);
        rvMain.setAdapter(mAdapter);
        rvMain.addOnItemTouchListener(new RecyclerViewClickListener(this, rvMain, (view, position) -> {

            Product product = mAdapter.getProduct(position);
            if (role.equals(Constants.ROLE_USER)) {
                Intent productActivity = new Intent(this, ProductActivity.class);
                productActivity.putExtra(Constants.PRODUCT_EXTRA, product);
                productActivity.putExtra(Constants.PRODUCT_ID_EXTRA, idProducts.get(position));
                startActivity(productActivity);
            } else if (role.equals(Constants.ROLE_ADMIN)) {
                Intent adminActivity = new Intent(this, AdminActivity.class);
                adminActivity.putExtra(Constants.PRODUCT_EXTRA, product);
                adminActivity.putExtra(Constants.PRODUCT_ID_EXTRA, idProducts.get(position));
                Toast.makeText(this, "Admin)", Toast.LENGTH_SHORT).show();
                startActivityForResult(adminActivity, CODE);
            }


        }));
        rvMain.addItemDecoration(decoration);


        mDatabase = FirebaseFirestore.getInstance();
        mDatabase.collection(Constants.DB_PRODUCT)
                .get()
                .addOnCompleteListener(task -> {

                    List<Product> products = new ArrayList<>();

                    for (DocumentSnapshot d :
                            task.getResult()) {

                        idProducts.add(d.getId());


                        String name = (String) d.get("name");
                        String image = (String) d.get("image");
                        String description = (String) d.get("description");
                        String price = (String) d.get("price");

                        //products.add(new Product(name, image, description, price));
                        products.add(d.toObject(Product.class));
                        Log.d("MyLOG", name + " " + image + " " + description + " " + price);

                    }

                    mAdapter.updateData(products);

                })
                .addOnFailureListener(e -> Log.w(Constants.FIRESTORE_ERROR, "Ошибка при добавлении документа", e.getCause()));


    }

    //if(resultCode == RESULT_OK ){


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (resultCode) {
                case ADD_CODE:
                    if(data != null){
                        Product p = data.getParcelableExtra(Constants.PRODUCT_EXTRA);
                        mAdapter.updateData(p);
                    }
                    break;
                case EDIT_CODE:
                    if (data != null) {
                        Product p = data.getParcelableExtra(Constants.PRODUCT_EXTRA);
                        int pos = idProducts.indexOf(data.getStringExtra(Constants.PRODUCT_ID_EXTRA));
                        mAdapter.updateData(p, pos);
                    }
                    break;
                case DELETE_CODE:
                    String id = "";
                    if (data != null) {
                        id = data.getStringExtra(Constants.PRODUCT_ID_EXTRA);
                        mAdapter.updateData(idProducts.indexOf(id));
                        idProducts.remove(id);
                    }
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(role.equals(Constants.ROLE_ADMIN)){
            getMenuInflater().inflate(R.menu.admin_menu, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.user_menu, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                addProduct();
                return true;
            case R.id.exit_admin:
                finish();
                return true;
            case R.id.exit_user:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void addProduct(){
        Intent intent = new Intent(this, AdminActivity.class);
        startActivityForResult(intent, CODE);
    }
}
