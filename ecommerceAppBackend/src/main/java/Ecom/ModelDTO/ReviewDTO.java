package Ecom.ModelDTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {

    @NotNull(message = "Pls provide Rating  ,can Not Be Null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not be more than 5")
    private Integer rating;

    @NotNull(message = "Pls provide comment  ,can Not Be Null")
    @NotBlank(message = "Comment should be Filled")
    private String comment;

}
