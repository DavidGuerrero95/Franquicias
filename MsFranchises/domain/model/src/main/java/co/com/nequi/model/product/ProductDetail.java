package co.com.nequi.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductDetail implements Serializable {
    private String id;
    private String title;
    private BigDecimal price;
    private String currency;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private String condition;
    private String permalink;
    private String warranty;
    private Seller seller;
    private Shipping shipping;
    private CategoryPath category;
    private List<Picture> pictures;
    private List<Attribute> attributes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Seller implements Serializable {
        private String id;
        private String nickname;
        private String city;
        private String reputationLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Shipping implements Serializable {
        private Boolean freeShipping;
        private String mode;
        private String logisticType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class CategoryPath implements Serializable {
        private String id;
        private String name;
        private List<Node> pathFromRoot;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class Node implements Serializable {
            private String id;
            private String name;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Picture implements Serializable {
        private String id;
        private String url;
        private String secureUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Attribute implements Serializable {
        private String name;
        private String value;
    }

}