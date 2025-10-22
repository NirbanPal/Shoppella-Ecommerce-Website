package Ecom.ModelDTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipperResponseDTO {

    private Integer shipperId;

    private String name;

    private String phoneNumber;
}
