package Ecom.Controller;

import Ecom.Model.Address;
import Ecom.Service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/customer-addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/{userId}")
    public ResponseEntity<Address> addAddressToUser(
            @PathVariable Integer userId, @Valid @RequestBody Address address) {
        Address addedAddress = addressService.addAddressToUser(userId, address);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<Address> updateAddress( @Valid @RequestBody Address address,@PathVariable Integer addressId) {
        Address updatedAddress = addressService.updateAddress(address,addressId);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<String> removeAddress(@PathVariable Integer addressId) {
        addressService.removeAddress(addressId);
        return ResponseEntity.ok("Address removed successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Address>> getAllUserAddress(@PathVariable Integer userId) {
        List<Address> allUserAddress = addressService.getAllUserAddress(userId);
        return ResponseEntity.ok(allUserAddress);
    }

}
