package com.ing.atms.mappers;

import com.ing.model.atms.Task;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.ing.model.atms.Task.RequestTypeEnum.*;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class RequestTypeEnumMapperTest {
    private static final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private RequestTypeEnumMapper requestTypeEnumMapper;

    @ParameterizedTest
    @MethodSource("provideRequestTypeEnumsAndExpectedPriorities")
    public void When_GetRequestTypeCriticality_ExpectCriticalityAsIntegers(Task task, int expectedPriority) {
        assertThat(requestTypeEnumMapper.getRequestTypeCriticality(task)).isEqualTo(expectedPriority);
    }

    private static Stream<Arguments> provideRequestTypeEnumsAndExpectedPriorities() {
        return Stream.of(
                Arguments.of(prepareTaskWithRequestType(FAILURE_RESTART), 0),
                Arguments.of(prepareTaskWithRequestType(PRIORITY), 1),
                Arguments.of(prepareTaskWithRequestType(SIGNAL_LOW), 2),
                Arguments.of(prepareTaskWithRequestType(STANDARD), 3)
        );
    }

    private static Task prepareTaskWithRequestType(Task.RequestTypeEnum requestTypeEnum) {
        Task task = easyRandom.nextObject(Task.class);
        task.setRequestType(requestTypeEnum);
        return task;
    }
}
