package acceptanceTests;

import io.micronaut.core.io.ResourceLoader;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.fail;

@Singleton
public class TestPayloadLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestPayloadLoader.class);

    private final ResourceLoader loader;

    public TestPayloadLoader(ResourceLoader loader) {
        this.loader = loader;
    }

    public String getPayloadFromFile(String path) {
        try {
            return new String(loader.getResourceAsStream(path).orElseThrow().readAllBytes());
        } catch (Exception ioException) {
            LOGGER.error("Couldn't fetch payload from path: {}, error: {}",
                    path,
                    ioException.getMessage());
        }
        return fail("Unreachable code!");
    }
}
