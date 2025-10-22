package Ecom.Service;

import Ecom.Exception.ShippingException;
import Ecom.Model.ShippingDetails;
import Ecom.ModelDTO.ShippingDTO;
import Ecom.ModelDTO.ShippingResponseDTO;

public interface ShippingService {
	
  public ShippingResponseDTO setShippingDetails(Long orderId, Integer shipperId, ShippingDetails shippingDetails) throws ShippingException;
  
  public ShippingDetails updateShippingAddress(Integer shippingId,ShippingDTO shippingDTO) throws ShippingException;
  
  public void deleteShippingDetails(Integer shippingId)throws ShippingException;

  ShippingDetails setShippingDetailsWhileOrdering(Long orderId, Integer addressId) throws ShippingException;

  ShippingDetails assignShipper(Integer shippingId, Integer shipperId) throws ShippingException;

}
