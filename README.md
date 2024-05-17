# E-Commerce Marketplace

## Description

The e-commerce platform provides a feature-rich environment for both users and merchants to engage in online retail activities. With its robust architecture and extensive set of functionalities, it offers a seamless and secure shopping experience for all stakeholders involved.

# Technologies Used
A list of technologies, frameworks, and tools used in the project.

* Java
* Spring (Core, Boot, MVC, Security, Data JPA)
* Postgresql, Hibernate
* Maven, Lombok, Mapstruct
* ELK (Elasticsearch, Logstash, Kibana)
* JUnit, Mockito
* Docker
* Swagger
* JWT


## Table of Contents

### API Documentation

#### Authentication

- [Login](#login)
- [Register](#register-user)
- [Register Merchant](#register-merchant)
- [Refresh Token](#refresh-token)

#### Email Confirmation

- [Confirm Email](#email-confirmation)

#### Password Reset

- [Request Password Reset](#request-password-reset)
- [Confirm Password Reset](#confirm-password-reset)

#### Users

- [Find by ID](#get-user-by-id)
- [Find All](#get-all-users)
- [Update User Fully](#update-user-fully)
- [Partially Update User](#partially-update-user)
- [Delete User](#delete-user)

#### Mechants

- [Get Merchant by ID](#get-merchant-by-id)
- [Get All Merchants](#get-all-merchants)
- [Update Merchant Fully](#update-merchant-fully)
- [Partially Update Merchant](#partially-update-merchant)
- [Delete Merchant](#delete-merchant)
- [Get Merchant Products](#get-merchant-products)
- [Get Product by ID](#get-product-by-id)
- [Create Product](#create-product)
- [Update Product Fully](#update-product-fully)
- [Partially Update Product](#partially-update-product)
- [Delete Product](#delete-product)

#### Categories

- [Find by ID](#get-category-by-id)
- [Find All](#get-all-categories)
- [Create Category](#create-category)
- [Update Category Fully](#update-category-fully)
- [Patrially Update Category](#partially-update-category)
- [Delete Category](#delete-category)
- [Find Products By Category](#get-products-by-category)

#### Products

- [Get All Products](#get-all-products)
- [Search Products](#search-products)

#### Shopping Cart

- [Add Item](#add-item)
- [Get All Items](#get-all-items)
- [Delete Item](#delete-item)
- [Update Item Quantity](#update-item-quantity)

#### Orders

- [Create Order](#create-order)
- [Get All Orders](#get-all-orders-by-user)
- [Get Order by ID](#get-order-by-id)
- [Get All Order Items](#get-all-order-items-by-order-id)
- [Delete Order](#delete-order-by-id)

#### Merchant Orders

- [Change Merchant Order Status](#change-merchant-order-status)
- [Get Merchant Orders By Merchant](#get-merchant-orders-by-merchant)

### Testing
- [About Tests](#testing-status-for-the-project)

# API Documentation

# Authentication

### JWT-Based Authentication

The authentication system is based on JWT (JSON Web Token) tokens. Upon successful login, a JWT token is generated and returned in the response. This token should be included in the Authorization header of subsequent requests to authenticate the user.

The JWT token contains encoded information about the user's identity and permissions. It is signed with a secret key to ensure its authenticity and integrity. When a token expires, the user can refresh it using the refresh token endpoint (`/api/auth/refresh`), provided the refresh token has not expired.

#### Register User

- **Endpoint:** `/api/auth/register`
- **Method:** `POST`
- **Description:** Register a new user.
- **Request Body:** `RegisterRequest`
- **Response:** `RegisterResponse`

#### Register Merchant

- **Endpoint:** `/api/auth/merchants/register`
- **Method:** `POST`
- **Description:** Register a new merchant.
- **Request Body:** `MerchantRegistrationRequestDto`
- **Response:** `MerchantRegistrationResponseDto`

### Email Verification

After successfully registering as a user or merchant, an email verification link will be sent to the provided email address. The user/merchant needs to click on this link to verify their email address and activate their account.

#### Confirm Email

- **Endpoint:** `/api/confirm`
- **Method:** `GET`
- **Description:** Confirm email address after registration.
- **Query Parameters:**
  - `token`: The verification token received via email.

#### Confirmation Flow

After clicking the email verification link sent to the user/merchant's email address, the user will be redirected to the `/api/confirm` endpoint with the verification token as a query parameter. The `EmailConfirmationController` will handle the confirmation process by invoking the `confirm` method in the `EmailConfirmationService`.

#### Login 

- **Endpoint:** `/api/auth/login`
- **Method:** `POST`
- **Description:** Login an existing user.
- **Request Body:** `LoginRequest`
- **Response:** `LoginResponse`

#### Refresh Token

- **Endpoint:** `/api/auth/refresh`
- **Method:** `POST`
- **Description:** Refresh the JWT token.
- **Request:** Authorization http header with bearer token.
- **Response:** `UserJwtTokenResponseDto`

### Brute Force Login Protection

To protect against brute force attacks, the following limits are enforced:

- Email-based Limiting: A maximum of 5 login attempts per email address.
- IP-based Limiting: A maximum of 10 login attempts per IP address.

After exceeding these limits, further login attempts will be temporarily blocked.

### Password Reset

#### Request Password Reset

- **Endpoint:** `/api/password-reset-request`
- **Method:** `POST`
- **Description:** Initiate the password reset process by requesting a password reset link.
- **Request Body:** `PasswordResetRequestDto`

#### Confirm Password Reset

- **Endpoint:** `/api/confirm-password-reset`
- **Method:** `POST`
- **Description:** Confirm the password reset by providing the new password and the reset token.
- **Request Body:** `PasswordResetConfirmationRequestDto`

### Password Reset Flow

**Request Password Reset:**

The user initiates the password reset process by sending a POST request to `/api/password-reset-request` with their email address. Upon receiving the request, the system sends an email containing a password reset link with a unique reset token.

**Confirm Password Reset:**

After clicking the password reset link in the email, the user is directed to the password reset confirmation page. The user provides the new password and the reset token in the request body of a POST request to `/api/confirm-password-reset`. The `PasswordResetController` invokes the `confirmPasswordReset` method in the `PasswordResetService` to process the password reset confirmation.


### User Management

#### Get User by ID

- **Endpoint:** `/api/users/{id}`
- **Method:** `GET`
- **Description:** Retrieve user information by their public ID.
- **Path Parameters:**
  - `id`: The public ID of the user.
- **Response:** `UserResponseDto`

#### Get All Users

- **Endpoint:** `/api/users`
- **Method:** `GET`
- **Description:** Retrieve a list of all users.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of users per page (optional).
- **Response:** `Page<UserResponseDto>`

#### Update User Fully

- **Endpoint:** `/api/users/{id}`
- **Method:** `PUT`
- **Description:** Update user information completely.
- **Path Parameters:**
  - `id`: The public ID of the user.
- **Request Body:** `UserUpdateRequestDto`
- **Response:** `UserUpdateResponseDto`

#### Partially Update User

- **Endpoint:** `/api/users/{id}`
- **Method:** `PATCH`
- **Description:** Partially update user information.
- **Path Parameters:**
  - `id`: The public ID of the user.
- **Request Body:** `UserPatchUpdateRequestDto`
- **Response:** `UserUpdateResponseDto`

#### Delete User

- **Endpoint:** `/api/users/{id}`
- **Method:** `DELETE`
- **Description:** Delete a user by their public ID.
- **Path Parameters:**
  - `id`: The public ID of the user.
 
### Merchant Management

#### Get Merchant by ID

- **Endpoint:** `/api/merchants/{id}`
- **Method:** `GET`
- **Description:** Retrieve merchant information by their public ID.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
- **Response:** `MerchantResponseDto`

#### Get All Merchants

- **Endpoint:** `/api/merchants`
- **Method:** `GET`
- **Description:** Retrieve a list of all merchants.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of merchants per page (optional).
- **Response:** `Page<MerchantResponseDto>`

#### Update Merchant Fully

- **Endpoint:** `/api/merchants/{id}`
- **Method:** `PUT`
- **Description:** Update merchant information completely.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
- **Request Body:** `MerchantUpdateRequestDto`
- **Response:** `MerchantUpdateResponseDto`

#### Partially Update Merchant

- **Endpoint:** `/api/merchants/{id}`
- **Method:** `PATCH`
- **Description:** Partially update merchant information.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
- **Request Body:** `MerchantPatchUpdateRequestDto`
- **Response:** `MerchantUpdateResponseDto`

#### Delete Merchant

- **Endpoint:** `/api/merchants/{id}`
- **Method:** `DELETE`
- **Description:** Delete a merchant by their public ID.
- **Path Parameters:**
  - `id`: The public ID of the merchant.

#### Get Merchant Products

- **Endpoint:** `/api/merchants/{id}/products`
- **Method:** `GET`
- **Description:** Retrieve a page of products associated with the merchant.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of products per page (optional).
- **Response:** `Page<ProductResponseDto>`

#### Get Product by ID

- **Endpoint:** `/api/merchants/{id}/products/{productId}`
- **Method:** `GET`
- **Description:** Retrieve a product by its ID associated with the merchant.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
  - `productId`: The ID of the product.
- **Response:** `ProductResponseDto`

#### Create Product

- **Endpoint:** `/api/merchants/{id}/products`
- **Method:** `POST`
- **Description:** Create a new product associated with the merchant.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
- **Request Body:** `ProductRequestDto`
- **Response:** `ProductResponseDto`

#### Update Product Fully

- **Endpoint:** `/api/merchants/{id}/products/{productId}`
- **Method:** `PUT`
- **Description:** Update product information completely associated with the merchant.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
  - `productId`: The ID of the product.
- **Request Body:** `ProductUpdateRequestDto`
- **Response:** `ProductResponseDto`

#### Partially Update Product

- **Endpoint:** `/api/merchants/{id}/products/{productId}`
- **Method:** `PATCH`
- **Description:** Partially update product information associated with the merchant.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
  - `productId`: The ID of the product.
- **Request Body:** `ProductPatchUpdateRequestDto`
- **Response:** `ProductResponseDto`

#### Delete Product

- **Endpoint:** `/api/merchants/{id}/products/{productId}`
- **Method:** `DELETE`
- **Description:** Delete a product associated with the merchant.
- **Path Parameters:**
  - `id`: The public ID of the merchant.
  - `productId`: The ID of the product.


### Category Management

#### Get Category by ID

- **Endpoint:** `/api/categories/{id}`
- **Method:** `GET`
- **Description:** Retrieve category information by its ID.
- **Path Parameters:**
  - `id`: The ID of the category.
- **Response:** `CategoryDto`

#### Get All Categories

- **Endpoint:** `/api/categories`
- **Method:** `GET`
- **Description:** Retrieve a list of all categories.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of categories per page (optional).
- **Response:** `Page<CategoryDto>`

#### Create Category

- **Endpoint:** `/api/categories`
- **Method:** `POST`
- **Description:** Create a new category.
- **Request Body:** `CategoryRequestDto`
- **Response:** `CategoryDto`

#### Update Category Fully

- **Endpoint:** `/api/categories/{id}`
- **Method:** `PUT`
- **Description:** Update category information completely.
- **Path Parameters:**
  - `id`: The ID of the category.
- **Request Body:** `CategoryRequestDto`
- **Response:** `CategoryDto`

#### Partially Update Category

- **Endpoint:** `/api/categories/{id}`
- **Method:** `PATCH`
- **Description:** Partially update category information.
- **Path Parameters:**
  - `id`: The ID of the category.
- **Request Body:** `CategoryPatchUpdateRequestDto`
- **Response:** `CategoryDto`

#### Delete Category

- **Endpoint:** `/api/categories/{id}`
- **Method:** `DELETE`
- **Description:** Delete a category by its ID.
- **Path Parameters:**
  - `id`: The ID of the category.

#### Get Products by Category

- **Endpoint:** `/api/categories/{id}/products`
- **Method:** `GET`
- **Description:** Retrieve a page of products belonging to the specified category.
- **Path Parameters:**
  - `id`: The ID of the category.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of products per page (optional).
- **Response:** `Page<ProductResponseDto>`

### Product Management

#### Get All Products

- **Endpoint:** `/api/products`
- **Method:** `GET`
- **Description:** Retrieve a list of all products.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of products per page (optional).
- **Response:** `Page<ProductDto>`

#### Search Products

- **Endpoint:** `/api/products/search`
- **Method:** `GET`
- **Description:** Search for products by name, category, description.
- **Query Parameters:**
  - `query`: The search query for product.
  - `page`: Page number for pagination (optional).
  - `size`: Number of products per page (optional).
- **Response:** `Page<ProductResponseDto>`
#### Implementation Details
The search functionality is implemented using Elasticsearch. Elasticsearch provides powerful full-text search capabilities, making it suitable for searching products by name efficiently. It indexes product data and enables fast retrieval based on search queries. Utilizing Elasticsearch enhances the search performance and provides relevant search results to users.

Additionally, data is piped from PostgreSQL to Elasticsearch via Logstash. Logstash serves as a data pipeline, facilitating the transfer of data from PostgreSQL, where product information is stored, to Elasticsearch. This ensures that the search index in Elasticsearch is regularly updated with the latest product data from the PostgreSQL database, maintaining consistency and accuracy in search results.


### Shopping Cart Management

#### Add Item

- **Endpoint:** `/api/cart/cart-items`
- **Method:** `POST`
- **Description:** Add an item to the shopping cart.
- **Request Body:** `CartItemRequestDto`
- **Authorization:** User role required.
- **Response:** `ShoppingCartResponseDto`

#### Get All Items

- **Endpoint:** `/api/cart/cart-items`
- **Method:** `GET`
- **Description:** Retrieve all items in the shopping cart.
- **Authorization:** User role required.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of items per page (optional).
- **Response:** `Page<CartItemResponseDto>`

#### Delete Item

- **Endpoint:** `/api/cart/cart-items/{id}`
- **Method:** `DELETE`
- **Description:** Remove an item from the shopping cart by its ID.
- **Path Parameters:**
  - `id`: The ID of the item in the shopping cart.
- **Authorization:** User role required.
- **Response:** No content (HTTP status code 204)

#### Update Item Quantity

- **Endpoint:** `/api/cart/cart-items/{id}`
- **Method:** `PUT`
- **Description:** Update the quantity of an item in the shopping cart.
- **Path Parameters:**
  - `id`: The ID of the item in the shopping cart.
- **Request Body:** `CartItemQuantityUpdateRequest`
- **Authorization:** User role required.
- **Response:** `CartItemResponseDto`


### Order Management

#### Create Order

- **Endpoint:** `/api/orders`
- **Method:** `POST`
- **Description:** Create a new order.
- **Request Body:** `OrderCreateRequestDto`
- **Authorization:** User role required.
- **Response:** `OrderResponseDto`

#### Get All Orders by User

- **Endpoint:** `/api/orders`
- **Method:** `GET`
- **Description:** Retrieve all orders placed by the authenticated user.
- **Authorization:** User role required.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of orders per page (optional).
- **Response:** `Page<OrderResponseDto>`

#### Get Order by ID

- **Endpoint:** `/api/orders/{id}`
- **Method:** `GET`
- **Description:** Retrieve an order by its ID.
- **Path Parameters:**
  - `id`: The ID of the order.
- **Authorization:** User role required.
- **Response:** `OrderResponseDto`

#### Get All Order Items by Order ID

- **Endpoint:** `/api/orders/{id}/order-items`
- **Method:** `GET`
- **Description:** Retrieve all order items associated with a specific order.
- **Path Parameters:**
  - `id`: The ID of the order.
- **Authorization:** User role required.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of order items per page (optional).
- **Response:** `Page<OrderItemResponseDto>`

#### Delete Order by ID

- **Endpoint:** `/api/orders/{id}`
- **Method:** `DELETE`
- **Description:** Delete an order by its ID.
- **Path Parameters:**
  - `id`: The ID of the order.
- **Authorization:** User role required.
- **Response:** No content (HTTP status code 204)


### Merchant Order Management

#### Change Merchant Order Status

- **Endpoint:** `/api/merchant-orders/{id}`
- **Method:** `PATCH`
- **Description:** Change the status of a merchant order.
- **Path Parameters:**
  - `id`: The ID of the merchant order.
- **Request Body:** `StatusChangeRequestDto`
- **Authorization:** Merchant or admin role required.
- **Response:** `MerchantOrderResponseDto`

#### Get Merchant Orders by Merchant

- **Endpoint:** `/api/merchant-orders`
- **Method:** `GET`
- **Description:** Retrieve all merchant orders associated with the authenticated merchant.
- **Authorization:** Merchant or admin role required.
- **Query Parameters:**
  - `page`: Page number for pagination (optional).
  - `size`: Number of merchant orders per page (optional).
- **Response:** `Page<MerchantOrderResponseDto>`

### Feature: Automatic Splitting of Main Order into Merchant Orders

#### Description
When a user places an order for products from different merchants, the main order is automatically split into multiple merchant orders. Each merchant order corresponds to the products purchased from a specific merchant. This feature ensures efficient management and fulfillment of orders from various merchants.

#### Functionality

#### Automatic Splitting
- When the user submits an order containing products from multiple merchants, the system automatically splits the main order into separate merchant orders based on the merchant of each product.

#### Merchant Order Creation
- For each distinct merchant involved in the main order, a corresponding merchant order is created.
- Each merchant order contains only the products associated with that particular merchant.

#### Order Association
- Merchant orders are associated with the main order to maintain a clear relationship between them.
- Each merchant order includes a reference to the main order to track its origin.

#### Order Status Synchronization
- Changes in the status of the main order are reflected in the associated merchant orders.
- Updates to merchant orders, such as order cancellation or fulfillment, are reflected in the main order status.

### Testing Status For The Project

Most of the classes of the project are thoroughly tested to ensure their functionality, reliability, and security. Unit tests, integration tests, and possibly end-to-end tests have been conducted to validate the behavior of these classes under various scenarios.

Tests cover:

- **Controller Layer:** Unit tests ensure that controller endpoints handle requests correctly, handle edge cases, and return the expected responses.
- **Service Layer:** Unit tests verify the business logic implemented in the service layer, ensuring that services perform the intended operations accurately.
- **Repository Layer:** Integration tests validate the interaction between the application and the database, ensuring that data access operations function correctly.
- **Input Validation:** Tests ensure that input validation mechanisms are working correctly, preventing invalid data from being processed.
- **Security:** Tests validate that security measures, such as authentication and authorization, are implemented correctly to protect sensitive operations and resources.

The comprehensive testing approach helps maintain the quality and stability of the merchant management functionality, ensuring that it behaves as expected and meets the specified requirements.


# README FILE IS NOT COMPLETED
