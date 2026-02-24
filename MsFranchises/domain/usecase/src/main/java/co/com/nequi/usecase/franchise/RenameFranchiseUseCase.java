package co.com.nequi.usecase.franchise;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.command.FranchiseRename;
import co.com.nequi.model.franchise.gateways.FranchiseGateway;
import co.com.nequi.model.franchise.query.FranchiseIdQuery;
import co.com.nequi.model.franchise.query.FranchiseNameQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RenameFranchiseUseCase {

    private final FranchiseGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (FranchiseRename) tx.getRequest();
        var byIdTx = TxUtil.next(tx, new FranchiseIdQuery(cmd.id()));

        return gateway.findById(byIdTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.FRANCHISE_NOT_FOUND)))
                .flatMap(foundTx -> validateUniqueName(tx, cmd))
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(200);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }

    private Mono<Transaction> validateUniqueName(Transaction tx, FranchiseRename cmd) {
        var byNameTx = TxUtil.next(tx, new FranchiseNameQuery(cmd.name()));

        return gateway.findByName(byNameTx)
                .flatMap(existingTx -> {
                    var existing = (Franchise) existingTx.getResponse();
                    boolean taken = existing != null && !existing.id().equals(cmd.id());
                    return taken
                            ? Mono.error(new BusinessException(BusinessExceptionEnum.FRANCHISE_ALREADY_EXISTS))
                            : Mono.<Transaction>empty();
                })
                .switchIfEmpty(gateway.rename(tx));
    }
}