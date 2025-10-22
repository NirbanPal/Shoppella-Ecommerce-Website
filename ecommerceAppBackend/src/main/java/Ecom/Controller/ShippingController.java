package Ecom.Controller;

import Ecom.ModelDTO.ShippingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Ecom.Model.ShippingDetails;
import Ecom.ModelDTO.ShippingDTO;
import Ecom.Service.ShippingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ecom/order-shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/{orderId}/{shipperId}")
    public ResponseEntity<ShippingResponseDTO> setShippingDetails(@PathVariable Long orderId,
                                                                  @PathVariable Integer shipperId,
                                                                  @Valid @RequestBody ShippingDetails shippingDetails) {
        ShippingResponseDTO savedShippingDetails = shippingService.setShippingDetails(orderId, shipperId,
                shippingDetails);
        return new ResponseEntity<>(savedShippingDetails, HttpStatus.CREATED);
    }

    @PutMapping("/{shippingId}")
    public ResponseEntity<ShippingDetails> updateShippingAddress(@PathVariable Integer shippingId,
                                                                 @Valid @RequestBody ShippingDTO shippingDTO) {
        ShippingDetails updatedShippingDetails = shippingService.updateShippingAddress(shippingId, shippingDTO);
        return new ResponseEntity<>(updatedShippingDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{shippingId}")
    public ResponseEntity<Void> deleteShippingDetails(@PathVariable Integer shippingId) {
        shippingService.deleteShippingDetails(shippingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/assign-shipper/{shippingId}/{shipperId}")
    public ResponseEntity<ShippingDetails> assignShipper(@PathVariable Integer shippingId, @PathVariable Integer shipperId) {

        ShippingDetails assignedShipper = shippingService.assignShipper(shippingId,shipperId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(assignedShipper);
    }
}
