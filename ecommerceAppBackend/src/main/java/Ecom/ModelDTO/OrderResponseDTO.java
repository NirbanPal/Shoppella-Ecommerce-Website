package Ecom.ModelDTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private String status;
    private LocalDateTime orderDate;
    private Long orderAmount;
    private ShippingDTO shippingDetails;
}
