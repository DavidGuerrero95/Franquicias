package co.com.nequi.api.products.doc;

import co.com.nequi.api.commons.openapi.OpenApiDoc;
import co.com.nequi.api.products.dto.ProductDetailRS;
import co.com.nequi.commons.constants.Constants;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public class ProductDetailOpenApi {

    private static final String TAG = "RetrieveProductDetail";
    private static final String OP_ID = "/ms-franchises/api/v1/products/detail";
    private static final String SUMMARY = "Consultar detalle de un artículo";
    private static final String DESCRIPTION = "Devuelve la información completa del producto";

    private ProductDetailOpenApi() {
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
                                .schema(schemaBuilder().implementation(ProductDetailRS.class))))
                .response(OpenApiDoc.getTechnicalError())
                .response(OpenApiDoc.getBusinessError())
                .build();
    }

}