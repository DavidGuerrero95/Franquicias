package co.com.nequi.usecase.product;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.ProductDelete;
import co.com.nequi.model.product.gateways.ProductGateway;
import co.com.nequi.model.product.query.ProductIdQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (ProductDelete) tx.getRequest();
        var byIdTx = TxUtil.next(tx, new ProductIdQuery(cmd.productId()));

        return gateway.findById(byIdTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_FOUND)))
                .flatMap(foundTx -> {
                    var found = (Product) foundTx.getResponse();
                    if (found == null || !cmd.branchId().equals(found.branchId())) {
                        return Mono.error(new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_FOUND));
                    }
                    return gateway.delete(tx);
                })
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(204);
                    out.setResponse(null);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }
}