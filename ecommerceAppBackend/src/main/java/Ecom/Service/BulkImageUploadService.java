package Ecom.Service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BulkImageUploadService {
    public ByteArrayResource uploadBulkImages(List<MultipartFile> files, String category);
}
