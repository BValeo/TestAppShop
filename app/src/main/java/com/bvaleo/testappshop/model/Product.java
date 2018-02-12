package com.bvaleo.testappshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Valery on 09.02.2018.
 */


public class Product implements Parcelable {
    private String name;
    private String image;
    private String description;
    private String price;


    public Product(String name, String image, String description, String price) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.description);
        dest.writeString(this.price);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.name = in.readString();
        this.image = in.readString();
        this.description = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
