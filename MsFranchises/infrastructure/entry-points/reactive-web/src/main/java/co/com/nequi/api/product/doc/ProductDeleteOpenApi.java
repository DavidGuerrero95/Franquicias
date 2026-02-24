package co.com.nequi.api.product.doc;

import co.com.nequi.api.commons.openapi.OpenApiDoc;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;

public final class ProductDeleteOpenApi {

    private static final String TAG = "Product";
    private static final String OP_ID = "/ms-franchises/api/v1/branches/{branchId}/products/{productId}";
    private static final String SUMMARY = "Eliminar producto de una sucursal";
    private static final String DESCRIPTION = "Elimina un producto existente de una sucursal";

    private ProductDeleteOpenApi() { }

    public static Consumer<Builder> getOpenAPI() {
        return ops -> ops.tag(TAG)
                .operationId(OP_ID)
                .summary(SUMMARY)
                .description(DESCRIPTION)
                .parameter(OpenApiDoc.getHeaderMessageId())
                .parameter(OpenApiDoc.getHeaderContentType())
                .parameter(OpenApiDoc.getHeaderAccept())
                .response(responseBuilder()
                        .responseCode("204")
                        .description("sin contenido"))
                .response(OpenApiDoc.getTechnicalError())
                .response(OpenApiDoc.getBusinessError())
                .build();
    }
}