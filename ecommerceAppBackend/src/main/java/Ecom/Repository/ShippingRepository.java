package Ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecom.Model.ShippingDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingDetails, Integer> {

}
