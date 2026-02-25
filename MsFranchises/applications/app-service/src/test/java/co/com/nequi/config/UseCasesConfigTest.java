package co.com.nequi.config;

import co.com.nequi.model.branch.gateways.BranchGateway;
import co.com.nequi.model.franchise.gateways.FranchiseGateway;
import co.com.nequi.model.franchise.gateways.FranchiseTreeGateway;
import co.com.nequi.model.product.gateways.ProductGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        FranchiseGateway franchiseGateway() {
            return Mockito.mock(FranchiseGateway.class);
        }

        @Bean
        BranchGateway branchGateway() {
            return Mockito.mock(BranchGateway.class);
        }

        @Bean
        ProductGateway productGateway() {
            return Mockito.mock(ProductGateway.class);
        }

        @Bean
        FranchiseTreeGateway franchiseTreeGateway() {
            return Mockito.mock(FranchiseTreeGateway.class);
        }

    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}