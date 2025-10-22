package Ecom.ModelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadRow {
    private String originalName;
    private String uploadedUrl;
}
