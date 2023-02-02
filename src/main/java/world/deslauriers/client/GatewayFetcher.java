package world.deslauriers.client;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Mono;
import world.deslauriers.client.dto.LoginRequest;
import world.deslauriers.client.dto.LoginResponse;
import world.deslauriers.client.dto.Profile;

public interface GatewayFetcher {

    Mono<LoginResponse> login(LoginRequest loginRequest);
    Mono<Profile> getUserProfileByUuid(String bearer, String uuid);
}
