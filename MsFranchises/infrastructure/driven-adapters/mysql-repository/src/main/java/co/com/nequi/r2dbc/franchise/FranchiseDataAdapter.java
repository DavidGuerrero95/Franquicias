package co.com.nequi.r2dbc.franchise;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.command.FranchiseCreate;
import co.com.nequi.model.franchise.command.FranchiseRename;
import co.com.nequi.model.franchise.gateways.FranchiseGateway;
import co.com.nequi.model.franchise.query.FranchiseIdQuery;
import co.com.nequi.model.franchise.query.FranchiseNameQuery;
import co.com.nequi.r2dbc.helper.AbstractReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class FranchiseDataAdapter
        extends AbstractReactiveAdapterOperations<Franchise, FranchiseData, Long, FranchiseRepository>
        implements FranchiseGateway {

    public FranchiseDataAdapter(FranchiseRepository repository, ObjectMapper mapper) {
        super(repository, mapper, data -> new Franchise(data.id(), data.name()));
    }

    @Override
    public Mono<Transaction> create(Transaction tx) {
        var cmd = (FranchiseCreate) tx.getRequest();
        var now = Instant.now();

        var data = new FranchiseData(null, cmd.name(), now, now);

        return repository.save(data)
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> rename(Transaction tx) {
        var cmd = (FranchiseRename) tx.getRequest();
        var now = Instant.now();

        return repository.findById(cmd.id())
                .flatMap(current -> repository.save(new FranchiseData(current.id(), cmd.name(), current.createdAt(), now)))
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> findById(Transaction tx) {
        var q = (FranchiseIdQuery) tx.getRequest();
        return repository.findById(q.id())
                .map(this::toEntity)
                .map(found -> {
                    tx.setResponse(found);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> findByName(Transaction tx) {
        var q = (FranchiseNameQuery) tx.getRequest();
        return repository.findByName(q.name())
                .map(this::toEntity)
                .map(found -> {
                    tx.setResponse(found);
                    return tx;
                });
    }
}