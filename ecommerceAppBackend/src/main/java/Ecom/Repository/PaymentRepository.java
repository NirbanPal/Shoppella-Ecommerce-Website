package Ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecom.Model.Payment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    boolean existsByIdempotencyKey(String idempotencyKey);
}
