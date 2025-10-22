package Ecom.ServiceImpl;

import Ecom.Cloudinary.CloudinaryService;
import Ecom.ModelDTO.ImageUploadError;
import Ecom.ModelDTO.ImageUploadRow;
import Ecom.Service.BulkImageUploadService;
import Ecom.Utility.ExcelHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BulkImageUploadServiceImpl implements BulkImageUploadService {



    private final CloudinaryService cloudinaryService;
    private final ExcelHelper excelHelper;

    @Override
    public ByteArrayResource uploadBulkImages(List<MultipartFile> files, String category) {
        List<ImageUploadRow> uploadedRows = new ArrayList<>();
        List<ImageUploadError> errorRows = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String uploadedUrl = cloudinaryService.uploadFile(file, category);
                uploadedRows.add(new ImageUploadRow(file.getOriginalFilename(), uploadedUrl));
            } catch (Exception e) {
                errorRows.add(new ImageUploadError(file.getOriginalFilename(), e.getMessage()));
            }
        }

        return excelHelper.generateExcel(uploadedRows, errorRows);
    }

}
