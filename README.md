# Milkshop Mobile Application

Dự án ứng dụng di động dành cho hệ thống **Milkshop**, hỗ trợ quản lý mua bán và điều phối giao hàng.

## 🎨 Phong cách thiết kế
*   **Màu sắc chủ đạo**: Xanh dương nhạt (Light Blue).
*   **Giao diện**: Thân thiện, hiện đại, tối ưu hóa trải nghiệm người dùng trên thiết bị di động.

## 👥 Các vai trò trong ứng dụng (Actors)
Ứng dụng được thiết kế cho 4 đối tượng chính (Admin quản lý trên Web):

### 1. Guest (Khách chưa đăng nhập)
*   **Xác thực**: Đăng ký, đăng nhập (thường & Google), quên mật khẩu, xác thực tài khoản qua Email.
*   **Khám phá**: Xem danh sách sản phẩm, tìm kiếm, xem chi tiết sản phẩm và các bài viết (Posts).
*   **Đánh giá**: Xem các đánh giá của người dùng khác về sản phẩm.

### 2. Buyer (Người mua hàng)
*   **Quản lý tài khoản**: Cập nhật thông tin cá nhân, thay đổi username/mật khẩu, quản lý địa chỉ nhận hàng.
*   **Giỏ hàng**: Thêm/sửa/xóa sản phẩm, áp dụng mã giảm giá (Voucher) và tích điểm.
*   **Thanh toán**: Đặt hàng (Checkout), đặt hàng trước (Preorder), tích hợp thanh toán trực tuyến.
*   **Đơn hàng**: Theo dõi lịch sử đơn hàng, chi tiết đơn hàng, hủy đơn hàng (khi ở trạng thái chờ).
*   **Tương tác**: Đánh giá sản phẩm đã mua, gửi báo cáo/khiếu nại (Reports).

### 3. Seller (Nhân viên/Chủ shop)
*   **Thống kê (Dashboard)**: Xem biểu đồ doanh thu, thống kê đơn hàng, sản phẩm bán chạy, khách hàng mới.
*   **Quản lý sản phẩm**: Thêm mới, cập nhật thông tin sản phẩm, quản lý trạng thái Preorder, quản lý ảnh sản phẩm.
*   **Quản lý kho**: Quản lý thương hiệu (Brands), đơn vị tính (Units), danh mục (Categories) và thuộc tính sản phẩm (Attributes).
*   **Quản lý đơn hàng**: Cập nhật trạng thái đơn hàng (Pending -> Processing -> Shipped), hủy đơn hàng.
*   **Nội dung & Khuyến mãi**: Quản lý bài viết (Posts) và các chương trình giảm giá (Vouchers).

### 4. Shipping (Người giao hàng/Đối tác vận chuyển)
*   **Vận chuyển (GHN)**: Tra cứu tỉnh/thành, quận/huyện, phường/xã, tính phí vận chuyển.
*   **Điều phối**: Tạo đơn vận chuyển trên hệ thống GHN, xem bản xem trước đơn hàng, theo dõi hành trình (tracking).
*   **Giao hàng**: Cập nhật trạng thái đơn hàng sang "Đã giao hàng" (Delivered).

## 🛠 Tích hợp hệ thống (Swagger API)
Ứng dụng tích hợp đầy đủ các Endpoint từ hệ thống Backend bao gồm:
*   `Authentication`: Quản lý định danh và truy cập.
*   `User/Account`: Thông tin cá nhân, địa chỉ, lịch sử mua hàng.
*   `Product/Brand/Category/Unit`: Hệ thống dữ liệu sản phẩm phong phú.
*   `Cart/Checkout/Payment`: Quy trình mua hàng và thanh toán khép kín.
*   `Dashboard`: Hệ thống báo cáo và thống kê chuyên sâu.
*   `Shipping (GHN)`: Tích hợp trực tiếp với dịch vụ Giao Hàng Nhanh.
*   `Voucher/Report/Post`: Các tính năng hỗ trợ marketing và CSKH.

## 🚀 Công nghệ sử dụng (Dự kiến)
*   **Framework**: [Flutter / React Native / Native Android]
*   **Quản lý trạng thái**: [Bloc / Redux / Provider]
*   **Kết nối API**: [Dio / Retrofit]
*   **Lưu trữ**: [Shared Preferences / SQLite]

---
*Dự án đang trong quá trình phát triển.*
