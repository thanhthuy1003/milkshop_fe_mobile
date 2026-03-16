package com.example.milkshop.data.model;

/**
 * Model cho yêu cầu thanh toán đơn hàng.
 */
public class CheckoutRequest {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private String paymentMethod; // e.g., "COD", "PAYOS"
    private String voucherCode;
    private int usePoint;

    public CheckoutRequest(String receiverName, String receiverPhone, String receiverAddress, String note, String paymentMethod, String voucherCode, int usePoint) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.voucherCode = voucherCode;
        this.usePoint = usePoint;
    }

    // Getters and Setters
    public String getReceiverName() { return receiverName; }
    public String getReceiverPhone() { return receiverPhone; }
    public String getReceiverAddress() { return receiverAddress; }
    public String getPaymentMethod() { return paymentMethod; }
}
