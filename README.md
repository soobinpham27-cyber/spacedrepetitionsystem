# 🧠 Spaced Repetition System (Ứng dụng Học tập qua Flashcard)

![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white)
![Room](https://img.shields.io/badge/Room_DB-3DDC84?style=for-the-badge&logo=android&logoColor=white)

Đây là đồ án cuối kỳ môn **Lập trình Di động**, Trường Đại học Sư phạm Đà Nẵng (UED). Ứng dụng giúp người dùng học từ vựng hiệu quả thông qua thẻ ghi nhớ (Flashcard) kết hợp thuật toán lặp lại ngắt quãng (Spaced Repetition).

---

## 🚀 Tính năng nổi bật

- **Thuật toán SuperMemo-2 (SM-2):** Tự động phân tích mức độ ghi nhớ của người dùng để lên lịch ôn tập tối ưu cho từng thẻ.
- **Offline-First & Cloud Sync:** Hoạt động mượt mà không cần mạng nhờ cơ sở dữ liệu cục bộ (Room DB). Tự động đồng bộ hóa tiến độ học tập lên đám mây (Firebase Firestore) khi có kết nối.
- **Giao diện 3D FlipCard:** Trải nghiệm lật thẻ trực quan, mượt mà được xây dựng hoàn toàn bằng **Jetpack Compose**.
- **Google Text-to-Speech (TTS):** Hỗ trợ phát âm tự động mặt từ vựng tiếng Anh và mặt nghĩa tiếng Việt.
- **Tìm kiếm thông minh:** Lọc thẻ theo thời gian thực (Real-time Search) sử dụng `StateFlow` và `debounce`.

---

## 🏗 Kiến trúc Hệ thống & Công nghệ

Dự án áp dụng chặt chẽ kiến trúc **Clean Architecture** kết hợp mô hình **MVVM**, chia thành 3 tầng riêng biệt: `Domain`, `Data`, và `UI`.

- **Ngôn ngữ:** 100% Kotlin.
- **Giao diện (UI):** Jetpack Compose, Material Design 3.
- **Bất đồng bộ:** Coroutines & Flow / StateFlow.
- **Dependency Injection:** Dagger Hilt.
- **Database cục bộ:** Room Database.
- **Backend / BaaS:** Firebase Authentication (Anonymous Login) & Cloud Firestore.

---

## 🗄 Cấu trúc Cơ sở dữ liệu (Database Schema)

Thực thể chính `FlashcardEntity` được đồng bộ nhất quán giữa Room DB và Firestore:

| Thuộc tính | Kiểu dữ liệu | Mô tả |
| :--- | :--- | :--- |
| `id` | String (UUID) | Khóa chính (Primary Key), mã định danh duy nhất của thẻ. |
| `userId` | String | ID người dùng ẩn danh từ Firebase Auth để bảo mật dữ liệu. |
| `front` | String | Nội dung mặt trước (Ví dụ: Từ vựng tiếng Anh). |
| `back` | String | Nội dung mặt sau (Ví dụ: Nghĩa tiếng Việt). |
| `easiness` | Double | Hệ số độ dễ (Easiness Factor) của thuật toán SM-2 (Mặc định: 2.5). |
| `interval` | Int | Khoảng thời gian (số ngày) cho lần ôn tập tiếp theo. |
| `repetitions` | Int | Số lần trả lời đúng liên tiếp. |
| `nextReviewDate` | Long | Timestamp (ms) chỉ định thời điểm thẻ đến hạn cần ôn tập. |

---

## 🔄 Luồng dữ liệu (Data Flow)

Ứng dụng xử lý dữ liệu theo cơ chế **Offline-First** để tối ưu UX và hiệu năng:

1. **Đọc dữ liệu:** Giao diện (UI) luôn quan sát (collect) trực tiếp dòng dữ liệu (Flow) từ Room DB cục bộ thông qua ViewModel.
2. **Ghi dữ liệu (Thêm/Sửa/Xóa):** - Thao tác được ghi ngay lập tức vào Room DB cục bộ -> UI phản hồi ngay tức thì.
   - Đồng thời, một Coroutine ngầm sẽ khởi chạy để đẩy `Entity` tương ứng lên `Firebase Firestore` để lưu trữ đám mây.
3. **Đồng bộ hóa thiết bị (Sync):** Khi người dùng mở app trên thiết bị mới, hàm `syncWithBackend(userId)` sẽ truy vấn Firestore, kéo toàn bộ thẻ về và cập nhật lại vào Room DB.

---

## 🤝 Thành viên & Phân công công việc

Dự án được quản lý mã nguồn nghiêm ngặt theo **Git Flow** (sử dụng Feature Branches, tạo Pull Request và bắt buộc Peer-review trước khi merge vào nhánh `develop`).

| STT | Họ và Tên | Mã Sinh Viên | Vai trò & Tỷ lệ đóng góp |
| :---: | :--- | :--- | :--- |
| 1 | **Phạm Hoàng Lĩnh** | 3120223104 | **Backend & Logic (50%)**<br>- Thiết kế kiến trúc MVVM.<br>- Setup Room DB & Firebase Firestore.<br>- Cài đặt thuật toán SM-2 & Logic đồng bộ. |
| 2 | **Nguyễn Anh Mạnh** | 3120223113 | **UI/UX & Media (50%)**<br>- Xây dựng giao diện bằng Jetpack Compose.<br>- Tích hợp Google TTS & StateFlow.<br>- Thiết kế báo cáo & Quay video Demo. |

---

