package co.com.nequi.api.products.mapper;

import co.com.nequi.api.products.dto.ProductDetailRQ;
import co.com.nequi.api.products.dto.ProductDetailRS;
import co.com.nequi.dto.MetaDataResponse;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.ProductDetail;
import co.com.nequi.model.product.ProductQuery;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductDetailDataMapper {

    @Mapping(target = "id", source = "data.id")
    ProductQuery toModel(ProductDetailRQ rq);

    @Mapping(target = "id", source = "id")
    ProductDetailRS.Data toDto(ProductDetail pd);

    default ProductDetailRS toDto(Transaction tx) {
        var pd = (ProductDetail) tx.getResponse();
        var meta = MetaDataResponse.builder()
                .messageId(Optional.ofNullable(tx.getHeaders()).map(h -> h.get("message-id")).orElse(null))
                .requestDateTime(LocalDateTime.now().toString())
                .applicationId("MsFranchises")
                .build();
        return ProductDetailRS.builder()
                .meta(meta)
                .data(pd == null ? null : toDto(pd))
                .build();
    }

}