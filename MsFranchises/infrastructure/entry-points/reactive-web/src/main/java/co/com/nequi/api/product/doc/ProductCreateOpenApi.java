package co.com.nequi.api.product.doc;

import co.com.nequi.api.commons.openapi.OpenApiDoc;
import co.com.nequi.api.product.dto.ProductRS;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public final class ProductCreateOpenApi {

    private static final String TAG = "Product";
    private static final String OP_ID = "/ms-franchises/api/v1/branches/{branchId}/products";
    private static final String SUMMARY = "Agregar producto a una sucursal";
    private static final String DESCRIPTION = "Crea un producto con stock inicial";

    private ProductCreateOpenApi() {
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
                        .responseCode("201")
                        .description("creado")
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ProductRS.class))))
                .response(OpenApiDoc.getTechnicalError())
                .response(OpenApiDoc.getBusinessError())
                .build();
    }
}