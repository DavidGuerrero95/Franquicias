package co.com.nequi.api.franchise.doc;

import co.com.nequi.api.commons.openapi.OpenApiDoc;
import co.com.nequi.api.franchise.dto.FranchiseTreeRS;
import co.com.nequi.commons.constants.Constants;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public final class FranchiseTreeOpenApi {

    private static final String TAG = "Franchise";
    private static final String OP_ID = "/ms-franchises/api/v1/franchises/tree";
    private static final String SUMMARY = "Listar árbol de franquicias";
    private static final String DESCRIPTION = "Lista franquicias con sus sucursales y productos. " +
            "Soporta paginación y filtro por nombre de sucursal (>= 3 letras).";

    private FranchiseTreeOpenApi() {
        // Not instance constructor
    }

    public static Consumer<Builder> getOpenAPI() {
        return ops -> ops.tag(TAG)
                .operationId(OP_ID)
                .summary(SUMMARY)
                .description(DESCRIPTION)
                .parameter(OpenApiDoc.getHeaderMessageId())
                .parameter(OpenApiDoc.getHeaderContentType())
                .parameter(OpenApiDoc.getHeaderAccept())
                .parameter(parameterBuilder()
                        .in(ParameterIn.QUERY)
                        .name("pageSize")
                        .required(false)
                        .description("Tamaño de página (1..30). Default: 10")
                        .schema(schemaBuilder().type("integer").example(String.valueOf(10))))
                .parameter(parameterBuilder()
                        .in(ParameterIn.QUERY)
                        .name("pageNumber")
                        .required(false)
                        .description("Número de página (>0). Default: 1")
                        .schema(schemaBuilder().type("integer").example(String.valueOf(1))))
                .parameter(parameterBuilder()
                        .in(ParameterIn.QUERY)
                        .name("branchName")
                        .required(false)
                        .description("Filtro por nombre de sucursal (contiene). Solo aplica si length >= 3")
                        .schema(schemaBuilder().type("string").example("cen")))
                .response(responseBuilder()
                        .responseCode(Constants.SUCCESSFUL)
                        .description("operación exitosa")
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(FranchiseTreeRS.class))))
                .response(OpenApiDoc.getTechnicalError())
                .response(OpenApiDoc.getBusinessError())
                .build();
    }
}