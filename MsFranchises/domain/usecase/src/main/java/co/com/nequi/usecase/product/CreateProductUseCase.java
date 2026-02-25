package co.com.nequi.usecase.product;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.branch.gateways.BranchGateway;
import co.com.nequi.model.branch.query.BranchIdQuery;
import co.com.nequi.model.product.command.ProductCreate;
import co.com.nequi.model.product.gateways.ProductGateway;
import co.com.nequi.model.product.query.ProductNameQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateProductUseCase {

    private final BranchGateway branchGateway;
    private final ProductGateway productGateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (ProductCreate) tx.getRequest();

        if (cmd.stock() == null || cmd.stock() < 0) {
            return Mono.error(new BusinessException(BusinessExceptionEnum.STOCK_NEGATIVE));
        }

        var branchTx = TxUtil.next(tx, new BranchIdQuery(cmd.branchId()));
        var byNameTx = TxUtil.next(tx, new ProductNameQuery(cmd.branchId(), cmd.name()));

        return branchGateway.findById(branchTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.BRANCH_NOT_FOUND)))
                .then(productGateway.findByName(byNameTx).hasElement())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(BusinessExceptionEnum.PRODUCT_ALREADY_EXISTS))
                        : productGateway.create(tx)
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