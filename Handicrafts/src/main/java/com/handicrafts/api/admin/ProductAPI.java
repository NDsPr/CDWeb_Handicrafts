// Spring Boot version of ProductAPI
package com.handicrafts.api.admin;

import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.service.IProductService;
import com.handicrafts.service.ImageService;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
public class ProductAPI {

    @Autowired
    private IProductService productService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ILogService<ProductDTO> logService;

    @GetMapping
    public ResponseEntity<DatatableDTO<ProductDTO>> getProducts(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "order[0][column]", defaultValue = "0") String orderBy,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String orderDir,
            @RequestParam(value = "columns[{orderBy}][data]", defaultValue = "id") String columnOrder
    ) {
        List<ProductDTO> products = productService.getProductsDatatable(start, length, columnOrder, orderDir, searchValue);
        products.forEach(p -> p.setImages(imageService.findImagesByProductId(p.getId())));

        int recordsTotal = productService.getRecordsTotal();
        int recordsFiltered = productService.getRecordsFiltered(searchValue);

        draw++;
        DatatableDTO<ProductDTO> dto = new DatatableDTO<>(products, recordsTotal, recordsFiltered, draw);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestParam int id, HttpServletRequest request) {
        ProductDTO prevProduct = productService.findById(id);
        int affectedRows = productService.disableProduct(id);

        String status;
        String notify;

        if (affectedRows > 0) {
            logService.log(request, "admin-delete-product", "SUCCESS", 1, prevProduct, null);
            status = "success";
            notify = "Vô hiệu hóa sản phẩm thành công!";
            UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevProduct.getId(), "Product");
        } else {
            logService.log(request, "admin-delete-product", "FAIL", 2, prevProduct, prevProduct);
            status = "error";
            notify = "Có lỗi khi vô hiệu hóa sản phẩm!";
        }

        String json = String.format("{\"status\": \"%s\", \"notify\": \"%s\"}", status, notify);
        return ResponseEntity.ok(json);
    }
}
