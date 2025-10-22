package Ecom.Model;

import Ecom.Enum.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Products")
public class Product {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @NotNull(message = "Product name is Mandatory ,can Not Be Null")
	@NotBlank(message = "Product name is Mandatory")
    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "isAvailable")
    private boolean isAvailable=true;
    
    @NotNull(message = "Product description is Mandatory ,can Not Be Null")
  	@NotBlank(message = "Product description is Mandatory")
    @Size(min=10,max = 50)
    @Column(name = "description")
    private String description;

    @NotNull(message = "Product price is Mandatory ,can Not Be Null")
    @Column(name = "price")
    private Long price;

    @NotNull(message = "Product category_name is Mandatory ,can Not Be Null")
    @Column(name = "category_name")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    

    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<OrderItem> orderItem= new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<Review> reviews= new ArrayList<>();
    
   
}

