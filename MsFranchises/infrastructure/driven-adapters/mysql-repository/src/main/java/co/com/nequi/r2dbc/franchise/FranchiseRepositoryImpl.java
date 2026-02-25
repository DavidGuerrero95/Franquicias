package co.com.nequi.r2dbc.franchise;

import co.com.nequi.model.franchise.query.FranchiseTreeQuery;
import co.com.nequi.model.franchise.report.BranchTreeItem;
import co.com.nequi.model.franchise.report.FranchiseTreeItem;
import co.com.nequi.model.franchise.report.FranchiseTreePage;
import co.com.nequi.model.franchise.report.ProductTreeItem;
import co.com.nequi.r2dbc.branch.BranchData;
import co.com.nequi.r2dbc.branch.BranchRepository;
import co.com.nequi.r2dbc.product.ProductData;
import co.com.nequi.r2dbc.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseTreeRepository {

    private final DatabaseClient db;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public Mono<FranchiseTreePage> retrieveTree(FranchiseTreeQuery q) {
        int pageSize = q.pageSize();
        int pageNumber = q.pageNumber();
        long offset = (long) (pageNumber - 1) * pageSize;

        String branchName = q.branchName() == null ? null : q.branchName().trim();
        boolean applyFilter = branchName != null && branchName.length() >= 3;

        Mono<Long> totalMono = db.sql("SELECT COUNT(*) AS total FROM franchise")
                .map((row, meta) -> {
                    Number n = row.get("total", Number.class);
                    return n == null ? 0L : n.longValue();
                })
                .one()
                .defaultIfEmpty(0L);

        Mono<List<FranchiseData>> pageMono = db.sql("""
                        SELECT id, name
                        FROM franchise
                        ORDER BY id
                        LIMIT :limit OFFSET :offset
                        """)
                .bind("limit", pageSize)
                .bind("offset", offset)
                .map((row, meta) -> new FranchiseData(
                        row.get("id", Long.class),
                        row.get("name", String.class),
                        null,
                        null
                ))
                .all()
                .collectList();

        return totalMono.zipWith(pageMono)
                .flatMap(tuple -> {
                    long total = tuple.getT1();
                    List<FranchiseData> franchises = tuple.getT2();

                    int totalPages = total <= 0 ? 0 : (int) Math.ceil(total / (double) pageSize);
                    boolean lastPage = totalPages == 0 || pageNumber >= totalPages;
                    boolean additional = !lastPage;

                    if (franchises.isEmpty()) {
                        return Mono.just(new FranchiseTreePage(
                                total, pageSize, pageNumber, totalPages, lastPage, additional, List.of()
                        ));
                    }

                    List<Long> franchiseIds = franchises.stream()
                            .map(FranchiseData::id)
                            .toList();

                    Flux<BranchData> branchesFlux = applyFilter
                            ? branchRepository.findByFranchiseIdInAndNameContainingOrderByFranchiseIdAscIdAsc(franchiseIds, branchName)
                            : branchRepository.findByFranchiseIdInOrderByFranchiseIdAscIdAsc(franchiseIds);

                    return branchesFlux.collectList()
                            .flatMap(branches -> {
                                List<Long> branchIds = branches.stream().map(BranchData::id).toList();
                                Mono<List<ProductData>> productsMono = branchIds.isEmpty()
                                        ? Mono.just(List.of())
                                        : productRepository.findByBranchIdInOrderByBranchIdAscIdAsc(branchIds).collectList();

                                return productsMono.map(products -> assembleTree(
                                        total, pageSize, pageNumber, totalPages, lastPage, additional,
                                        franchises, branches, products
                                ));
                            });
                });
    }

    private FranchiseTreePage assembleTree(
            long total, int pageSize, int pageNumber, int totalPages, boolean lastPage, boolean additional,
            List<FranchiseData> franchises,
            List<BranchData> branches,
            List<ProductData> products
    ) {
        Map<Long, MutableFranchise> franchiseMap = new LinkedHashMap<>();
        Map<Long, MutableBranch> branchMap = new LinkedHashMap<>();

        for (FranchiseData f : franchises) {
            franchiseMap.put(f.id(), new MutableFranchise(f.id(), f.name()));
        }

        for (BranchData b : branches) {
            MutableFranchise mf = franchiseMap.get(b.franchiseId());
            if (mf != null) {
                MutableBranch mb = new MutableBranch(b.id(), b.name());
                mf.branches.put(b.id(), mb);
                branchMap.put(b.id(), mb);
            }
        }

        for (ProductData p : products) {
            MutableBranch mb = branchMap.get(p.branchId());
            if (mb != null) {
                mb.products.add(new ProductTreeItem(p.id(), p.name(), p.stock()));
            }
        }

        List<FranchiseTreeItem> data = new ArrayList<>();
        for (FranchiseData f : franchises) {
            MutableFranchise mf = franchiseMap.get(f.id());
            List<BranchTreeItem> branchItems = mf == null
                    ? List.of()
                    : mf.branches.values().stream()
                    .map(b -> new BranchTreeItem(b.id, b.name, List.copyOf(b.products)))
                    .toList();

            data.add(new FranchiseTreeItem(f.id(), f.name(), branchItems));
        }

        return new FranchiseTreePage(total, pageSize, pageNumber, totalPages, lastPage, additional, data);
    }

    private static final class MutableFranchise {
        final Long id;
        final String name;
        final Map<Long, MutableBranch> branches = new LinkedHashMap<>();
        MutableFranchise(Long id, String name) { this.id = id; this.name = name; }
    }

    private static final class MutableBranch {
        final Long id;
        final String name;
        final List<ProductTreeItem> products = new ArrayList<>();
        MutableBranch(Long id, String name) { this.id = id; this.name = name; }
    }

}