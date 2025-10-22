package Ecom.ModelDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingDTO {

	private String address;

	private String city;

	private String state;

	private String postalCode;
}
