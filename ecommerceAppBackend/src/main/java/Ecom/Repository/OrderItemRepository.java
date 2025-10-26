package Ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecom.Model.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
