package Ecom.ServiceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Ecom.Cloudinary.CloudinaryService;
import Ecom.Enum.ProductCategory;
import Ecom.ModelDTO.BulkUploadResponseDTO;
import Ecom.Utility.ExcelHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Ecom.Exception.ProductException;
import Ecom.Model.Product;
import Ecom.ModelDTO.ProductDTO;
import Ecom.Repository.ProductRepository;
import Ecom.Service.ProductService;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ExcelHelper excelHelper;

    @Override
    public Product addProduct(MultipartFile productImg, ProductDTO product) throws ProductException {
        if (product == null)
            throw new ProductException("Product Can not be Null");

        Product savedProd = new Product();
        savedProd.setName(product.getName());
        savedProd.setPrice(product.getPrice());
        savedProd.setDescription(product.getDescription());
        savedProd.setCategory(ProductCategory.fromDisplayName(product.getCategory()));
        String url=cloudinaryService.uploadFile(productImg,product.getCategory());
        savedProd.setImageUrl(url);
        return productRepository.save(savedProd);
    }

    @Override
    public Product updateProduct(Integer productId, MultipartFile productImage, ProductDTO updatedProduct) throws ProductException {

        Product existingProduct = productRepository.findById(productId).orElseThrow(()->new ProductException("Product with ID " + productId + " not found."));

        System.out.println("before");
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(ProductCategory.fromDisplayName(updatedProduct.getCategory()));
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());

        if (productImage != null && !productImage.isEmpty()) {
            String url=cloudinaryService.uploadFile(productImage,updatedProduct.getCategory());
            existingProduct.setImageUrl(url);
        }
        System.out.println("after");
        productRepository.save(existingProduct);
        return existingProduct;
    }

    @Override
    public List<Product> getProductByName(String name) throws ProductException {

        List<Product> existProductByName = productRepository.findByName(name);
        if (existProductByName.isEmpty()) {
            throw new ProductException("Product Not found with name " + name);
        }
        return existProductByName;
    }

    @Override
    public List<Product> getAllProduct(String keyword, String sortDirection, String sortBy) throws ProductException {

        Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,sortBy);

        List<Product> products;

        if (keyword != null) {

            products = productRepository.findAllByNameContainingIgnoreCase(keyword, sort);
        } else {
            products = productRepository.findAll(sort);
        }
        if (products.isEmpty()) {
            throw new ProductException("Product List Empty");
        }

        return products;

    }

    @Override
    public List<Product> getProductByCategory(String category) throws ProductException {
        // Retrieve products by category from the database
        List<Product> allProductCategoryName = productRepository.getProductCategoryName(ProductCategory.fromDisplayName(category));

        if (allProductCategoryName.isEmpty())
            throw new ProductException("Product with category Name " + category + " not found.");

        return allProductCategoryName;
    }


    @Override
    public void removeProduct(Integer productId) throws ProductException {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product with ID " + productId + " not found."));
        String uuidOfImgFile=extractUuidFromUrl(existingProduct.getImageUrl());
        cloudinaryService.deleteFile(uuidOfImgFile,existingProduct.getCategory().getDisplayName());
        productRepository.delete(existingProduct);
    }

    @Override
    public Product getSingleProduct(Integer productId) {

        Product single = productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));
        return single;
    }



    //Look into it
    @Override
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
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.searchProducts(
                keyword,
                category,
                minPrice,
                maxPrice,
                isAvailable,
                pageable
        );
    }

    public static String extractUuidFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            String path = url.getPath();

            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("URL path is empty.");
            }

            String[] parts = path.split("/");
            String fileName = parts[parts.length - 1];  // 1t3v3vt3e.png

            if (!fileName.contains(".")) {
                throw new IllegalArgumentException("No file extension found in the URL.");
            }

            String uuid = fileName.substring(0, fileName.lastIndexOf('.'));

            if (uuid.isEmpty()) {
                throw new IllegalArgumentException("UUID could not be extracted.");
            }

            return uuid;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + urlStr, e);
        }
    }


    @Transactional
    @Override
    public BulkUploadResponseDTO uploadProducts(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {

            List<Product> products = excelHelper.excelToProducts(file.getInputStream());

            productRepository.saveAll(products);

            return new BulkUploadResponseDTO(products.size(), "Products uploaded successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }
    }

}
