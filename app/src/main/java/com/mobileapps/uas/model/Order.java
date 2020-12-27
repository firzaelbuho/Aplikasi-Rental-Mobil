package com.mobileapps.uas.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private String status = "";
    private String orderId = "";
    private long orderDate = 0;
    private long rendStartDate = 0;
    private long rendEndDate = 0;
    private String customerId = "";
    private String customerName = "";
    private String address = "";
    private String phone = "";
    private String carName = "";
    private int carprice = 0;
    private int totalPrice = 0;
    private String carId = "";

    public Order(String status, String orderId, long orderDate, long rendStartDate, long rendEndDate, String customerId, String customerName, String address, String phone, String carName, int carprice, int totalPrice, String carId) {
        this.status = status;
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.rendStartDate = rendStartDate;
        this.rendEndDate = rendEndDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.carName = carName;
        this.carprice = carprice;
        this.totalPrice = totalPrice;
        this.carId = carId;
    }

    public Order() {

    }


    protected Order(Parcel in) {
        status = in.readString();
        orderId = in.readString();
        orderDate = in.readLong();
        rendStartDate = in.readLong();
        rendEndDate = in.readLong();
        customerId = in.readString();
        customerName = in.readString();
        address = in.readString();
        phone = in.readString();
        carName = in.readString();
        carprice = in.readInt();
        totalPrice = in.readInt();
        carId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(orderId);
        dest.writeLong(orderDate);
        dest.writeLong(rendStartDate);
        dest.writeLong(rendEndDate);
        dest.writeString(customerId);
        dest.writeString(customerName);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(carName);
        dest.writeInt(carprice);
        dest.writeInt(totalPrice);
        dest.writeString(carId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public long getRendStartDate() {
        return rendStartDate;
    }

    public void setRendStartDate(long rendStartDate) {
        this.rendStartDate = rendStartDate;
    }

    public long getRendEndDate() {
        return rendEndDate;
    }

    public void setRendEndDate(long rendEndDate) {
        this.rendEndDate = rendEndDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getCarprice() {
        return carprice;
    }

    public void setCarprice(int carprice) {
        this.carprice = carprice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}


