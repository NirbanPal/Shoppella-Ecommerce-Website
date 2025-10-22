package Ecom.Cloudinary;


import Ecom.Exception.InvalidFileException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Define the maximum file size in bytes (500KB)
    private static final long MAX_FILE_SIZE = 500 * 1024;

    // Define the allowed content types
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png");

    // Private helper method for validation
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty. Please select a file to upload.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("File size exceeds the limit of 2MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidFileException("Invalid file type. Only JPG, JPEG, and PNG are allowed.");
        }
    }

    /**
     * Uploads a file to Cloudinary inside the `product/{category}` folder.
     * Example: product/electronics/image123.jpg
     */

    public String uploadFile(MultipartFile file, String category) {

        // Perform validation first
        validateFile(file);

        try {

            // Cloudinary auto-creates the folder if it doesn't exist
            String folderPath = "product/" + category;


            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folderPath,
                            "public_id", UUID.randomUUID().toString(),
                            "overwrite", true, // allow replacing existing
                            "invalidate", true, // clear CDN cache or to purge the CDN cache on overwrite
                            "resource_type", "auto"
                    ));
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Could not upload the file! " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a file from Cloudinary by its publicId.
     * publicId must include folder path (e.g., product/electronics/image123)
     */

    public void deleteFile(String publicUuId, String category) {
        try {
            String publicUuIdWithFolder = "product/" + category+"/"+publicUuId;

            // The 'destroy' method deletes the resource
            cloudinary.uploader().destroy(publicUuIdWithFolder, Map.of());
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Could not delete the file!", e);
        }
    }

    /**
     * Deletes all files inside a category (and hence the folder).
     * Cloudinary auto-removes empty folders.
     */
    public void deleteCategory(String category) {
        try {
            String prefix = "product/" + category;
            cloudinary.api().deleteResourcesByPrefix(prefix, Map.of());
            // Cloudinary will auto-delete empty folder
        } catch (Exception e) {
            throw new RuntimeException("Could not delete category folder!", e);
        }
    }

}
