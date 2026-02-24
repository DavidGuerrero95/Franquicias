package co.com.nequi.usecase.product;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.ProductRename;
import co.com.nequi.model.product.gateways.ProductGateway;
import co.com.nequi.model.product.query.ProductIdQuery;
import co.com.nequi.model.product.query.ProductNameQuery;
import co.com.nequi.usecase.util.TxUtil;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RenameProductUseCase {

    private final ProductGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        var cmd = (ProductRename) tx.getRequest();
        var byIdTx = TxUtil.next(tx, new ProductIdQuery(cmd.id()));

        return gateway.findById(byIdTx)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_FOUND)))
                .flatMap(foundTx -> validateUniqueName(tx, cmd, (Product) foundTx.getResponse()))
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(200);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }

    private Mono<Transaction> validateUniqueName(Transaction tx, ProductRename cmd, Product found) {
        var byNameTx = TxUtil.next(tx, new ProductNameQuery(found.branchId(), cmd.name()));

        return gateway.findByName(byNameTx)
                .flatMap(existingTx -> {
                    var existing = (Product) existingTx.getResponse();
                    boolean taken = existing != null && !existing.id().equals(cmd.id());
                    return taken
                            ? Mono.error(new BusinessException(BusinessExceptionEnum.PRODUCT_ALREADY_EXISTS))
                            : Mono.<Transaction>empty();
                })
                .switchIfEmpty(gateway.rename(tx));
    }
}