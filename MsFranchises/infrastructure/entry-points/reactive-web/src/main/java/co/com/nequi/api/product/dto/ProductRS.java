package co.com.nequi.api.product.dto;

import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.dto.MetaDataResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record ProductRS(
        MetaDataResponse meta,
        Data data,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ErrorRes.Data> errors
) {
    public record Data(Long id, Long branchId, String name, Integer stock) { }
}