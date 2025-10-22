package Ecom.ModelDTO;

public record VerifyPaymentResponseDTO(
        boolean success,
        String message,
        String orderStatus
) {}
