package com.bvaleo.testappshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bvaleo.testappshop.R;
import com.bvaleo.testappshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valery on 10.02.2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> mData;
    private Context mContext;

    public ProductAdapter(List<Product> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView ivImage;
        @BindView(R.id.tw_name)
        TextView twName;
        @BindView(R.id.tw_price)
        TextView twPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mData.get(position);

        holder.twName.setText(product.getName());
        holder.twPrice.setText(product.getPrice().concat("$"));

        Picasso.with(mContext)
                .load(product.getImage())
                .resize(80, 80)
                .error(R.drawable.box)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<Product> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public void updateData(int pos){
        this.mData.remove(pos);
        this.notifyDataSetChanged();
    }

    public void updateData(Product product, int pos){
        this.mData.set(pos, product);
        this.notifyDataSetChanged();
    }

    public void updateData(Product product){
        this.mData.add(product);
        this.notifyDataSetChanged();
    }

    public Product getProduct(int id) {
        return mData.get(id);
    }
}