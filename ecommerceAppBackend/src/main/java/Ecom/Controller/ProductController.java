package Ecom.Controller;

import java.util.List;
import java.util.Objects;

import Ecom.Cloudinary.CloudinaryService;
import Ecom.Enum.ProductCategory;
import Ecom.Exception.InvalidFileException;
import Ecom.ModelDTO.BulkUploadResponseDTO;
import Ecom.Service.BulkImageUploadService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ecom.Model.Product;
import Ecom.ModelDTO.ProductDTO;
import Ecom.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/ecom/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BulkImageUploadService bulkImageUploadService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestPart("productImg") MultipartFile productImg, @Valid @RequestPart ProductDTO product) {
        Product newProduct = productService.addProduct(productImg,product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct( @PathVariable Integer productId, @RequestPart(value = "productImg", required = false) MultipartFile productImg,  @Valid @RequestPart("updatedProduct") ProductDTO updatedProduct) {
        Product updatedProductResult = productService.updateProduct(productId, productImg, updatedProduct);
        return new ResponseEntity<>(updatedProductResult, HttpStatus.OK);
    }

    @GetMapping("/product-By-name/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String name) {
        List<Product> products = productService.getProductByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> search(
       @RequestParam (required = false)  String keyword,
       @RequestParam(required = false,defaultValue = "asc") String sort,
       @RequestParam(required = false,defaultValue = "price") String sortBy

    ) {
        List<Product> products = productService.getAllProduct(keyword, sort, sortBy);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }



    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable Integer productId) {
        Product singleProduct = productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Integer productId) {
        productService.removeProduct(productId);
        return new ResponseEntity<>("Product removed successfully.", HttpStatus.OK);
    }


    //Pagination and advanced sorting and search according to keywords, category, min-max price,
    @GetMapping("/products/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<Product> result = productService.searchProducts(
                keyword,
                category,
                minPrice,
                maxPrice,
                isAvailable,
                page,
                size,
                sortBy,
                sortDir
        );
        return ResponseEntity.ok(result);
    }

    //bulk Image upload according to category
    @PostMapping("/bulk-image-upload")
    public ResponseEntity<Resource> bulkImageUpload(
            @RequestParam("category") String category,
            @RequestParam("imageFiles") List<MultipartFile> files) {

        if (files.isEmpty()) {
            throw new InvalidFileException("No files provided for upload");
        }

        ByteArrayResource excelFile = bulkImageUploadService.uploadBulkImages(files, category);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=uploaded_images.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile); // cast to Resource
    }



    //bulk products upload using excel sheet(Name, ImageUrl, Category, Description, Price)
    @PostMapping("/upload-all")
    public ResponseEntity<BulkUploadResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
            throw new RuntimeException("Only Excel (.xlsx) files are supported");
        }

        BulkUploadResponseDTO response = productService.uploadProducts(file);
        return ResponseEntity.ok(response);
    }

}
