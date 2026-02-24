package co.com.nequi.api.products.mapper;

import co.com.nequi.api.products.datatest.TestDataProductDetail;
import co.com.nequi.api.products.dto.ProductDetailRQ;
import co.com.nequi.api.products.dto.ProductDetailRS;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.ProductQuery;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductDetailDataMapperTest {

    private final ProductDetailDataMapper mapper = Mappers.getMapper(ProductDetailDataMapper.class);

    @Test
    void shouldMapRqToModel() {
        ProductDetailRQ rq = TestDataProductDetail.validRQ();

        ProductQuery model = mapper.toModel(rq);

        assertNotNull(model);
        assertEquals(TestDataProductDetail.VALID_PRODUCT_ID, model.getId());
    }

    @Test
    void shouldMapTransactionToDto() {
        Transaction tx = TestDataProductDetail.txWithResponse();

        ProductDetailRS rs = mapper.toDto(tx);

        assertNotNull(rs);
        assertNotNull(rs.getMeta());
        assertEquals(TestDataProductDetail.MESSAGE_ID, rs.getMeta().getMessageId());
        assertNotNull(rs.getData());
        assertEquals(TestDataProductDetail.VALID_PRODUCT_ID, rs.getData().getId());
        assertEquals("USD", rs.getData().getCurrency());
    }

    @Test
    void shouldMapTransactionWithNullResponse() {
        Transaction tx = Transaction.builder()
                .headers(TestDataProductDetail.validHeaders())
                .response(null)
                .build();

        ProductDetailRS rs = mapper.toDto(tx);

        assertNotNull(rs);
        assertNotNull(rs.getMeta());
        assertEquals(TestDataProductDetail.MESSAGE_ID, rs.getMeta().getMessageId());
        assertNull(rs.getData());
    }
}
