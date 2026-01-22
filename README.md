# 🛡️ Secure OTP-Based File Sharing System

A Spring Boot RESTful service that allows users to upload files and share them securely using a one-time password (OTP) and a temporary download token.



## 🛠 Features

* **Secure Upload**: Files up to 50MB with unique filename generation.
* **OTP Protection**: Files are protected by a 6-digit OTP generated upon upload.
* **Two-Step Validation**:
    1.  Validate OTP to receive a unique Download Token.
    2.  Use the Download Token to fetch the actual file.
* **Expiration Logic**: Files and tokens expire after 10 minutes.
* **Auto-Cleanup**: A background scheduler automatically removes expired files from the disk and database every minute.
* **Global Exception Handling**: Standardized JSON responses for errors (404, 410, 413, etc.).

---

## 🛣 API Endpoints

### 1. Upload File
**POST** `/api/files/upload`

* **Request**: `multipart/form-data` containing a `file` param.
* **Success Response**: Returns a 6-digit OTP and the expiration timestamp.

### 2. Validate OTP
**GET** `/api/files/otp/{otp}`

* **Description**: Validates the OTP. If valid, marks it as used and generates a unique UUID download token.
* **Constraint**: Once validated, the OTP cannot be used again.

### 3. Download File
**GET** `/api/files/download/{token}`

* **Description**: Streams the file directly to the client.
* **Constraint**: This token is single-use and expires if not used before the file's expiration time.

---

## ⚙️ Configuration

Ensure the following properties are set in your `application.properties`:

```properties
# Directory where files will be stored
file.upload-dir=uploads

# Base path for generating download links
app.download.base-path=http://localhost:8080/api/files/download/