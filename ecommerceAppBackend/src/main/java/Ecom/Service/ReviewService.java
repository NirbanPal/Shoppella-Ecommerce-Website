package Ecom.Service;

import java.util.List;

import Ecom.Exception.ReviewException;
import Ecom.Model.Review;
import Ecom.ModelDTO.ReviewDTO;

public interface ReviewService {
	
	 public Review addReviewToProduct(Integer productId, Integer UserId, ReviewDTO review)
			 throws ReviewException;
	 
	 public Review updateReviewToProduct(Integer reviewId,ReviewDTO review)
			 throws ReviewException;
	 
	 public void deleteReview(Integer reviewId) throws ReviewException;
	 
	 public List<Review> getAllReviewOfProduct(Integer productId)throws ReviewException;

}
