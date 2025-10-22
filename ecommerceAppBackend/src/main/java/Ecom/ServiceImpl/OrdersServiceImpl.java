package Ecom.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import Ecom.Enum.PaymentStatus;
import Ecom.Exception.AddressException;
import Ecom.Model.*;
import Ecom.ModelDTO.OrderItemDTO;
import Ecom.ModelDTO.OrderResponseDTO;
import Ecom.ModelDTO.ShippingDTO;
import Ecom.Repository.*;
import Ecom.Service.AddressService;
import Ecom.Service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import Ecom.Enum.OrderStatus;
import Ecom.Exception.OrdersException;
import Ecom.Exception.UserException;
import Ecom.ModelDTO.OrdersDTO;
import Ecom.Service.OrdersService;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;

    private final CartRepository cartRepository;

    private final AddressRepository addressRepository;

    private final ShippingService shippingService;


    @Override
    public OrdersDTO placeOrder(Integer userId,Integer addressId) throws OrdersException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Found In Database"));

        Address shippingAddress = addressRepository.findById(addressId).orElseThrow(()-> new AddressException("Address not found"));


        Cart usercart = existingUser.getCart();
        if(usercart.getTotalAmount()==0){
            throw new OrdersException("Add item To the cart first.......");
        }
        Integer cartId = usercart.getCartId();

        Orders newOrder = new Orders();

        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setStatus(OrderStatus.PENDING_PAYMENT);

        existingUser.getOrders().add(newOrder);
        newOrder.setUser(existingUser);
        userRepository.save(existingUser);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem itemDTO : usercart.getCartItems()) {
            System.out.println("inside the loop");
            if (Objects.equals(itemDTO.getCart().getCartId(), cartId)) {

                OrderItem orderItem = new OrderItem();

                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setProduct(itemDTO.getProduct());
                orderItem.setOrder(newOrder);
                orderItems.add(orderItem);
                System.out.println("inside the loop and if");
            }
        }

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalAmount(usercart.getTotalAmount());
        orderRepository.save(newOrder);


        usercart.setTotalAmount(usercart.getTotalAmount() - newOrder.getTotalAmount());
        usercart.getCartItems().clear();
        cartItemRepository.removeAllProductFromCart(cartId);
        cartRepository.save(usercart);

        ShippingDetails shipper= shippingService.setShippingDetailsWhileOrdering(newOrder.getOrderId(),addressId);


        OrdersDTO orderdata=new OrdersDTO();
        orderdata.setOrderId(newOrder.getOrderId());
        orderdata.setOrderAmount(newOrder.getTotalAmount());
        orderdata.setStatus("Pending");
        orderdata.setPaymentStatus("Pending");
        orderdata.setOrderDate("Current Date"+ new Date());
        return orderdata;

    }

    @Transactional
    public OrderResponseDTO getOrdersDetails(Long orderId) throws OrdersException {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrdersException("Order not found in the database."));

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().toString())
                .orderDate(order.getOrderDate())
                .orderAmount(order.getTotalAmount())
                .shippingDetails(ShippingDTO.builder().city(order.getShippingDetails().getCity())
                        .state(order.getShippingDetails().getState())
                        .address(order.getShippingDetails().getAddress())
                        .postalCode(order.getShippingDetails().getPostalCode()).build()
                ).build();
    }

    @Override
    public List<OrderResponseDTO> getAllUserOrder(Integer userId) throws OrdersException {
        try {
            List<OrderResponseDTO> orders = orderRepository.getAllOrderByUserId(userId).stream().map(order->OrderResponseDTO.builder()
                    .orderId(order.getOrderId())
                    .status(order.getStatus().toString())
                    .orderDate(order.getOrderDate())
                    .orderAmount(order.getTotalAmount())
                    .shippingDetails(ShippingDTO.builder().city(order.getShippingDetails().getCity())
                            .state(order.getShippingDetails().getState())
                            .address(order.getShippingDetails().getAddress())
                            .postalCode(order.getShippingDetails().getPostalCode()).build()
                    ).build()).collect(Collectors.toList());
            if (orders.isEmpty()) {
                throw new OrdersException("No orders found for the user in the database.");
            }
            return orders;
        } catch (Exception e) {
            throw new OrdersException("Failed to fetch orders for the user: " + e.getMessage());
        }
    }

    @Override
    public List<OrderResponseDTO> viewAllOrders() throws OrdersException {

        List<OrderResponseDTO> orders = orderRepository.findAll().stream().map(order->OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().toString())
                .orderDate(order.getOrderDate())
                .orderAmount(order.getTotalAmount())
                .shippingDetails(ShippingDTO.builder().city(order.getShippingDetails().getCity())
                        .state(order.getShippingDetails().getState())
                        .address(order.getShippingDetails().getAddress())
                        .postalCode(order.getShippingDetails().getPostalCode()).build()
                ).build()).collect(Collectors.toList());;

        if (orders.isEmpty()) {
            throw new OrdersException("No orders found in the database.");
        }
        return orders;
    }

    @Override
    public List<OrderResponseDTO> viewAllOrderByDate(LocalDate date) throws OrdersException {

        List<OrderResponseDTO> orders = orderRepository.findByOrderDateGreaterThanEqual(date.atStartOfDay()).stream().map(order->OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().toString())
                .orderDate(order.getOrderDate())
                .orderAmount(order.getTotalAmount())
                .shippingDetails(ShippingDTO.builder().city(order.getShippingDetails().getCity())
                        .state(order.getShippingDetails().getState())
                        .address(order.getShippingDetails().getAddress())
                        .postalCode(order.getShippingDetails().getPostalCode()).build()
                ).build()).collect(Collectors.toList());;

        if (orders.isEmpty()) {
            throw new OrdersException("No orders found for the given date.");
        }

        return orders;

    }

    @Override
    public void deleteOrders(Integer userId, Long Orderid) throws OrdersException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Found In Database"));
        Orders existingOrder = orderRepository.findById(Orderid)
                .orElseThrow(() -> new UserException("order Not Found In Database"));

        orderRepository.delete(existingOrder);
    }

    @Override
    public Orders updateOrders(Long ordersid, OrdersDTO orderDTo) throws OrdersException {

        return null;
    }

}
