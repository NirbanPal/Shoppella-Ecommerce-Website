package Ecom.ServiceImpl;

import java.util.List;

import Ecom.ModelDTO.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecom.Exception.ProductException;
import Ecom.Exception.ReviewException;
import Ecom.Exception.UserException;
import Ecom.Model.Product;
import Ecom.Model.Review;
import Ecom.Model.User;
import Ecom.Repository.ProductRepository;
import Ecom.Repository.ReviewRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ProductRepository productRepository;

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	@Override
	public Review addReviewToProduct(Integer productId, Integer userId, ReviewDTO review) throws ReviewException {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ReviewException("Product Not Found"));

		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ReviewException("User Not Found In Database"));

		Review reviewData = new Review();

		reviewData.setComment(review.getComment());
		reviewData.setRating(review.getRating());
		reviewData.setUser(existingUser);
		reviewData.setProduct(existingProduct);

		reviewRepository.save(reviewData);

		return reviewRepository.save(reviewData);
	}

	@Override
	public Review updateReviewToProduct(Integer reviewId, ReviewDTO review) throws ReviewException {
		Review existingReview = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewException("Review With Id "+reviewId+"Not Found In DataBase"));

		existingReview.setComment(review.getComment());
		existingReview.setRating(review.getRating());

		return reviewRepository.save(existingReview);
	}

	@Override
	public void deleteReview(Integer reviewId) throws ReviewException {
		Review existingReview = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewException("Review With Id "+reviewId+"Not Found In DataBase"));
		
		reviewRepository.delete(existingReview);

	}

	@Override
	public List<Review> getAllReviewOfProduct(Integer productId) throws ReviewException {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ReviewException("Invalid Product id"));
		
		List<Review> allReviewsByProductId = reviewRepository.findAllReviewsByProductId(productId);
		if(allReviewsByProductId.isEmpty()) { 
			 throw new ReviewException ("No Rewiew Of Given Product is Available");
		}
		return allReviewsByProductId;
	}

}
