package edu.distributedsystems.pw2;

import java.io.Serializable;

public class Sale implements Serializable {
    private String date;
    private String region;
    private String product;
    private int qty;
    private float cost;
    private float amt;
    private float tax;
    private float total;

    public Sale(String[] input) throws Exception {
        if (input.length < 8 ){
                throw new IllegalArgumentException("Incorrect number of values in the array. Please provide at least 8 values.");
        }

        date = input[0];
        region = input[1];
        product = input[2];
        qty = Integer.parseInt(input[3]);
        cost = Float.parseFloat(input[4]);
        amt = Float.parseFloat(input[5]);
        tax = Float.parseFloat(input[6]);
        total = Float.parseFloat(input[7]);
    }
    
    public String getDate() {
        return date;
    }

    public String getRegion() {
        return region;
    }

    public String getProduct() {
        return product;
    }

    public int getQty() {
        return qty;
    }

    public float getCost() {
        return cost;
    }

    public float getAmt() {
        return amt;
    }

    public float getTax() {
        return tax;
    }

    public float getTotal() {
        return total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
