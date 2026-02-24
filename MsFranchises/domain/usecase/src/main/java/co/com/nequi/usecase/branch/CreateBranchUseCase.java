package co.com.nequi.usecase.branch;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.BranchCreate;
import co.com.nequi.model.branch.gateways.BranchGateway;
import co.com.nequi.model.branch.query.BranchNameQuery;
import co.com.nequi.model.franchise.gateways.FranchiseGateway;
import co.com.nequi.model.franchise.query.FranchiseIdQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateBranchUseCase {

    private final FranchiseGateway franchiseGateway;
    private final BranchGateway branchGateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (BranchCreate) tx.getRequest();

        var franchiseTx = TxUtil.next(tx, new FranchiseIdQuery(cmd.franchiseId()));
        var byNameTx = TxUtil.next(tx, new BranchNameQuery(cmd.franchiseId(), cmd.name()));

        return franchiseGateway.findById(franchiseTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.FRANCHISE_NOT_FOUND)))
                .then(branchGateway.findByName(byNameTx).hasElement())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(BusinessExceptionEnum.BRANCH_ALREADY_EXISTS))
                        : branchGateway.create(tx)
                )
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(201);
                    out.setResponse((Branch) out.getResponse());
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }
}