package Ecom.ModelDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentOrderRequestDTO(
        @NotNull Long orderId,
        @NotNull @Positive Integer amountPaise,
        @NotBlank String currency
) {}
