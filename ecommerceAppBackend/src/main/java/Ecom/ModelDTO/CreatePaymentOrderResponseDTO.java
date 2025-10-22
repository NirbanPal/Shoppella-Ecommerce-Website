package Ecom.ModelDTO;

public record CreatePaymentOrderResponseDTO(
        String razorpayOrderId,
        Integer amountPaise,
        String currency,
        String razorpayKeyId
) {}
