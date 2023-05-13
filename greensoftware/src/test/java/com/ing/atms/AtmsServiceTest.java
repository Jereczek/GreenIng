package com.ing.atms;

import com.google.common.collect.Ordering;
import com.ing.atms.mappers.TaskMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
public class AtmsServiceTest {
    private static final int COLLECTION_SIZE = 3;
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private TaskMapper taskMapper;

    @Inject
    private AtmsService atmsService;

    @Test
    public void When_CalculateOrderOfAtms_Expect_ProcessAndReturnSortedAtms() {
        List<Task> taskList = prepareAndMockExplicitOrdering();
        LinkedHashSet<ATM> atms = prepareAndMockAtmsForGivenTaskList(taskList);

        Set<ATM> calculateOrderOfAtms =  atmsService.calculateOrderOfAtms(taskList);

        assertThat(calculateOrderOfAtms).isInstanceOf(LinkedHashSet.class);
        assertThat(calculateOrderOfAtms.size()).isEqualTo(COLLECTION_SIZE);
        assertThat(calculateOrderOfAtms).isEqualTo(atms);
        verifyAllTasksHasBennProcessed(taskList);
        verifySortedSetHasBeenCreatedWithTaskOrdering();
    }

    @Test
    public void When_CalculateOrderOfAtmsAndThereAreAtmDuplicates_Expect_ProcessAndGetOnlyDistinctAtms() {
        Task task1 = easyRandom.nextObject(Task.class);
        Task task2 = easyRandom.nextObject(Task.class);
        Integer sameAtmId = easyRandom.nextInt();
        Integer sameRegion = easyRandom.nextInt();
        ATM atm1 = prepareAtm(sameAtmId, sameRegion);
        ATM atm2 = prepareAtm(sameAtmId, sameRegion);
        when(taskMapper.mapToAtm(task1)).thenReturn(atm1);
        when(taskMapper.mapToAtm(task2)).thenReturn(atm2);
        when(taskMapper.getTaskPriorityOrdering()).thenReturn(Ordering.explicit(List.of(task1, task2)));

        Set<ATM> calculateOrderOfAtms =  atmsService.calculateOrderOfAtms(List.of(task1, task2));

        assertThat(calculateOrderOfAtms).isInstanceOf(LinkedHashSet.class);
        assertThat(calculateOrderOfAtms.size()).isEqualTo(1);
        assertThat(calculateOrderOfAtms.stream().findAny().orElseThrow()).isEqualTo(atm1);
    }

    private LinkedHashSet<ATM> prepareAndMockAtmsForGivenTaskList(List<Task> taskList) {
        LinkedHashSet<ATM> atms = new LinkedHashSet<>();
        taskList.forEach(
                task -> {
                    ATM atm = easyRandom.nextObject(ATM.class);
                    atms.add(atm);
                    when(taskMapper.mapToAtm(task)).thenReturn(atm);
                }
        );
        return atms;
    }

    private void verifyAllTasksHasBennProcessed(List<Task> taskList) {
        taskList.forEach(
                task -> {
                    verify(taskMapper).mapToAtm(task);
                }
        );
    }

    private ATM prepareAtm(Integer atmId, Integer region) {
        ATM atm = new ATM();
        atm.setAtmId(atmId);
        atm.setRegion(region);
        return atm;
    }

    private void verifySortedSetHasBeenCreatedWithTaskOrdering() {
        verify(taskMapper).getTaskPriorityOrdering();
    }

    private List<Task> prepareAndMockExplicitOrdering() {
        List<Task> taskList = easyRandom.objects(Task.class, COLLECTION_SIZE).toList();
        when(taskMapper.getTaskPriorityOrdering()).thenReturn(Ordering.explicit(taskList));
        return taskList;
    }

    @MockBean(TaskMapper.class)
    TaskMapper taskMapper() {
        return mock(TaskMapper.class);
    }
}
