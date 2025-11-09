# Project: API Quản Lý Công Việc (To-do List)
Đây là một project API backend xây dựng bằng Spring Boot. Project tập trung vào việc xử lý logic CRUD cho các công việc (tasks) và bảo mật các endpoint bằng Spring Security và JWT.

## Các tính năng chính
* **Xác thực:** Đăng ký user mới (mã hóa mật khẩu bằng BCrypt) và đăng nhập (trả về JWT Token).
* **Phân quyền:** Các API quản lý công việc được bảo vệ. User chỉ có thể xem, sửa, hoặc xóa các công việc do chính họ tạo ra.
* **CRUD:** Cung cấp đầy đủ API cho 2 đối tượng `User` và `Task`.

## Công nghệ sử dụng
* Java 17
* Spring Boot 3 (Spring Web, Spring Security, Spring Data JPA)
* JWT (JSON Web Token)
* Microsoft SQL Server
* Maven

## Hướng dẫn API
### 1. Authentication Endpoints (Public)
**`POST /api/auth/register`**
* Đăng ký tài khoản mới.
* **Request Body:**
    ```json
    {
     "username": "user_test",
     "password": "password123",
     "email": "test@gmail.com"
    }
    ```
**`POST /api/auth/login`**
* Đăng nhập để nhận JWT Token.
* **Request Body:**
    ```json
    {
     "username": "user_test",
     "password": "password123",
     "email": "test@gmail.com"
    }
    ```
* **Response (Success):**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyX3Rlc3QiLCJpYXQiOjE3NjI2ODUwMjIsImV4cCI6MTc2Mjc3MTQyMn0.EG8MHsDPtJuFV44PvwzkSA_8_J4bjPOBA2WCvh7MLxY"
    }
    ```
### 2. Task Endpoints (Protected)
**Yêu cầu:** Tất cả các request này phải gửi kèm `Bearer Token` trong `Authorization` header.
**`POST /api/tasks`**
* Tạo một công việc mới cho user đang đăng nhập.
* **Request Body:**
    ```json
    {
      "title": "Hoàn thành project",
      "description": "Nộp CV trước 10 ngày",
      "status": "PENDING"
    }
    ```
**`GET /api/tasks`**
* Lấy tất cả công việc của user đang đăng nhập.
**`GET /api/tasks/{id}`**
* Lấy chi tiết 1 công việc theo ID.
**`PUT /api/tasks/{id}`**
* Cập nhật 1 công việc. Chỉ chủ sở hữu mới có quyền này.
**`DELETE /api/tasks/{id}`**
* Xóa 1 công việc. Chỉ chủ sở hữu mới có quyền này.

## Cài đặt
1.  Clone project: `git clone [https://github.com/LeTuanAnh210/QuanLyCongViec.git]`
2.  Mở file `src/main/resources/application.properties`.
3.  Cấu hình lại `spring.datasource.url`, `username`, `password` cho CSDL SQL Server của bạn.
4.  Chạy file `QuanLyCongViecApplication.java` để khởi động server.