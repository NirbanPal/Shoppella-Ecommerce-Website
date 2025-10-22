package Ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecom.Model.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    boolean existsByIdempotencyKey(String idempotencyKey);
}
