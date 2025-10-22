package Ecom.Controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import Ecom.ModelDTO.OrderResponseDTO;
import Ecom.ModelDTO.OrdersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecom.Model.Orders;
import Ecom.Service.OrdersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/orders")
public class OrderController {

    private final OrdersService ordersService;


    @PostMapping("/placed/{userid}/{addressId}")
    public ResponseEntity<?> addOrderToCart(@PathVariable("userid") Integer userid, @PathVariable("addressId") Integer addressId) {
        OrdersDTO placeOrder = ordersService.placeOrder(userid, addressId);
        return ResponseEntity.ok(placeOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrdersDetails(@PathVariable("orderId") Long orderId) {
        OrderResponseDTO order = ordersService.getOrdersDetails(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllUserOrder(@PathVariable Integer userId) {
        List<OrderResponseDTO> orders = ordersService.getAllUserOrder(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> viewAllOrders() {
        List<OrderResponseDTO> orders = ordersService.viewAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<OrderResponseDTO>> viewAllOrderByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<OrderResponseDTO> orders = ordersService.viewAllOrderByDate(date);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/{orderId}")
    public ResponseEntity<String> deleteOrders(@PathVariable Integer userId, @PathVariable Long orderId) {
        ordersService.deleteOrders(userId, orderId);
        return new ResponseEntity<>("Order successfully deleted.", HttpStatus.OK);
    }

}
