package com.ing.atms.mappers;

import com.ing.model.atms.Task;
import jakarta.inject.Singleton;

@Singleton
public class RequestTypeEnumMapper {

    public Integer getRequestTypeCriticality(Task.RequestTypeEnum requestTypeEnum) {
        return switch (requestTypeEnum) {
            case FAILURE_RESTART -> 0;
            case PRIORITY -> 1;
            case SIGNAL_LOW -> 2;
            case STANDARD -> 3;
        };
    };
}
