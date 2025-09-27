package com.misa.bookstore.checkoutservice;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "checkouts")
public class CheckoutEntity {

    @Id
    private String id;
    private String customerEmail;
    private String shippingAddress;
    private List<CheckoutItem> items;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<CheckoutItem> getItems() { return items; }
    public void setItems(List<CheckoutItem> items) { this.items = items; }
}

class CheckoutItem {
    private String bookId;
    private int qty;

    // Getters and setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
}
