package com.ing.onlinegame;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/onlinegame")
public class OnlineGameController {

    @Inject
    private OnlineGameService onlineGameService;

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            uri = "/calculate")
    public List<List<Clan>> calculate(@Body Players players) {
        return onlineGameService.computeMatchmaking(players);
    }
}
