package co.com.nequi.api.commons.openapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenApiConfigTest {

    OpenApiConfig openApiConfig;

    @BeforeEach
    public void setup(){
        openApiConfig = new OpenApiConfig();
    }

    @Test
    void validateCustomOpenAPIOk() {
        Assertions.assertNotNull(openApiConfig.customOpenAPI("test"));
    }

}
