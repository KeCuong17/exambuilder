# ExamBuilder — Website soạn đề thi trắc nghiệm (MVP)

Dự án Java Spring Boot để học cách xây dựng website có tài khoản, lưu đề thi vào cơ sở dữ liệu và triển khai lên Internet.

## 1. Có những chức năng nào?

- Đăng ký tài khoản và đăng nhập bằng Spring Security (mật khẩu BCrypt).
- Tạo đề trắc nghiệm: tên đề, môn học, tối đa 30 câu; mỗi câu có A/B/C/D và đáp án đúng.
- Xem danh sách đề đã tạo, sửa, xóa và in đề (đáp án được ẩn khi in).
- Người dùng chỉ được xem/sửa/xóa đề của chính mình.
- Database H2 lưu vào thư mục `data/` trên máy, không mất khi dừng chạy ứng dụng.
- Khi deploy, đổi sang PostgreSQL bằng biến môi trường (không phải sửa code Java).

Đây là bản học tập, chưa có AI, chưa có làm bài thi và chấm điểm, chưa xuất Word.

## 2. Cần cài gì trên máy Windows?

1. **JDK 25 (LTS)** (kiểm tra bằng `java -version`).
2. **Apache Maven 3.6.3+** (kiểm tra bằng `mvn -version`).
3. **VS Code** kèm Extension Pack for Java (hoặc IntelliJ IDEA).

## 3. Chạy trên máy

Mở Terminal trong thư mục chứa `pom.xml`, rồi chạy:

```powershell
mvn spring-boot:run
```

Mở trang: http://localhost:8080

Thử nghiệm: vào **Đăng ký** > tạo tài khoản > **Đăng nhập** > **Tạo đề mới** > điền 1-2 câu > **Lưu đề**.

Nếu port 8080 đang bị dùng, hãy chạy với:

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Dữ liệu lưu trong `data/exambuilder.mv.db` (không tải thư mục này lên GitHub).

## 4. Các thư mục chính

```text
src/main/java/com/example/exambuilder/
  config/        (Spring Security)
  model/         (UserAccount, Exam, Question = các bảng DB)
  repository/    (đọc ghi DB)
  service/       (xử lý tạo/sửa/xóa đề, kiểm tra người sở hữu)
  web/           (controller và DTO cho form)
src/main/resources/
  templates/     (giao diện HTML)
  static/css/    (CSS)
  static/js/     (nút thêm và xóa câu hỏi)
  application.properties  (cấu hình)
```

## 5. Đưa website online (GitHub + Neon + Render)

### A. Đẩy mã nguồn lên GitHub

Tạo repository `exambuilder`, đưa các file và thư mục trong gói này lên repository (không đưa thư mục `data/`, `target/` hay thông tin mật khẩu). Nếu dùng Git trên máy:

```bash
git init
git add .
git commit -m "ExamBuilder MVP"
git branch -M main
git remote add origin https://github.com/TEN_GITHUB_CUA_BAN/exambuilder.git
git push -u origin main
```

Thay `TEN_GITHUB_CUA_BAN` bằng username GitHub thật của bạn. Trước bước `git push`, tạo repo trống trên GitHub.

### B. Tạo database PostgreSQL trên Neon

1. Vào https://console.neon.tech/ và tạo project PostgreSQL.
2. Ở mục **Connect**, ghi lại hostname, database, user và password.
3. Đặt URL JDBC theo dạng:

```text
jdbc:postgresql://TEN_HOST.neon.tech/TEN_DATABASE?sslmode=require
```

Chuỗi kết nối lấy từ Neon có thể có dạng `postgresql://user:password@hostname/database?sslmode=require`; **không** dán nguyên chuỗi đó vào `DB_URL`. Đối với project này, `DB_URL` chỉ chứa JDBC URL/host/database, còn user và mật khẩu đặt ở hai biến riêng.

### C. Deploy trên Render

1. Vào https://dashboard.render.com/ > **New** > **Web Service**.
2. Kết nối repo GitHub vừa tạo.
3. Chọn **Language/Runtime = Docker**; file Dockerfile có sẵn ở thư mục gốc.
4. Chọn plan thích hợp (Free chỉ nên dùng thử nghiệm và có thể ngủ khi không truy cập).
5. Mở **Environment** và thêm các biến:

| Biến | Giá trị |
|---|---|
| `DB_URL` | `jdbc:postgresql://TEN_HOST.neon.tech/TEN_DATABASE?sslmode=require` |
| `DB_USER` | user của Neon |
| `DB_PASSWORD` | mật khẩu của Neon |
| `SPRING_PROFILES_ACTIVE` | `prod` (tùy chọn, hiện project không bắt buộc) |

6. Chọn **Deploy**. Khi hoàn thành, Render cấp địa chỉ dạng `https://ten-web.onrender.com`.

**Lưu ý quan trọng:** Không deploy bằng cấu hình H2 mặc định lên Render Free vì ổ đĩa của dịch vụ không bền vững, dữ liệu có thể bị mất. Hãy kết nối PostgreSQL trước. Không chia sẻ DB_PASSWORD hoặc để nó trong mã nguồn.

## 6. Tự kiểm tra sau khi chạy

- Truy cập trang chủ `/` khi chưa đăng nhập.
- Đăng ký một tài khoản và đăng nhập.
- Tạo một đề gồm 2 câu, xem lại rồi chỉnh sửa và lưu.
- Đăng xuất, xác nhận `/exams` yêu cầu đăng nhập lại.
- Tạo tài khoản thứ hai, xác nhận tài khoản này không xem được đề của tài khoản đầu (trả về 404).
- In đề từ nút **In đề thi**, kiểm tra đáp án đúng không xuất hiện trên bản in.

## 7. Ghi chú bảo mật và giới hạn

- Form đăng nhập dùng session cookie HttpOnly và CSRF protection mặc định của Spring Security.
- Password được hash bằng BCrypt. Không lưu password dạng văn bản thuần.
- ID đề thi luôn được truy vấn cùng tài khoản hiện tại; không thể chỉ đổi URL để xem đề của người khác.
- Đây là MVP phục vụ học tập: nếu triển khai cho người dùng thật, hãy bổ sung xác minh email, chống spam, giới hạn request, kiểm thử tự động, kiểm soát phiên, database migration (Flyway), sao lưu DB và giám sát lỗi.
- CSS tải font từ Google Fonts; nếu mạng bị chặn, trang vẫn hiển thị bằng font dự phòng.
- Nếu muốn hỗ trợ công thức, chèn ảnh, xuất Word hoặc AI, chúng ta sẽ bổ sung sau khi MVP ổn định.

## 8. Nếu gặp lỗi

- `mvn is not recognized`: Maven chưa cài hoặc chưa thêm vào PATH.
- `java is not recognized`: Chưa cài JDK/Java PATH.
- `Port 8080 was already in use`: Đổi port theo hướng dẫn phần 3.
- `Failed to configure a DataSource`: Xem lại biến `DB_URL`, `DB_USER`, `DB_PASSWORD`, đặc biệt dùng JDBC URL.
- `Whitelabel Error Page / 403` khi nộp form: kiểm tra trang có dùng `th:action` của Thymeleaf để chèn CSRF token.

Tài liệu tham khảo:
- Spring Security: https://spring.io/guides/gs/securing-web/
- Render Docker: https://render.com/docs/docker
- Render web services: https://render.com/docs/web-services
- Neon: https://neon.com/docs
