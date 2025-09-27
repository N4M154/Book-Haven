package com.misa.bookstore.orderservice.model;

public class InventoryDto {
    private String id;
    private String bookId;
    private int available;
    private int reserved;
    // getters/setters
    public String getId(){return id;} public void setId(String id){this.id=id;}
    public String getBookId(){return bookId;} public void setBookId(String bookId){this.bookId=bookId;}
    public int getAvailable(){return available;} public void setAvailable(int available){this.available=available;}
    public int getReserved(){return reserved;} public void setReserved(int reserved){this.reserved=reserved;}
}
