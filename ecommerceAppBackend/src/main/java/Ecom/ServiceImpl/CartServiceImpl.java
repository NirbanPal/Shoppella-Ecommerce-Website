package Ecom.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import Ecom.Exception.CartException;
import Ecom.Exception.ProductException;
import Ecom.Exception.UserException;
import Ecom.Model.Cart;
import Ecom.Model.CartItem;
import Ecom.Model.Product;
import Ecom.Model.User;
import Ecom.Repository.CartItemRepository;
import Ecom.Repository.CartRepository;
import Ecom.Repository.ProductRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final ProductRepository productRepository;

	private final CartRepository cartRepository;

	private final UserRepository userRepository;

	public Cart addProductToCart(Integer userId, Integer productId) throws CartException {

		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not available in Stock..."));

		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found In Database"));

		if (existingUser.getCart() != null) {
			System.out.println("Cart is already allocated...");
			Cart userCart = existingUser.getCart();

			List<CartItem> cartItems = userCart.getCartItems();

			if (!cartItems.isEmpty()) {
				System.out.println("cart item inside loop...");
                for (CartItem cartItem : cartItems) {
                    System.out.println("inside loop");
                    if (cartItem.getProduct().getProductId().equals(productId) &&
                            cartItem.getCart().getCartId().equals(userCart.getCartId())) {
                        throw new CartException("Product Already in the Cart, Please Increase the Quantity");
                    }
                }
			}
			CartItem cartItem = new CartItem();
			cartItem.setProduct(existingProduct);
			cartItem.setQuantity(1);
			cartItem.setCart(userCart);
			userCart.getCartItems().add(cartItem);

			userCart.setTotalAmount(calculateCartTotal(cartItems));
			cartRepository.save(userCart);
			return userCart;

		} else {

			Cart newCart = new Cart();
			newCart.setUser(existingUser);
			existingUser.setCart(newCart);


			CartItem cartItem = new CartItem();

			cartItem.setProduct(existingProduct);
			cartItem.setQuantity(1);

			newCart.getCartItems().add(cartItem);
			cartItem.setCart(newCart);

			newCart.setTotalAmount(calculateCartTotal(newCart.getCartItems()));
			userRepository.save(existingUser);

			return existingUser.getCart();
		}
	}

	private long calculateCartTotal(List<CartItem> cartItems) {
		long total = 0L;
		for (CartItem item : cartItems) {
			long itemPrice = item.getProduct().getPrice();
			int itemQuantity = (item.getQuantity());
			total +=itemPrice * itemQuantity;
		}
		return total;
	}

	@Override
	public Cart increaseProductQuantity(Integer userId, Integer productId) throws CartException {
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found in Database"));

		if (existingUser.getCart() == null) {
			throw new CartException("Cart Not Found");
		}

		Cart userCart = existingUser.getCart();
		List<CartItem> cartItems = userCart.getCartItems();

		CartItem cartItemToUpdate = cartItems.stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)
						&&item.getCart().getCartId().equals(userCart.getCartId())).findFirst()
				.orElseThrow(() -> new CartException("Cart Item Not Found"));

		int quantity = cartItemToUpdate.getQuantity();
		cartItemToUpdate.setQuantity(quantity + 1);
		userCart.setCartItems(cartItems);
		userCart.setTotalAmount(calculateCartTotal(cartItems));
		cartRepository.save(userCart);

		return userCart;
	}
	@Override
	public Cart decreaseProductQuantity(Integer userId, Integer productId) throws CartException {
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found in Database"));

		if (existingUser.getCart() == null) {
			throw new CartException("Cart Not Found");
		}

		Cart userCart = existingUser.getCart();
		List<CartItem> cartItems = userCart.getCartItems();
		CartItem cartItemToUpdate = cartItems.stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)
						&&item.getCart().getCartId().equals(userCart.getCartId())).findFirst()
				.orElseThrow(() -> new CartException("Cart Item Not Found"));

		int quantity = cartItemToUpdate.getQuantity();
		if(quantity==1){
			throw new CartException("Product can not be Further decrease...");
		}
		if (quantity > 1) {
			cartItemToUpdate.setQuantity(quantity - 1);


			userCart.setCartItems(cartItems);
			userCart.setTotalAmount(calculateCartTotal(cartItems));
			cartRepository.save(userCart);
		} else {
			cartItems.remove(cartItemToUpdate);
			userCart.setCartItems(cartItems);
			userCart.setTotalAmount(calculateCartTotal(cartItems));
			cartRepository.save(userCart);
		}
		return userCart;
	}
	@Override
	public void removeProductFromCart(Integer cartId, Integer productId) throws CartException {
		Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new CartException("Cart Not Found"));
		Product removedProduct = productRepository.findById(productId).orElseThrow(()->new ProductException("Product does not found."));

		CartItem removedItem=existingCart.getCartItems().stream().filter(cItem->cItem.getProduct().getProductId().equals(productId)).findFirst().orElseThrow(()->new ProductException("Product not found in the cart"));

		existingCart.getCartItems().remove(removedItem);
		existingCart.setTotalAmount(calculateCartTotal(existingCart.getCartItems()));
		cartRepository.save(existingCart);
	}

	@Override
	public Cart getAllCartProduct(Integer cartId) throws CartException {
		Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new CartException("Cart Not Found"));

		List<CartItem> cartItems = existingCart.getCartItems();
		List<Product> products = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			if (cartItem.getCart().getCartId() == cartId) {
				Product product = cartItem.getProduct();
				products.add(product);
			}
		}
		if(products.isEmpty()){
			throw new CartException("Cart is Empty...");
		}
		return existingCart;
	}

	@Override
	public void removeAllProductFromCart(Integer cartId) throws CartException {
		Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new CartException("Cart Not Found"));

		existingCart.getCartItems().clear();

		existingCart.setTotalAmount(0L);
		cartRepository.save(existingCart);
	}
}