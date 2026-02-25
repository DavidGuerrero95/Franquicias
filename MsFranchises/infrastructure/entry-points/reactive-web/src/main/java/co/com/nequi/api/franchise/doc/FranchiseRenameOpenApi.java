package co.com.nequi.api.franchise.doc;

import co.com.nequi.api.commons.openapi.OpenApiDoc;
import co.com.nequi.api.franchise.dto.FranchiseRS;
import co.com.nequi.commons.constants.Constants;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public final class FranchiseRenameOpenApi {

    private static final String TAG = "Franchise";
    private static final String OP_ID = "/ms-franchises/api/v1/franchises/{franchiseId}/name";
    private static final String SUMMARY = "Actualizar nombre de una franquicia";
    private static final String DESCRIPTION = "Actualiza el nombre de una franquicia existente";

    private FranchiseRenameOpenApi() {
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
                .response(responseBuilder()
                        .responseCode(Constants.SUCCESSFUL)
                        .description("operación exitosa")
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(FranchiseRS.class))))
                .response(OpenApiDoc.getTechnicalError())
                .response(OpenApiDoc.getBusinessError())
                .build();
    }
}