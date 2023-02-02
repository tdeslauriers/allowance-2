package world.deslauriers.client;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;
import world.deslauriers.client.dto.LoginRequest;
import world.deslauriers.client.dto.LoginResponse;
import world.deslauriers.client.dto.Profile;

@Client(id = "gateway")
public interface GatewayClient extends GatewayFetcher{

    @Override
    @Post("/auth/login")
    Mono<LoginResponse> login(@Body LoginRequest loginRequest);

    @Override
    @Get("/auth/profiles/{uuid}")
    Mono<Profile> getUserProfileByUuid(@Header("Authorization") String bearer, String uuid);
}
