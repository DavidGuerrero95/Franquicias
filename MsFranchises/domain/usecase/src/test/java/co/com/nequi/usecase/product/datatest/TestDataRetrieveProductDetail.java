package co.com.nequi.usecase.product.datatest;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.ProductQuery;

import java.util.Map;

public class TestDataRetrieveProductDetail {

    public static final String DEFAULT_ID = "MLA0001";

    public TestDataRetrieveProductDetail() {
        // prevent instantiation
    }

    public static ProductQuery productQuery(String id) {
        return ProductQuery.builder().id(id).build();
    }

    public static Transaction emptyTx() {
        return Transaction.builder().build();
    }

    public static Transaction txWithRequest(ProductQuery rq) {
        return Transaction.builder()
                .headers(Map.of("message-id", "11111111-1111-1111-1111-111111111111"))
                .request(rq)
                .build();
    }
}