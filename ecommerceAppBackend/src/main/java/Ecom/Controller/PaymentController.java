package Ecom.Controller;


import Ecom.ModelDTO.CreatePaymentOrderRequestDTO;
import Ecom.ModelDTO.CreatePaymentOrderResponseDTO;
import Ecom.ModelDTO.VerifyPaymentRequestDTO;
import Ecom.ModelDTO.VerifyPaymentResponseDTO;
import Ecom.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/order-payments")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/order")
	public ResponseEntity<CreatePaymentOrderResponseDTO> createOrder(
			@RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
			@RequestHeader(value = "X-User-Id", required = false) Long userId,
			@RequestBody @Valid CreatePaymentOrderRequestDTO req) {
		return ResponseEntity.ok(paymentService.createRazorpayOrder(req, idemKey, userId));
	}


	@PostMapping("/verify")
	public ResponseEntity<VerifyPaymentResponseDTO> verify(@RequestBody @Valid VerifyPaymentRequestDTO req) {
		return ResponseEntity.ok(paymentService.verifyPayment(req));
	}


	@GetMapping("/orders/{orderId}")
	public ResponseEntity<Map<String,Object>> getOrderStatus(@PathVariable Long orderId) {
		return ResponseEntity.ok(paymentService.getOrderStatus(orderId));
	}

}
