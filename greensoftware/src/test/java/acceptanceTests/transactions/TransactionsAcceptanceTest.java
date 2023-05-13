package acceptanceTests.transactions;

import acceptanceTests.TestPayloadLoader;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestInstance(PER_CLASS)
public class TransactionsAcceptanceTest {

    private final static String REPORT_PATH = "/transactions/report";

    private final static Map<String, String> listOfExamples = Map.of(
            "payloads/transactions/example_request.json", "payloads/transactions/example_response.json");
    @Inject
    private TestPayloadLoader testPayloadLoader;

    @Inject
    private RequestSpecification spec;

    @ParameterizedTest
    @MethodSource("provideRequestPayloadsAndExpectedResponse")
    public void When_Post_OnlinegameCalculate_Expect_ProperResponse(String requestBody, String expectedBody) {
        spec
                .given()
                    .body(requestBody)
                    .contentType(ContentType.JSON)
                .when()
                    .post(REPORT_PATH)
                .then()
                    .statusCode(HttpStatus.OK.getCode())
                    .body(is(expectedBody));
    }

    private Stream<Arguments> provideRequestPayloadsAndExpectedResponse() {
        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();
        listOfExamples.forEach(
                (request, response) ->
                        argumentsBuilder.add(
                                Arguments.of(testPayloadLoader.getPayloadFromFile(request),
                                        removeWhiteSpacesAndWindowsCharacters(
                                                testPayloadLoader.getPayloadFromFile(response)))
                        )
        );
        return argumentsBuilder.build();
    }

    private static String removeWhiteSpacesAndWindowsCharacters(String text) {
        return text.replaceAll("\\s+", "");
    }
}
