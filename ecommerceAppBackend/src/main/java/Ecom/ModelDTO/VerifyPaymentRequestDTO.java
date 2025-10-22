package Ecom.ModelDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyPaymentRequestDTO(
        @NotNull Integer userId,
        @NotNull Long orderId,
        @NotBlank String razorpayOrderId,
        @NotBlank String razorpayPaymentId,
        @NotBlank String razorpaySignature
) {}
