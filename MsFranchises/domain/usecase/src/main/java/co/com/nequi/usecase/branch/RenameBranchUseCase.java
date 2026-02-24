package co.com.nequi.usecase.branch;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.BranchRename;
import co.com.nequi.model.branch.gateways.BranchGateway;
import co.com.nequi.model.branch.query.BranchIdQuery;
import co.com.nequi.model.branch.query.BranchNameQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RenameBranchUseCase {

    private final BranchGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (BranchRename) tx.getRequest();
        var byIdTx = TxUtil.next(tx, new BranchIdQuery(cmd.id()));

        return gateway.findById(byIdTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.BRANCH_NOT_FOUND)))
                .flatMap(foundTx -> validateUniqueName(tx, cmd, (Branch) foundTx.getResponse()))
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(200);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }

    private Mono<Transaction> validateUniqueName(Transaction tx, BranchRename cmd, Branch found) {
        var byNameTx = TxUtil.next(tx, new BranchNameQuery(found.franchiseId(), cmd.name()));

        return gateway.findByName(byNameTx)
                .flatMap(existingTx -> {
                    var existing = (Branch) existingTx.getResponse();
                    boolean taken = existing != null && !existing.id().equals(cmd.id());
                    return taken
                            ? Mono.error(new BusinessException(BusinessExceptionEnum.BRANCH_ALREADY_EXISTS))
                            : Mono.<Transaction>empty();
                })
                .switchIfEmpty(gateway.rename(tx));
    }
}