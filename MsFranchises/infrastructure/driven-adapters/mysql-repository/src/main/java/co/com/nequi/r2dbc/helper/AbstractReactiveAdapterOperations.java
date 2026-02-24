package co.com.nequi.r2dbc.helper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractReactiveAdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I> &
        ReactiveQueryByExampleExecutor<D>> {

    protected final R repository;
    protected final ObjectMapper mapper;
    private final Class<D> dataClass;
    private final Function<D, E> toEntityFn;

    protected AbstractReactiveAdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "ObjectMapper cannot be null");
        this.toEntityFn = Objects.requireNonNull(toEntityFn, "Entity mapper function cannot be null");
        this.dataClass = resolveDataClass();
    }

    @SuppressWarnings("unchecked")
    private Class<D> resolveDataClass() {
        return (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected D toData(E entity) {
        return mapper.map(entity, dataClass);
    }

    protected E toEntity(D data) {
        return data != null ? toEntityFn.apply(data) : null;
    }

    public Mono<E> save(E entity) {
        return repository.save(toData(entity))
                .map(this::toEntity);
    }

    protected Flux<E> saveAllEntities(Flux<E> entities) {
        return repository.saveAll(entities.map(this::toData))
                .map(this::toEntity);
    }

    public Mono<E> findById(I id) {
        return repository.findById(id)
                .map(this::toEntity);
    }

    public Flux<E> findByExample(E entity) {
        return repository.findAll(Example.of(toData(entity)))
                .map(this::toEntity);
    }

    public Flux<E> findAll() {
        return repository.findAll()
                .map(this::toEntity);
    }
}