package co.com.nequi.api.franchise.mapper;

import co.com.nequi.api.franchise.dto.FranchiseTreeRS;
import co.com.nequi.dto.MetaDataResponse;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.report.BranchTreeItem;
import co.com.nequi.model.franchise.report.FranchiseTreeItem;
import co.com.nequi.model.franchise.report.FranchiseTreePage;
import co.com.nequi.model.franchise.report.ProductTreeItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface FranchiseTreeMapper {

    private static MetaDataResponse meta(Transaction tx, FranchiseTreePage page) {
        var messageId = Optional.ofNullable(tx.getHeaders()).map(h -> h.get("message-id")).orElse(null);

        MetaDataResponse.MetaDataResponseBuilder<?, ?> builder = MetaDataResponse.builder()
                .messageId(messageId)
                .requestDateTime(LocalDateTime.now().toString())
                .applicationId("MsFranchises");

        if (page != null) {
            builder.lastPageIndicator(page.lastPageIndicator());
            builder.additionalRecords(page.additionalRecords());
            builder.totalRecords((int) Math.min(Integer.MAX_VALUE, page.totalRecords()));
            builder.pageSize(page.pageSize());
            builder.totalPageNumber(page.totalPageNumber());
        }

        return builder.build();
    }

    default FranchiseTreeRS toRs(Transaction tx) {
        var page = (FranchiseTreePage) tx.getResponse();

        List<FranchiseTreeRS.Data> data = page == null ? List.of() : page.data().stream()
                .map(this::toFranchise)
                .toList();

        return new FranchiseTreeRS(meta(tx, page), data, null);
    }

    private FranchiseTreeRS.Data toFranchise(FranchiseTreeItem f) {
        List<FranchiseTreeRS.Branch> branches = f.branches() == null ? List.of() : f.branches().stream()
                .map(this::toBranch)
                .toList();
        return new FranchiseTreeRS.Data(f.id(), f.name(), branches);
    }

    private FranchiseTreeRS.Branch toBranch(BranchTreeItem b) {
        List<FranchiseTreeRS.Product> products = b.products() == null ? List.of() : b.products().stream()
                .map(this::toProduct)
                .toList();
        return new FranchiseTreeRS.Branch(b.id(), b.name(), products);
    }

    private FranchiseTreeRS.Product toProduct(ProductTreeItem p) {
        return new FranchiseTreeRS.Product(p.id(), p.name(), p.stock());
    }
}