package co.com.nequi.api.product.mapper;

import co.com.nequi.api.product.dto.ProductRS;
import co.com.nequi.dto.MetaDataResponse;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.ProductCreate;
import co.com.nequi.model.product.command.ProductStockUpdate;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductMapper {

    private static MetaDataResponse meta(Transaction tx) {
        var messageId = Optional.ofNullable(tx.getHeaders()).map(h -> h.get("message-id")).orElse(null);
        return MetaDataResponse.builder()
                .messageId(messageId)
                .requestDateTime(LocalDateTime.now().toString())
                .applicationId("MsFranchises")
                .build();
    }

    default ProductCreate toCreate(Long branchId, String name, Integer stock) {
        return new ProductCreate(branchId, name, stock);
    }

    default ProductStockUpdate toStockUpdate(Long productId, Integer stock) {
        return new ProductStockUpdate(productId, stock);
    }

    default ProductRS toRs(Transaction tx) {
        var product = (Product) tx.getResponse();
        return new ProductRS(
                meta(tx),
                product == null ? null : new ProductRS.Data(product.id(), product.branchId(),
                        product.name(), product.stock()),
                null
        );
    }
}