package co.com.nequi.usecase.franchise;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.command.FranchiseCreate;
import co.com.nequi.model.franchise.gateways.FranchiseGateway;
import co.com.nequi.model.franchise.query.FranchiseNameQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (FranchiseCreate) tx.getRequest();
        var byNameTx = TxUtil.next(tx, new FranchiseNameQuery(cmd.name()));

        return gateway.findByName(byNameTx)
                .hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(BusinessExceptionEnum.FRANCHISE_ALREADY_EXISTS))
                        : gateway.create(tx)
                )
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(201);
                    out.setResponse(out.getResponse());
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }

}