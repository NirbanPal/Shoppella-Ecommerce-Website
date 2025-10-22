package Ecom.Repository;

import java.util.List;
import java.util.Optional;

import Ecom.Enum.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Ecom.Model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p  WHERE p.name like %:prduct%")
	public List<Product> findByName(@Param("prduct") String name);
	
	@Query("SELECT p FROM Product p  WHERE p.category like %:cat%")
	public List<Product> getProductCategoryName(@Param("cat") ProductCategory category);

    List<Product> findAllByNameContainingIgnoreCase(String keyword, Sort sort);

	@Query("SELECT p FROM Product p WHERE " +
			"(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
			"(:category IS NULL OR p.category = :category) AND " +
			"(:minPrice IS NULL OR p.price >= :minPrice) AND " +
			"(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
			"(:isAvailable IS NULL OR p.isAvailable = :isAvailable)")
	Page<Product> searchProducts(
			@Param("keyword") String keyword,
			@Param("category") ProductCategory category,
			@Param("minPrice") Double minPrice,
			@Param("maxPrice") Double maxPrice,
			@Param("isAvailable") Boolean isAvailable,
			Pageable pageable
	);

}
