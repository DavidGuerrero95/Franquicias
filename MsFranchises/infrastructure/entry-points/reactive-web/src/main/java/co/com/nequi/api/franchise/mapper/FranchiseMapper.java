package co.com.nequi.api.franchise.mapper;

import co.com.nequi.api.franchise.dto.FranchiseCreateRQ;
import co.com.nequi.api.franchise.dto.FranchiseRS;
import co.com.nequi.api.franchise.dto.TopStockRS;
import co.com.nequi.dto.MetaDataResponse;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.command.FranchiseCreate;
import co.com.nequi.model.product.report.TopStockItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface FranchiseMapper {

    @Mapping(target = "name", source = "data.name")
    FranchiseCreate toModel(FranchiseCreateRQ rq);

    default FranchiseRS toRs(Transaction tx) {
        var franchise = (Franchise) tx.getResponse();
        return new FranchiseRS(
                meta(tx),
                franchise == null ? null : new FranchiseRS.Data(franchise.id(), franchise.name()),
                null
        );
    }

    default TopStockRS toTopStockRs(Transaction tx) {
        @SuppressWarnings("unchecked")
        var list = (List<TopStockItem>) tx.getResponse();
        var data = list == null ? List.<TopStockRS.Data>of()
                : list.stream().map(i -> new TopStockRS.Data(
                i.branchId(), i.branchName(), i.productId(), i.productName(), i.stock()
        )).toList();
        return new TopStockRS(meta(tx), data, null);
    }

    private static MetaDataResponse meta(Transaction tx) {
        var messageId = Optional.ofNullable(tx.getHeaders()).map(h -> h.get("message-id")).orElse(null);
        return MetaDataResponse.builder()
                .messageId(messageId)
                .requestDateTime(LocalDateTime.now().toString())
                .applicationId("MsFranchises")
                .build();
    }
}