package co.com.nequi.api.branch.mapper;

import co.com.nequi.api.branch.dto.BranchRS;
import co.com.nequi.dto.MetaDataResponse;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.BranchCreate;
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
public interface BranchMapper {

    private static MetaDataResponse meta(Transaction tx) {
        var messageId = Optional.ofNullable(tx.getHeaders()).map(h -> h.get("message-id")).orElse(null);
        return MetaDataResponse.builder()
                .messageId(messageId)
                .requestDateTime(LocalDateTime.now().toString())
                .applicationId("MsFranchises")
                .build();
    }

    default BranchCreate toCreate(Long franchiseId, String name) {
        return new BranchCreate(franchiseId, name);
    }

    default BranchRS toRs(Transaction tx) {
        var branch = (Branch) tx.getResponse();
        return new BranchRS(
                meta(tx),
                branch == null ? null : new BranchRS.Data(branch.id(), branch.franchiseId(), branch.name()),
                null
        );
    }
}