package com.bookstore.wishlist.model;

import javax.persistence.*;

@Entity
public class WishlistItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long bookId;
    public WishlistItem() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getUserId(){return userId;} public void setUserId(Long u){this.userId=u;}
    public Long getBookId(){return bookId;} public void setBookId(Long b){this.bookId=b;}
}
