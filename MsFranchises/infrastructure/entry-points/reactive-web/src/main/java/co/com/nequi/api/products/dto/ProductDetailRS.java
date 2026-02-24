package co.com.nequi.api.products.dto;

import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.dto.MetaDataResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class ProductDetailRS implements Serializable {

    private transient MetaDataResponse meta;
    private transient Data data;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private transient List<ErrorRes.Data> errors;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Data implements Serializable {
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


        @lombok.Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class Seller implements Serializable {
            private String id;
            private String nickname;
            private String city;
            private String reputationLevel;
        }

        @lombok.Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class Shipping implements Serializable {
            private Boolean freeShipping;
            private String mode;
            private String logisticType;
        }

        @lombok.Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class CategoryPath implements Serializable {
            private String id;
            private String name;
            private List<Node> pathFromRoot;

            @lombok.Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder(toBuilder = true)
            public static class Node implements Serializable {
                private String id;
                private String name;
            }
        }

        @lombok.Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class Picture implements Serializable {
            private String id;
            private String url;
            private String secureUrl;
        }

        @lombok.Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class Attribute implements Serializable {
            private String name;
            private String value;
        }
    }
}