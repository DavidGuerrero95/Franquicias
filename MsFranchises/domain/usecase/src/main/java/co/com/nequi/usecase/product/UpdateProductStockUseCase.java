package co.com.nequi.usecase.product;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.command.ProductStockUpdate;
import co.com.nequi.model.product.gateways.ProductGateway;
import co.com.nequi.model.product.query.ProductIdQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (ProductStockUpdate) tx.getRequest();
        if (cmd.stock() == null || cmd.stock() < 0) {
            return Mono.error(new BusinessException(BusinessExceptionEnum.STOCK_NEGATIVE));
        }

        var byIdTx = TxUtil.next(tx, new ProductIdQuery(cmd.id()));

        return gateway.findById(byIdTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_FOUND)))
                .then(gateway.updateStock(tx))
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(200);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }
}