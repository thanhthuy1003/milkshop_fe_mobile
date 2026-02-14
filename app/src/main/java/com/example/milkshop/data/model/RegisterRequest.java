package com.example.milkshop.data.model;

/**
 * Lớp model đại diện cho yêu cầu đăng ký tài khoản người dùng mới.
 * Khớp với schema của API POST /api/authentication/sign-up.
 */
public class RegisterRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String confirmPassword;

    /**
     * Khởi tạo một yêu cầu đăng ký mới.
     *
     * @param username        Tên đăng nhập duy nhất.
     * @param firstName       Họ của người dùng.
     * @param lastName        Tên của người dùng.
     * @param phoneNumber     Số điện thoại liên lạc.
     * @param email           Địa chỉ email để xác thực.
     * @param password        Mật khẩu đăng nhập.
     * @param confirmPassword Xác nhận lại mật khẩu (phải khớp với password).
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

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
