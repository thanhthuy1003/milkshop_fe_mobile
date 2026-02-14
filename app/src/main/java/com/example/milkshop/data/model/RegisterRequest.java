package com.example.milkshop.data.model;

/**
 * Lớp model đại diện cho yêu cầu đăng ký tài khoản người dùng mới.
 * Khớp với schema của API POST /api/authentication/sign-up.
 */
public class RegisterRequest {
    /** Tên đăng nhập duy nhất của người dùng */
    private String username;
    
    /** Họ của người dùng */
    private String firstName;
    
    /** Tên của người dùng */
    private String lastName;
    
    /** Số điện thoại liên lạc (phải là số điện thoại Việt Nam hợp lệ) */
    private String phoneNumber;
    
    /** Địa chỉ email để xác thực và liên lạc */
    private String email;
    
    /** Mật khẩu đăng nhập */
    private String password;
    
    /** Xác nhận lại mật khẩu để tránh sai sót */
    private String confirmPassword;

    /**
     * Khởi tạo một đối tượng RegisterRequest với đầy đủ thông tin.
     *
     * @param username        Tên đăng nhập.
     * @param firstName       Họ.
     * @param lastName        Tên.
     * @param phoneNumber     Số điện thoại.
     * @param email           Email.
     * @param password        Mật khẩu.
     * @param confirmPassword Xác nhận mật khẩu.
     */
    public RegisterRequest(String username, String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    /** @return Tên đăng nhập */
    public String getUsername() { return username; }
    /** @param username Tên đăng nhập mới */
    public void setUsername(String username) { this.username = username; }

    /** @return Họ của người dùng */
    public String getFirstName() { return firstName; }
    /** @param firstName Họ mới */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return Tên của người dùng */
    public String getLastName() { return lastName; }
    /** @param lastName Tên mới */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return Số điện thoại */
    public String getPhoneNumber() { return phoneNumber; }
    /** @param phoneNumber Số điện thoại mới */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /** @return Địa chỉ email */
    public String getEmail() { return email; }
    /** @param email Email mới */
    public void setEmail(String email) { this.email = email; }

    /** @return Mật khẩu */
    public String getPassword() { return password; }
    /** @param password Mật khẩu mới */
    public void setPassword(String password) { this.password = password; }

    /** @return Mật khẩu xác nhận */
    public String getConfirmPassword() { return confirmPassword; }
    /** @param confirmPassword Mật khẩu xác nhận mới */
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
