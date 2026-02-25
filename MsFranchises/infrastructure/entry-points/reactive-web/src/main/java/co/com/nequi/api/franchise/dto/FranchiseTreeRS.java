package co.com.nequi.api.franchise.dto;

import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.dto.MetaDataResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record FranchiseTreeRS(
        MetaDataResponse meta,
        List<Data> data,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ErrorRes.Data> errors) {
    public record Data(
            Long id,
            String name,
            List<Branch> branches
    ) {
    }

    public record Branch(
            Long id,
            String name,
            List<Product> products
    ) {
    }

    public record Product(
            Long id,
            String name,
            Integer stock
    ) {
    }
}