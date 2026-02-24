package co.com.nequi.api.commons.openapi;

import co.com.nequi.api.commons.util.ConstantsOpenApi;
import co.com.nequi.commons.constants.Constants;
import co.com.nequi.commons.error.ErrorRes;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.content.Builder;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public class OpenApiDoc {

    private OpenApiDoc() {
        // Constructor privado para evitar instanciación
    }

    public static org.springdoc.core.fn.builders.parameter.Builder getHeaderMessageId() {
        return parameterBuilder().in(ParameterIn.HEADER).name(Constants.MESSAGE_ID)
                .description("ID para trazabilidad de la transacción. Debe ser proporcionado " +
                        "por el front en formato UUID")
                .schema(schemaBuilder().type(Constants.EMPTY).example("8348c30c-1296-4882-84b8-d7306205ce26"))
                .required(true);
    }


    public static org.springdoc.core.fn.builders.parameter.Builder getHeaderContentType() {
        return parameterBuilder().in(ParameterIn.HEADER).name(Constants.CONTENT_TYPE)
                .description("Tipo de contenido enviado en la petición")
                .schema(schemaBuilder().type(Constants.EMPTY).example("application/json"))
                .required(true);
    }

    public static org.springdoc.core.fn.builders.parameter.Builder getHeaderAccept() {
        return parameterBuilder().in(ParameterIn.HEADER).name(Constants.ACCEPT)
                .description("el cual representa el formato de mensajería.")
                .schema(schemaBuilder().type(Constants.EMPTY).example("application/json"))
                .required(true);
    }

    public static org.springdoc.core.fn.builders.apiresponse.Builder getTechnicalError() {
        return responseBuilder()
                .responseCode(Constants.INTERNAL_SERVER_ERROR)
                .description("Technical error")
                .content(Builder.contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                        .schema(schemaBuilder().implementation(ErrorRes.Data.class))
                        .example(exampleTechnicalError()));
    }

    public static org.springdoc.core.fn.builders.apiresponse.Builder getBusinessError() {
        return responseBuilder()
                .responseCode(Constants.CONFLICT)
                .description("Business error")
                .content(Builder.contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                        .schema(schemaBuilder().implementation(ErrorRes.Data.class))
                        .example(exampleBusinessError()));
    }

    private static org.springdoc.core.fn.builders.exampleobject.Builder exampleBusinessError() {
        return org.springdoc.core.fn.builders.exampleobject.Builder.exampleOjectBuilder()
                .value(ConstantsOpenApi.RESPONSE_ERROR_BUSINESS);
    }

    private static org.springdoc.core.fn.builders.exampleobject.Builder exampleTechnicalError() {
        return org.springdoc.core.fn.builders.exampleobject.Builder.exampleOjectBuilder()
                .value(ConstantsOpenApi.RESPONSE_ERROR_TECHNICAL);
    }
}
