package Ecom.ServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import Ecom.Exception.OrdersException;
import Ecom.Exception.PaymentException;
import Ecom.ModelDTO.CreatePaymentOrderRequestDTO;
import Ecom.ModelDTO.CreatePaymentOrderResponseDTO;
import Ecom.ModelDTO.VerifyPaymentRequestDTO;
import Ecom.ModelDTO.VerifyPaymentResponseDTO;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import Ecom.Enum.OrderStatus;
import Ecom.Enum.PaymentMethod;
import Ecom.Enum.PaymentStatus;

import Ecom.Model.Orders;
import Ecom.Model.Payment;
import Ecom.Repository.OrderRepository;
import Ecom.Repository.PaymentRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.PaymentService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private OrderRepository ordersRepo;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    @Value("${razorpay.webhook.secret}")
    private String razorpayWebHookSecret;


    @Override
    @Transactional
    public CreatePaymentOrderResponseDTO createRazorpayOrder(CreatePaymentOrderRequestDTO req, String idemKey, Long userId) {
        Orders order = ordersRepo.findById(req.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));


        if (order.getTotalAmount() * 100 != req.amountPaise()) {
            throw new IllegalArgumentException("Amount mismatch with Order total");
        }
        if (!Objects.equals(order.getCurrency(), req.currency())) {
            throw new IllegalArgumentException("Currency mismatch with Order");
        }


        if (idemKey != null && paymentRepo.existsByIdempotencyKey(idemKey)) {
            Payment p = paymentRepo.findAll().stream()
                    .filter(pay -> idemKey.equals(pay.getIdempotencyKey()))
                    .findFirst().orElseThrow();
            return new CreatePaymentOrderResponseDTO(p.getRazorpayOrderId(), p.getPaymentAmountPaise(), p.getCurrency(), razorpayKeyId);
        }


        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpaySecret);
            JSONObject options = new JSONObject();
            options.put("amount", req.amountPaise());
            options.put("currency", req.currency());
            options.put("receipt", "ord_" + order.getOrderId() + "_" + System.currentTimeMillis());
            options.put("payment_capture", 1); // auto-capture


            Order rzpOrder = client.orders.create(options);

            Payment payment = Payment.builder()
                    .order(order)
                    .user(order.getUser())
                    .razorpayOrderId(rzpOrder.get("id"))
                    .paymentAmountPaise((Integer) rzpOrder.get("amount"))
                    .currency(rzpOrder.get("currency"))
                    .paymentDate(LocalDateTime.now())
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentMethod(PaymentMethod.RAZORPAY)
                    .idempotencyKey(idemKey)
                    .build();
            paymentRepo.save(payment);


            order.setStatus(OrderStatus.PENDING_PAYMENT);

            order.getPayments().add(payment);

            order.getUser().getPayments().add(payment);


            return new CreatePaymentOrderResponseDTO(payment.getRazorpayOrderId(), payment.getPaymentAmountPaise(), payment.getCurrency(), razorpayKeyId);
        } catch (RazorpayException e) {
            throw new IllegalStateException("Failed to create Razorpay order", e);
        }
    }

    @Override
    public VerifyPaymentResponseDTO verifyPayment(VerifyPaymentRequestDTO req) {
        Payment payment = paymentRepo.findByRazorpayOrderId(req.razorpayOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if( !payment.getUser().getUserId().equals(req.userId()) || !Objects.equals(payment.getOrder().getOrderId(), req.orderId()))
            throw new PaymentException("Verification Mismatch");

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
            return new VerifyPaymentResponseDTO(true, "Already verified", payment.getOrder().getStatus().name());
        }


        try {
            String data = req.razorpayOrderId() + "|" + req.razorpayPaymentId();
            String expectedSignature = hmacSHA256(data, razorpaySecret);


            if (!expectedSignature.equals(req.razorpaySignature())) {
                payment.setPaymentStatus(PaymentStatus.FAILED);
                paymentRepo.save(payment);
                return new VerifyPaymentResponseDTO(false, "Signature mismatch", payment.getOrder().getStatus().name());
            }


            payment.setRazorpayPaymentId(req.razorpayPaymentId());
            payment.setRazorpaySignature(req.razorpaySignature());
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepo.save(payment);


            Orders order = payment.getOrder();
            order.setStatus(OrderStatus.PAID);
            ordersRepo.save(order);


            return new VerifyPaymentResponseDTO(true, "Payment verified", order.getStatus().name());
        } catch (Exception e) {
            throw new IllegalStateException("Verification error", e);
        }
    }

    @Override
    public Map<String,Object> getOrderStatus(Long orderId) {
        try {
            System.out.println("*****************************************************");
            Orders order = ordersRepo.findById(orderId).orElseThrow(() -> new OrdersException("Order not found."));
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("orderId", order.getOrderId());
            resp.put("orderStatus", order.getStatus().name());
            resp.put("totalAmountPaise", order.getTotalAmount() * 100);
            resp.put("currency", order.getCurrency());
            resp.put("payments", order.getPayments()
                    .stream().map(p -> Map.of(
                        "paymentId" ,p.getPaymentId(),
                    "razorpayOrderId", p.getRazorpayOrderId(),
                    "paymentStatus", p.getPaymentStatus(),
                    "amountPaise", p.getPaymentAmountPaise(),
                    "currency", p.getCurrency()
            )).toList());
            System.out.println(resp);
            return resp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Map.of();
    }


    private static String hmacSHA256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : raw) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("HMAC calc failed", e);
        }
    }

}
