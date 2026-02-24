package co.com.nequi.api.products;

import co.com.nequi.api.commons.util.RequestUtil;
import co.com.nequi.api.products.dto.ProductDetailRQ;
import co.com.nequi.api.products.mapper.ProductDetailDataMapper;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.usecase.product.RetrieveProductDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductDetailHandler {

    private final RequestUtil requestUtil;
    private final ProductDetailDataMapper mapper;
    private final RetrieveProductDetailUseCase useCase;

    public Mono<ServerResponse> retrieve(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        return request.bodyToMono(ProductDetailRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .request(mapper.toModel(rq))
                        .headers(headers)
                        .build())
                .flatMap(useCase::retrieve)
                .map(mapper::toDto)
                .flatMap(rs -> ServerResponse.ok().bodyValue(rs));
    }

}