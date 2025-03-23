# Installation Guide

## Prerequisites
Before setting up the system, ensure that you have the following installed:
- **Java 21** or higher
- **Maven 3.6+**
- **Microsoft SQL Server**
- **SQL Server Management Studio (SSMS)** (or using database management tools in IntelliJ IDEA)

## Step 1: Database Configuration
1. Open Microsoft SQL Server Management Studio (SSMS) or database management tools in IntelliJ IDEA.
2. Connect to the SQL Server instance.
3. Create a new database named `hsf302` using the following SQL command:
```sql
CREATE DATABASE hsf302;
```
3. Update the `application.yml` file with your database credentials:
```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=hsf302;encrypt=true;trustServerCertificate=true
    username: YOUR_DATABASE_USERNAME
    password: YOUR_DATABASE_PASSWORD
```

## Step 2: Change the Recaptcha, and VNPay API Keys (Optional)
- Update the Recaptcha in the `application.yml` file:
```yaml
recaptcha:
  url: YOUR_RECAPTCHA_URL (using google recaptcha v2)
  key:
    secret: YOUR_RECAPTCHA_SECRET_KEY
    site: YOUR_RECAPTCHA_SITE_KEY
```
- Update the VNPay API keys in the `application.yml` file:
```yaml
vnpay:
  vnp_Version: 2.1.0
  vnp_CurrCode: VND
  vnp_Locale: vn
  vnp_IpAddr: 127.0.0.1
  vnp_TmnCode: YOUR_VNPAY_TMN_CODE
  vnp_HashSecret: YOUR_VNPAY_HASH_SECRET
  vnp_Url: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
  vnp_apiUrl: https://sandbox.vnpayment.vn/merchant_webapi/api/transaction
  vnp_ReturnUrl: http://localhost:8080/vnpay-return
 ```

## Step 3: Setting Up the Owner Account
The default owner account is configured as follows:
```yaml
owner:
  username: owner
  password: owner
  firstname: Chủ
  lastname: cửa hàng
  email: owner@gmail.com
  phone: 0123456789
  gender: true
  birthday: 2004-09-14
  address: 123 Đường 123, Quận 123, TP. 123
  avatar: images/male.png
```
Log in with these credentials and update them as necessary.

## Step 4: Storage Configuration
Ensure that the following directories exist for file storage:
- **Uploads:** `uploads/`
- **Temporary Files:** `temps/`

## Step 5: Build and Run the Project
### Using an IDE (IntelliJ IDEA)
1. Open the project in your preferred IDE.
2. Ensure that the Java SDK and Maven are properly configured.
3. Run the `Sp25Hsf302Group3Se1889VjApplication` class.


