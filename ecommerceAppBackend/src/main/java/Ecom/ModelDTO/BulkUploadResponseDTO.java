package Ecom.ModelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulkUploadResponseDTO {
    private int count;
    private String message;
}
