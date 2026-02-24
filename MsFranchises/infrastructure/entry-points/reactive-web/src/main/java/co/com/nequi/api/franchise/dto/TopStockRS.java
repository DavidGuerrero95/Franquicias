package co.com.nequi.api.franchise.dto;

import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.dto.MetaDataResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record TopStockRS(
        MetaDataResponse meta,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<Data> data,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ErrorRes.Data> errors
) {
    public record Data(
            Long branchId,
            String branchName,
            Long productId,
            String productName,
            Integer stock
    ) { }
}