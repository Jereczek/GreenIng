package com.ing.onlinegame;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/onlinegame")
public class OnlineGameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineGameController.class);

    @Inject
    private OnlineGameService onlineGameService;

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            uri = "/calculate")
    public List<List<Clan>> calculate(@Body Players players) {
        LOGGER.info("Incoming request | POST | /onlinegame/calculate | Number of clans {}", players.getClans().size());
        return onlineGameService.computeMatchmaking(players);
    }
}
