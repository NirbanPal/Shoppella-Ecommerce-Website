package Ecom.ModelDTO;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ShippingResponseDTO {
    private String address;

    private String city;

    private String state;

    private String postalCode;

    private ShipperResponseDTO shipper;

    private OrderResponseDTO orders;
}
