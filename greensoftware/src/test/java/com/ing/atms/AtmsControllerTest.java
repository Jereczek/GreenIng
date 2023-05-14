package com.ing.atms;

import com.ing.model.atms.ATM;
import com.ing.model.atms.Task;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MicronautTest
public class AtmsControllerTest {
    private static final int COLLECTION_SIZE = 3;
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private AtmsService atmsService;

    @Inject
    private AtmsController atmsController;

    @Test
    public void When_AtmsControllerCalled_Expect_AtmsServiceExecuted() {
        List<Task> taskList = easyRandom.objects(Task.class, COLLECTION_SIZE).toList();
        LinkedHashSet<ATM> atms = easyRandom.objects(ATM.class, COLLECTION_SIZE)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        when(atmsService.calculateOrderOfAtms(taskList)).thenReturn(atms);

        Set<ATM> result =  atmsController.calculate(taskList);
        verify(atmsService).calculateOrderOfAtms(taskList);
        assertThat(result).isEqualTo(atms);
    }

    @Test
    public void When_AtmsControllerCalled_And_AtmsServiceThrowUncheckException_Expect_Rethrow() {
        List<Task> taskList = easyRandom.objects(Task.class, COLLECTION_SIZE).toList();
        when(atmsService.calculateOrderOfAtms(taskList)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> atmsController.calculate(taskList));
    }

    @MockBean(AtmsService.class)
    AtmsService atmsService() {
        return mock(AtmsService.class);
    }
}
