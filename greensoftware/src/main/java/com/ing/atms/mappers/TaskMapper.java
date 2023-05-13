package com.ing.atms.mappers;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.ing.model.atms.ATM;
import com.ing.model.atms.Task;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class TaskMapper {
    private static final int LEAVE_LEFT_FIRST = 1;

    @Inject
    private RequestTypeEnumMapper requestTypeEnumMapper;

    public ATM mapToAtm(Task task) {
        ATM atmOne = new ATM();
        atmOne.setAtmId(task.getAtmId());
        atmOne.setRegion(task.getRegion());
        return atmOne;
    }

    public Ordering<Task> getTaskPriorityOrdering() {
        return new Ordering<>() {
            @Override
            public int compare(Task left, Task right) {
                int result = ComparisonChain.start()
                        .compare(
                                Objects.requireNonNull(left.getRegion()),
                                Objects.requireNonNull(right.getRegion()))
                        .compare(
                                requestTypeEnumMapper.getRequestTypeCriticality(Objects.requireNonNull(left)),
                                requestTypeEnumMapper.getRequestTypeCriticality(Objects.requireNonNull(right)))
                        .result();
                return result == 0 ? compareAtmIdsButLeaveLeftIfDiffers(left, right) : result;
            }

            private int compareAtmIdsButLeaveLeftIfDiffers(Task left, Task right) {
                if (Objects.equals(left.getAtmId(), right.getAtmId()))
                    return 0;
                else {
                    return LEAVE_LEFT_FIRST;
                }
            }
        };
    }
}
