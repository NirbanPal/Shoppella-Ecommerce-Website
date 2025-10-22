package Ecom.ModelDTO;



import lombok.Data;

@Data
public class OrdersDTO {
    private Long orderId;
    private String status;
    private String orderDate;
    private Long orderAmount;
    private String paymentStatus;
}
