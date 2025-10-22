package Ecom.ModelDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {

	    private int productId;
	    private int quantity;
		private String ProductName;
	    private Long price;
	    
}
