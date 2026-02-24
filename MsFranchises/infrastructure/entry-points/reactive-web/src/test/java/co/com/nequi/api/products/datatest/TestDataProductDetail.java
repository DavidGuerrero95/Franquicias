package co.com.nequi.api.products.datatest;

import co.com.nequi.api.products.dto.ProductDetailRQ;
import co.com.nequi.api.products.dto.ProductDetailRS;
import co.com.nequi.dto.MetaDataResponse;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.ProductDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class TestDataProductDetail {

    public static final String BASE_PATH = "/ms-franchises/api/v1/";
    public static final String PRODUCT_DETAIL_PATH = "products/detail";
    public static final String MESSAGE_ID = "8348c30c-1296-4882-84b8-d7306205ce26";
    public static final String VALID_PRODUCT_ID = "MLA0001";
    private TestDataProductDetail() {
    }

    public static Map<String, String> validHeaders() {
        return Map.of(
                "message-id", MESSAGE_ID,
                "content-type", "application/json",
                "accept", "application/json"
        );
    }

    public static ProductDetailRQ validRQ() {
        return ProductDetailRQ.builder()
                .data(ProductDetailRQ.Data.builder()
                        .id(VALID_PRODUCT_ID)
                        .build())
                .build();
    }

    public static ProductDetail modelResponse() {
        return ProductDetail.builder()
                .id(VALID_PRODUCT_ID)
                .title("Smartphone X 128GB Black")
                .price(new BigDecimal("1299.99"))
                .currency("USD")
                .availableQuantity(42)
                .soldQuantity(318)
                .condition("new")
                .permalink("https://example.com/item/MLA0001")
                .warranty("12 meses")
                .seller(ProductDetail.Seller.builder()
                        .id("S100").nickname("TechStore").city("Bogotá").reputationLevel("gold").build())
                .shipping(ProductDetail.Shipping.builder()
                        .freeShipping(true).mode("me2").logisticType("drop_off").build())
                .category(ProductDetail.CategoryPath.builder()
                        .id("CAT100").name("Cell Phones")
                        .pathFromRoot(List.of(
                                ProductDetail.CategoryPath.Node.builder().id("ROOT").name("Electrónica").build(),
                                ProductDetail.CategoryPath.Node.builder().id("CAT100").name("Celulares").build()
                        )).build())
                .pictures(List.of(
                        ProductDetail.Picture.builder().id("P1").url("http://images/mla0001-1.jpg").secureUrl("https://images/mla0001-1.jpg").build(),
                        ProductDetail.Picture.builder().id("P2").url("http://images/mla0001-2.jpg").secureUrl("https://images/mla0001-2.jpg").build()
                ))
                .attributes(List.of(
                        ProductDetail.Attribute.builder().name("RAM").value("6GB").build(),
                        ProductDetail.Attribute.builder().name("Storage").value("128GB").build()
                ))
                .build();
    }

    public static Transaction txWithResponse() {
        return Transaction.builder()
                .headers(validHeaders())
                .response(modelResponse())
                .status(true)
                .build();
    }

    public static ProductDetailRS expectedRS() {
        return ProductDetailRS.builder()
                .meta(MetaDataResponse.builder()
                        .messageId(MESSAGE_ID)
                        .requestDateTime(LocalDateTime.now().toString())
                        .applicationId("MsFranchises")
                        .build())
                .data(ProductDetailRS.Data.builder()
                        .id(VALID_PRODUCT_ID)
                        .title("Smartphone X 128GB Black")
                        .price(new BigDecimal("1299.99"))
                        .currency("USD")
                        .availableQuantity(42)
                        .soldQuantity(318)
                        .condition("new")
                        .permalink("https://example.com/item/MLA0001")
                        .warranty("12 meses")
                        .seller(ProductDetailRS.Data.Seller.builder()
                                .id("S100").nickname("TechStore").city("Bogotá").reputationLevel("gold").build())
                        .shipping(ProductDetailRS.Data.Shipping.builder()
                                .freeShipping(true).mode("me2").logisticType("drop_off").build())
                        .category(ProductDetailRS.Data.CategoryPath.builder()
                                .id("CAT100").name("Cell Phones").build())
                        .pictures(List.of(
                                ProductDetailRS.Data.Picture.builder().id("P1").url("http://images/mla0001-1.jpg").secureUrl("https://images/mla0001-1.jpg").build(),
                                ProductDetailRS.Data.Picture.builder().id("P2").url("http://images/mla0001-2.jpg").secureUrl("https://images/mla0001-2.jpg").build()
                        ))
                        .attributes(List.of(
                                ProductDetailRS.Data.Attribute.builder().name("RAM").value("6GB").build(),
                                ProductDetailRS.Data.Attribute.builder().name("Storage").value("128GB").build()
                        ))
                        .build())
                .build();
    }
}
