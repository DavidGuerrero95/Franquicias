package co.com.nequi.r2dbc.branch;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.BranchCreate;
import co.com.nequi.model.branch.command.BranchRename;
import co.com.nequi.model.branch.gateways.BranchGateway;
import co.com.nequi.model.branch.query.BranchIdQuery;
import co.com.nequi.model.branch.query.BranchNameQuery;
import co.com.nequi.r2dbc.helper.AbstractReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class BranchDataAdapter extends AbstractReactiveAdapterOperations<Branch, BranchData, Long, BranchRepository>
        implements BranchGateway {

    public BranchDataAdapter(BranchRepository repository, ObjectMapper mapper) {
        super(repository, mapper, data -> new Branch(data.id(), data.franchiseId(), data.name()));
    }

    @Override
    public Mono<Transaction> create(Transaction tx) {
        var cmd = (BranchCreate) tx.getRequest();
        var now = Instant.now();

        var data = new BranchData(null, cmd.franchiseId(), cmd.name(), now, now);

        return repository.save(data)
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> rename(Transaction tx) {
        var cmd = (BranchRename) tx.getRequest();
        var now = Instant.now();

        return repository.findById(cmd.id())
                .flatMap(current -> repository.save(new BranchData(current.id(), current.franchiseId(),
                        cmd.name(), current.createdAt(), now)))
                .map(this::toEntity)
                .map(saved -> {
                    tx.setResponse(saved);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> findById(Transaction tx) {
        var q = (BranchIdQuery) tx.getRequest();
        return repository.findById(q.id())
                .map(this::toEntity)
                .map(found -> {
                    tx.setResponse(found);
                    return tx;
                });
    }

    @Override
    public Mono<Transaction> findByName(Transaction tx) {
        var q = (BranchNameQuery) tx.getRequest();
        return repository.findByFranchiseIdAndName(q.franchiseId(), q.name())
                .map(this::toEntity)
                .map(found -> {
                    tx.setResponse(found);
                    return tx;
                });
    }
}