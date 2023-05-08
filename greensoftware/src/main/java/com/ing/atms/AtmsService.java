package com.ing.atms;

import com.google.common.collect.ImmutableSortedSet;
import com.ing.atms.mappers.TaskMapper;
import com.ing.model.atms.ATM;
import com.ing.model.atms.Task;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AtmsService {

    @Inject
    private TaskMapper taskMapper;

    public LinkedHashSet<ATM> calculateOrderOfAtms(List<Task> tasks) {
        return ImmutableSortedSet.copyOf(taskMapper.priorityOrdering, tasks)
                .parallelStream()
                .map(taskMapper::mapToAtm)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // Removes less prioritized redundancies
    }
}
