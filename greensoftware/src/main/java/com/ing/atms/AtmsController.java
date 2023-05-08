package com.ing.atms;

import com.ing.model.atms.ATM;
import com.ing.model.atms.Task;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.List;

@Controller("/atms")
public class AtmsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtmsController.class);

    @Inject
    private AtmsService atmsService;

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            uri = "/calculateOrder")
    public LinkedHashSet<ATM> calculate(@Body List<Task> tasks) {
        return atmsService.calculateOrderOfAtms(tasks);
    }
}
