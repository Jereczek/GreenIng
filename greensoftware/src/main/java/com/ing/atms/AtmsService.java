package com.ing.atms;

import com.google.common.collect.ImmutableSortedSet;
import com.ing.atms.mappers.TaskMapper;
import com.ing.model.atms.ATM;
import com.ing.model.atms.Task;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class AtmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtmsService.class);

    @Inject
    private TaskMapper taskMapper;

    public Set<ATM> calculateOrderOfAtms(List<Task> tasks) {
        LOGGER.debug("Calculating order of {} atms", tasks.size());
        return ImmutableSortedSet.copyOf(taskMapper.getTaskPriorityOrdering(), tasks)
                .parallelStream()
                .map(taskMapper::mapToAtm)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // Removes less prioritized redundancies
    }
}
