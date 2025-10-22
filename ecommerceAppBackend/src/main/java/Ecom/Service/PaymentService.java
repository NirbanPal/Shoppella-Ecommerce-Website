package Ecom.Service;

import Ecom.Exception.PaymentException;
import Ecom.Model.Payment;
import Ecom.ModelDTO.CreatePaymentOrderRequestDTO;
import Ecom.ModelDTO.CreatePaymentOrderResponseDTO;
import Ecom.ModelDTO.VerifyPaymentRequestDTO;
import Ecom.ModelDTO.VerifyPaymentResponseDTO;

import java.util.Map;

public interface PaymentService {


	CreatePaymentOrderResponseDTO createRazorpayOrder(CreatePaymentOrderRequestDTO req, String idemKey, Long userId) throws PaymentException;

	VerifyPaymentResponseDTO verifyPayment(VerifyPaymentRequestDTO req) throws PaymentException;

	Map<String,Object> getOrderStatus(Long orderId) throws PaymentException;

}
