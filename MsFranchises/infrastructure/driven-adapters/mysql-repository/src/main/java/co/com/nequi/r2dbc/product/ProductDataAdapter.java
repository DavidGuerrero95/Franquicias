package co.com.nequi.r2dbc.product;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.ProductCreate;
import co.com.nequi.model.product.command.ProductDelete;
import co.com.nequi.model.product.command.ProductRename;
import co.com.nequi.model.product.command.ProductStockUpdate;
import co.com.nequi.model.product.gateways.ProductGateway;
import co.com.nequi.model.product.query.ProductIdQuery;
import co.com.nequi.model.product.query.ProductNameQuery;
import co.com.nequi.model.product.query.TopStockQuery;
import co.com.nequi.model.product.report.TopStockItem;
import co.com.nequi.r2dbc.helper.AbstractReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class ProductDataAdapter extends AbstractReactiveAdapterOperations<Product, ProductData, Long, ProductRepository>
        implements ProductGateway {

    public ProductDataAdapter(ProductRepository repository, ObjectMapper mapper) {
        super(repository, mapper, data -> new Product(data.id(), data.branchId(), data.name(), data.stock()));
    }

    @Override
    public Mono<Transaction> create(Transaction tx) {
        var cmd = (ProductCreate) tx.getRequest();
        var now = Instant.now();

        var data = new ProductData(null, cmd.branchId(), cmd.name(), cmd.stock(), now, now);

        return repository.save(data)
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> rename(Transaction tx) {
        var cmd = (ProductRename) tx.getRequest();
        var now = Instant.now();

        return repository.findById(cmd.id())
                .flatMap(current -> repository.save(new ProductData(
                        current.id(),
                        current.branchId(),
                        cmd.name(),
                        current.stock(),
                        current.createdAt(),
                        now
                )))
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> updateStock(Transaction tx) {
        var cmd = (ProductStockUpdate) tx.getRequest();
        var now = Instant.now();

        return repository.findById(cmd.id())
                .flatMap(current -> repository.save(new ProductData(
                        current.id(),
                        current.branchId(),
                        current.name(),
                        cmd.stock(),
                        current.createdAt(),
                        now
                )))
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> delete(Transaction tx) {
        var cmd = (ProductDelete) tx.getRequest();
        return repository.deleteById(cmd.productId())
                .thenReturn(tx);
    }

    @Override
    public Mono<Transaction> findById(Transaction tx) {
        var q = (ProductIdQuery) tx.getRequest();
        return repository.findById(q.id())
                .map(this::toEntity)
                .map(found -> {
                    tx.setResponse(found);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> findByName(Transaction tx) {
        var q = (ProductNameQuery) tx.getRequest();
        return repository.findByBranchIdAndName(q.branchId(), q.name())
                .map(this::toEntity)
                .map(found -> {
                    tx.setResponse(found);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> topStockByFranchise(Transaction tx) {
        var q = (TopStockQuery) tx.getRequest();
        return repository.findTopStockByFranchiseId(q.franchiseId())
                .map(row -> new TopStockItem(row.getBranchId(), row.getBranchName(), row.getProductId(), row.getProductName(), row.getStock()))
                .collectList()
                .map(list -> {
                    tx.setResponse(list);
                    return tx;
                });
    }
}