package com.misa.bookstore.cartservice.model;
public class CartItem {
    private String bookId;
    private int qty;
    private Double priceAtAdd;

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public Double getPriceAtAdd() { return priceAtAdd; }
    public void setPriceAtAdd(Double priceAtAdd) { this.priceAtAdd = priceAtAdd; }
}
