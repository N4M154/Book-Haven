# BookHaven – A Microservice Approach for Online Book Management  
**Course:** Software Design and Architecture (SWE 4601)  

---

##  Microservices Overview  

- **Eureka Server** 
- **API Gateway**  
- **User Service** 
- **Cart Service** 
- **Review Service**   
- **Catalog Service** 
- **Wishlist Service**  
- **Order Service** 
- **Inventory Service**   
- **Checkout Service** 


## Contributors  

- [**Namisa Najah Raisa – 210042112**](https://github.com/N4M154)  
- [**Nazifa Tasneem – 210042114**](https://github.com/nazifatasneem13)  
- [**Irfam Hakim Bhuiyan – 210042158**](https://github.com/irfanAbir1231)  



## Contributions  

### Namisa Najah Raisa
- **Core Services Setup**  
  - Eureka Server & API Gateway  
  - Base implementation of Order, Checkout, Catalog, and Cart Services  
  - Full implementation of User Service  
- **Service Integrations**  
  - Established communication between User, Order, and Inventory Services  
- **Features Implemented**  
  - User registration & login with password hashing  
  - Catalog Service: add books (auto-generate book IDs)  
  - Inventory Service: set book quantity & manage stock  
  - Order Service:  
    - Place order with user & book IDs (auto-generate order ID)  
    - Verify order creation (returns order ID)  
    - Verify inventory updates (decrease stock, return remaining quantity)  
    - Restrict users from ordering out-of-stock books  



### Nazifa Tasneem
*(Details to be added)*  


### Irfam Hakim Bhuiyan
*(Details to be added)*  



## API Testing with Postman  

The Postman collections are included in the repository.  
Alternatively, you can use the direct links below to test the APIs.  

> 💡 Tip: Set the environment to **`local`** for better flexibility.  
> The collections include **post-test scripts** to automatically save variables.  

### Postman Collections  

- [Namisa's Test](https://solo66-1179.postman.co/workspace/microservice~e48b35f7-e9d7-4881-a420-69920823ece1/collection/33047879-104184db-4148-4953-906b-4d9a282826d6?action=share&creator=33047879)  
- [Nazifa's Test](https://solo66-1179.postman.co/workspace/microservice~e48b35f7-e9d7-4881-a420-69920823ece1/collection/33047879-ac5dfc25-5080-442f-9130-a086ce35d291?action=share&creator=33047879)  
- [Irfan's Test](https://solo66-1179.postman.co/workspace/microservice~e48b35f7-e9d7-4881-a420-69920823ece1/collection/33047879-218e3c09-09b4-4ae1-a1b6-69e37cd3f5b4?action=share&creator=33047879)  

