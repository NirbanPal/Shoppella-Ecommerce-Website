package Ecom.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import Ecom.ModelDTO.OrderResponseDTO;
import org.springframework.stereotype.Service;

import Ecom.Exception.OrdersException;
import Ecom.Model.Orders;
import Ecom.ModelDTO.OrdersDTO;

public interface OrdersService {
	
	public OrdersDTO placeOrder(Integer userId, Integer addressId) throws OrdersException;
	
	public Orders updateOrders(Long ordersid,OrdersDTO orderDTo) throws OrdersException;
	
	public OrderResponseDTO getOrdersDetails(Long orderid) throws OrdersException;
	
	public List<OrderResponseDTO> getAllUserOrder(Integer userId) throws OrdersException;
	
	public List<OrderResponseDTO> viewAllOrders() throws OrdersException;
	
	public List<OrderResponseDTO> viewAllOrderByDate(LocalDate date) throws OrdersException;
	
	public void deleteOrders(Integer userId,Long Orderid) throws OrdersException;

}
