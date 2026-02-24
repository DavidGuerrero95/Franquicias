package co.com.nequi.usecase.product;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.gateways.FranchiseGateway;
import co.com.nequi.model.franchise.query.FranchiseIdQuery;
import co.com.nequi.model.product.gateways.ProductGateway;
import co.com.nequi.model.product.query.TopStockQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RetrieveTopStockByBranchUseCase {

    private final FranchiseGateway franchiseGateway;
    private final ProductGateway productGateway;

    public Mono<Transaction> execute(Transaction tx) {
        var query = (TopStockQuery) tx.getRequest();

        var franchiseTx = TxUtil.next(tx, new FranchiseIdQuery(query.franchiseId()));
        var topTx = TxUtil.next(tx, query);

        return franchiseGateway.findById(franchiseTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.FRANCHISE_NOT_FOUND)))
                .then(productGateway.topStockByFranchise(topTx))
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(200);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }
}