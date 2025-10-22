package Ecom.Model;

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
@Table(name = "Shipper")
public class Shipper {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shipperId;

    @NotNull(message = "Name Is Mandatory ,can Not Be Null")
	@NotBlank(message = "Name Is Mandatory")
    @Column(name = "name")
    private String name;

    @NotNull(message = "phoneNumber Is Mandatory ,can Not Be Null")
   	@NotBlank(message = "phoneNumber Is Mandatory")
    @Size(min=10,max = 12)
    @Column(name = "phoneNumber")
    private String phoneNumber;
    
    @JsonIgnore
    @OneToMany(mappedBy = "shipper",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ShippingDetails> shippingDetails= new ArrayList<>();;

  
}

