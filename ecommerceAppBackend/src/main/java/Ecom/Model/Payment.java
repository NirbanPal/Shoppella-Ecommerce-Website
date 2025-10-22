package Ecom.Model;

import Ecom.Enum.PaymentMethod;
import Ecom.Enum.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@NoArgsConstructor   // generates default constructor
@AllArgsConstructor  // generates parameterized constructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_rzp_order", columnList = "razorpay_order_id", unique = true),
        @Index(name = "idx_payment_rzp_payment", columnList = "razorpay_payment_id"),
        @Index(name = "idx_payment_user", columnList = "user_id"),
        @Index(name = "idx_payment_status", columnList = "payment_status")
})
@Check(constraints = "payment_amount_paise >= 100")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;


    @Column(name = "razorpay_order_id", nullable = false, unique = true, length = 40)
    private String razorpayOrderId;


    @Column(name = "razorpay_payment_id", length = 40)
    private String razorpayPaymentId;


    @Column(name = "razorpay_signature", length = 256)
    private String razorpaySignature;


    @Column(name = "payment_amount_paise", nullable = false)
    private Integer paymentAmountPaise;


    @Column(name = "currency", nullable = false, length = 10)
    private String currency = "INR";


    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod = PaymentMethod.RAZORPAY;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;


    @Column(name = "idempotency_key", length = 64)
    private String idempotencyKey;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
}

