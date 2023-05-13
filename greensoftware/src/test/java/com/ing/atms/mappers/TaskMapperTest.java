package com.ing.atms.mappers;

import com.google.common.collect.Ordering;
import com.ing.model.atms.ATM;
import com.ing.model.atms.Task;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
public class TaskMapperTest {

    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private RequestTypeEnumMapper requestTypeEnumMapper;

    @Inject
    private TaskMapper taskMapper;


    @Test
    public void When_MapToAtm_Expect_ProperMapping() {
        Task task = easyRandom.nextObject(Task.class);

        ATM mappedAtm = taskMapper.mapToAtm(task);
        assertThat(mappedAtm.getAtmId()).isEqualTo(task.getAtmId());
        assertThat(mappedAtm.getRegion()).isEqualTo(task.getRegion());
    }

    @Test
    public void When_GetTaskPriorityOrdering_Expect_ProperOrderingByRegionFirst() {
        Task task1 = prepareTask(easyRandom.nextInt(), 3, easyRandom.nextInt());
        Task task2 = prepareTask(easyRandom.nextInt(), 1, easyRandom.nextInt());
        Task task3 = prepareTask(easyRandom.nextInt(), 2, easyRandom.nextInt());

        List<Task> toSortByRegion = Arrays.asList(task1, task2, task3);
        toSortByRegion.sort(taskMapper.getTaskPriorityOrdering());

        Ordering<Task> expectedOrder = Ordering.explicit(List.of(task2, task3, task1));
        assertThat(expectedOrder.isOrdered(toSortByRegion)).isTrue();
    }

    @Test
    public void When_GetTaskPriorityOrdering_Expect_ProperOrderingByCriticalitySecond() {
        Task task1 = prepareTask(easyRandom.nextInt(), 1, 3);
        Task task2 = prepareTask(easyRandom.nextInt(), 1, 1);
        Task task3 = prepareTask(easyRandom.nextInt(), 1, 2);

        List<Task> toSortByCriticality = Arrays.asList(task1, task2, task3);
        toSortByCriticality.sort(taskMapper.getTaskPriorityOrdering());

        Ordering<Task> expectedOrder = Ordering.explicit(List.of(task2, task3, task1));
        assertThat(expectedOrder.isOrdered(toSortByCriticality)).isTrue();
    }

    @Test
    public void When_GetTaskPriorityOrdering_Expect_ProperOrderingLeaveInputOrderWhenRegionAndCriticalityIsTheSame() {
        Task task1 = prepareTask(3, 1, 3);
        Task task2 = prepareTask(1, 1, 3);
        Task task3 = prepareTask(2, 1, 3);

        List<Task> toSortByCriticality = Arrays.asList(task1, task2, task3);
        toSortByCriticality.sort(taskMapper.getTaskPriorityOrdering());

        Ordering<Task> expectedOrder = Ordering.explicit(List.of(task1, task2, task3));
        assertThat(expectedOrder.isOrdered(toSortByCriticality)).isTrue();
    }

    @Test
    public void When_GetTaskPriorityOrdering_Expect_IfDuplicatesKeepsInputOrder() {
        Task task1 = prepareTask(1, 1, 1);
        Task task2 = prepareTask(1, 1, 1);
        Task task3 = prepareTask(1, 1, 1);

        List<Task> toSortByCriticality = Arrays.asList(task1, task2, task3);
        toSortByCriticality.sort(taskMapper.getTaskPriorityOrdering());

        Ordering<Task> expectedOrder = Ordering.explicit(List.of(task1, task2, task3));
        assertThat(expectedOrder.isOrdered(toSortByCriticality)).isTrue();
    }

    private Task prepareTask(Integer atmId, Integer region, Integer criticality) {
        Task task = easyRandom.nextObject(Task.class);
        task.setAtmId(atmId);
        task.setRegion(region);
        when(requestTypeEnumMapper.getRequestTypeCriticality(task)).thenReturn(criticality);
        return task;
    }


    @MockBean(RequestTypeEnumMapper.class)
    RequestTypeEnumMapper requestTypeEnumMapper() {
        return mock(RequestTypeEnumMapper.class);
    }

}
