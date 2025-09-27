package com.misa.bookstore.orderservice;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "orders")
public class OrderEntity {
    @Id private String id;
    private String userId;            // link to UserService user id
    private String shippingAddress;
    private List<OrderItem> items;
    private Double totalAmount;

    // getters/setters
    public String getId(){return id;} public void setId(String id){this.id = id;}
    public String getUserId(){return userId;} public void setUserId(String userId){this.userId = userId;}
    public String getShippingAddress(){return shippingAddress;} public void setShippingAddress(String shippingAddress){this.shippingAddress = shippingAddress;}
    public List<OrderItem> getItems(){return items;} public void setItems(List<OrderItem> items){this.items = items;}
    public Double getTotalAmount(){return totalAmount;} public void setTotalAmount(Double totalAmount){this.totalAmount = totalAmount;}

    public static class OrderItem {
        private String bookId;
        private int qty;
        public String getBookId(){return bookId;} public void setBookId(String bookId){this.bookId = bookId;}
        public int getQty(){return qty;} public void setQty(int qty){this.qty = qty;}
    }
}
