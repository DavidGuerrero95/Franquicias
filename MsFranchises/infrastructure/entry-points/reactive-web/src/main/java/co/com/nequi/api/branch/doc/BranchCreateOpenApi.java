package co.com.nequi.api.branch.doc;

import co.com.nequi.api.branch.dto.BranchRS;
import co.com.nequi.api.commons.openapi.OpenApiDoc;
import co.com.nequi.commons.constants.Constants;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public final class BranchCreateOpenApi {

    private static final String TAG = "Branch";
    private static final String OP_ID = "/ms-franchises/api/v1/franchises/{franchiseId}/branches";
    private static final String SUMMARY = "Agregar sucursal a una franquicia";
    private static final String DESCRIPTION = "Crea una sucursal asociada a una franquicia existente";

    private BranchCreateOpenApi() { }

    public static Consumer<Builder> getOpenAPI() {
        return ops -> ops.tag(TAG)
                .operationId(OP_ID)
                .summary(SUMMARY)
                .description(DESCRIPTION)
                .parameter(OpenApiDoc.getHeaderMessageId())
                .parameter(OpenApiDoc.getHeaderContentType())
                .parameter(OpenApiDoc.getHeaderAccept())
                .response(responseBuilder()
                        .responseCode("201")
                        .description("creado")
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(BranchRS.class))))
                .response(OpenApiDoc.getTechnicalError())
                .response(OpenApiDoc.getBusinessError())
                .build();
    }
}