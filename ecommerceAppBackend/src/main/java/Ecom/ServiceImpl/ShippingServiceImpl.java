package Ecom.ServiceImpl;

import Ecom.Exception.AddressException;
import Ecom.Exception.OrdersException;
import Ecom.Model.Address;
import Ecom.ModelDTO.OrderResponseDTO;
import Ecom.ModelDTO.ShipperResponseDTO;
import Ecom.ModelDTO.ShippingResponseDTO;
import Ecom.Repository.AddressRepository;
import org.springframework.stereotype.Service;

import Ecom.Exception.ShippingException;
import Ecom.Model.Orders;
import Ecom.Model.Shipper;
import Ecom.Model.ShippingDetails;
import Ecom.ModelDTO.ShippingDTO;
import Ecom.Repository.OrderRepository;
import Ecom.Repository.ShipperRepository;
import Ecom.Repository.ShippingRepository;
import Ecom.Service.ShippingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private final ShippingRepository shippingRepository;

	private final OrderRepository orderRepository;

	private final ShipperRepository shipperRepository;

	private final AddressRepository addressRepo;

	@Override
	public ShippingResponseDTO setShippingDetails(Long orderId, Integer shipperId, ShippingDetails shippingDetails)
	        throws ShippingException {
	    if (shippingDetails == null)
	        throw new ShippingException("ShippingDetails cannot be null");

	    Orders existingOrder = orderRepository.findById(orderId)
	            .orElseThrow(() -> new ShippingException("Order not found"));

	    Shipper existingShipper = shipperRepository.findById(shipperId)
	            .orElseThrow(() -> new ShippingException("Shipper not found"));

	    shippingDetails.setOrders(existingOrder);
	    shippingDetails.setShipper(existingShipper);
	    ShippingDetails sd=shippingRepository.save(shippingDetails);

	    existingOrder.setShippingDetails(shippingDetails);
	    orderRepository.save(existingOrder);



	    return ShippingResponseDTO.builder().address(sd.getAddress()).city(sd.getCity()).state(sd.getState()).postalCode(sd.getPostalCode()).shipper(ShipperResponseDTO.builder().shipperId(sd.getShipper().getShipperId()).name(sd.getShipper().getName()).phoneNumber(sd.getShipper().getPhoneNumber()).build()).orders(OrderResponseDTO.builder().orderId(sd.getOrders().getOrderId()).orderDate(sd.getOrders().getOrderDate()).orderAmount(sd.getOrders().getTotalAmount()).status(sd.getOrders().getStatus().toString()).build()).build();
	}


	@Override
	public ShippingDetails updateShippingAddress(Integer shippingId, ShippingDTO shippingDTO) throws ShippingException {
		ShippingDetails existing = shippingRepository.findById(shippingId)
				.orElseThrow(() -> new ShippingException("Shipping detail not found"));

		existing.setState(shippingDTO.getState());
		existing.setAddress(shippingDTO.getAddress());
		existing.setCity(shippingDTO.getCity());
		existing.setPostalCode(shippingDTO.getPostalCode());
		return shippingRepository.save(existing);
	}

	@Override
	public void deleteShippingDetails(Integer shippingId) throws ShippingException {
		ShippingDetails existing = shippingRepository.findById(shippingId)
				.orElseThrow(() -> new ShippingException("Shipping detail not found"));

		shippingRepository.delete(existing);

	}

	@Override
	public ShippingDetails setShippingDetailsWhileOrdering(Long orderId, Integer addressId){
		Orders existingOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrdersException("Order detail not found"));

		Address existingAddress = addressRepo.findById(addressId)
				.orElseThrow(() -> new AddressException("Address detail not found"));

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setAddress("Flat : "+existingAddress.getFlatNo()+" Street : "+existingAddress.getStreet());
		shippingDetails.setCity(existingAddress.getCity());
		shippingDetails.setPostalCode(existingAddress.getZipCode());
		shippingDetails.setState(existingAddress.getState());
		shippingDetails.setOrders(existingOrder);
		existingOrder.setShippingDetails(shippingDetails);

		return shippingRepository.save(shippingDetails);

	}


	@Override
	public ShippingDetails assignShipper(Integer shippingId, Integer shipperId){
		ShippingDetails existingShipping = shippingRepository.findById(shippingId)
				.orElseThrow(() -> new OrdersException("Shipping details not found"));

		Shipper shipper = shipperRepository.findById(shipperId)
				.orElseThrow(() -> new OrdersException("Shipper details not found"));

		existingShipping.setShipper(shipper);
		shipper.getShippingDetails().add(existingShipping);

		return shippingRepository.save(existingShipping);

	}


}
