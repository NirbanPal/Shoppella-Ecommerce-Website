package Ecom.Utility;

import Ecom.Enum.ProductCategory;
import Ecom.Model.Product;
import Ecom.ModelDTO.ImageUploadError;
import Ecom.ModelDTO.ImageUploadRow;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExcelHelper {

    /**
     * Generates an Excel file mapping original image name → uploaded URL
     * Also adds a separate sheet for failed uploads
     */
    public ByteArrayResource generateExcel(List<ImageUploadRow> uploadedRows, List<ImageUploadError> errorRows) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet successSheet = workbook.createSheet("Uploaded Images");
            Row header = successSheet.createRow(0);
            header.createCell(0).setCellValue("Original File Name");
            header.createCell(1).setCellValue("Uploaded URL");

            int rowNum = 1;
            for (ImageUploadRow row : uploadedRows) {
                Row r = successSheet.createRow(rowNum++);
                r.createCell(0).setCellValue(row.getOriginalName());
                r.createCell(1).setCellValue(row.getUploadedUrl());
            }

            successSheet.autoSizeColumn(0);
            successSheet.autoSizeColumn(1);

            if (!errorRows.isEmpty()) {
                Sheet errorSheet = workbook.createSheet("Failed Uploads");
                Row errHeader = errorSheet.createRow(0);
                errHeader.createCell(0).setCellValue("Original File Name");
                errHeader.createCell(1).setCellValue("Error Message");

                int errRowNum = 1;
                for (ImageUploadError error : errorRows) {
                    Row r = errorSheet.createRow(errRowNum++);
                    r.createCell(0).setCellValue(error.getOriginalName());
                    r.createCell(1).setCellValue(error.getErrorMessage());
                }

                errorSheet.autoSizeColumn(0);
                errorSheet.autoSizeColumn(1);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel: " + e.getMessage(), e);
        }
    }


    public List<Product> excelToProducts(InputStream is) {
        List<Product> products = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header
            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row row = rows.next();
                Product dto = new Product();

                dto.setName(getStringCell(row.getCell(0)));
                dto.setImageUrl(getStringCell(row.getCell(1)));
                dto.setCategory(ProductCategory.fromDisplayName(getStringCell(row.getCell(2))));
                dto.setDescription(getStringCell(row.getCell(3)));
                long price = (long) row.getCell(4).getNumericCellValue();
                if(price <= 0 || price > 10_000_000){
                    throw new IllegalArgumentException("Price must be greater than 0 and maximum 10_000_000 at row " + row.getRowNum());
                }
                dto.setPrice(price);
                // Validate DTO (JSR-380 programmatic validation)
                validate(dto);

                products.add(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }

        return products;
    }

    private String getStringCell(Cell cell) {
        return cell == null ? null : cell.getStringCellValue().trim();
    }

    private void validate(Product dto) {
        Set<ConstraintViolation<Product>> violations =
                Validation.buildDefaultValidatorFactory().getValidator().validate(dto);

        if (!violations.isEmpty()) {
            String errors = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Validation failed: " + errors);
        }
    }
}

