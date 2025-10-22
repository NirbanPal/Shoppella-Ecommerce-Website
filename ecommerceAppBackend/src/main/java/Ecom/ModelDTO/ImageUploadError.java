package Ecom.ModelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadError {
    private String originalName;
    private String errorMessage;
}
