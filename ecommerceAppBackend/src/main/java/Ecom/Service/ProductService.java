package Ecom.Service;

import java.util.List;

import Ecom.Enum.ProductCategory;
import Ecom.Exception.ProductException;
import Ecom.Model.Product;
import Ecom.ModelDTO.BulkUploadResponseDTO;
import Ecom.ModelDTO.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
	
	public Product addProduct(MultipartFile productImg, ProductDTO products)throws ProductException;
	
	public Product updateProduct(Integer productId, MultipartFile productImg, ProductDTO product)throws ProductException;
	
	public List<Product> getProductByName(String name)throws ProductException;
	
	public List<Product> getAllProduct(String keyword, String sortDirection, String sortBy)throws ProductException;
	
	public List<Product> getProductByCategory(String category) throws ProductException;
	
	public void removeProduct(Integer productId) throws ProductException;

	public Product getSingleProduct(Integer productId) throws ProductException;

	public Page<Product> searchProducts(
			String keyword,
			ProductCategory category,
			Double minPrice,
			Double maxPrice,
			Boolean isAvailable,
			int page,
			int size,
			String sortBy,
			String sortDir
	) throws ProductException;

	public BulkUploadResponseDTO uploadProducts(MultipartFile file) throws ProductException;
}
 