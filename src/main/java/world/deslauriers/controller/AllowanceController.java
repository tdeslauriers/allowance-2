package world.deslauriers.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.service.AllowanceService;

import javax.validation.Valid;
import java.net.URI;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/allowances")
public class AllowanceController {

    protected final AllowanceService allowanceService;

    public AllowanceController(AllowanceService allowanceService) {
        this.allowanceService = allowanceService;
    }

    @Get
    Flux<Allowance> getAll(){
        return allowanceService.findAll();
    }
    @Secured({"ALLOWANCE_ADMIN", "ALLOWANCE_USER"})
    @Get("/{id}")
    Mono<Allowance> getTasksByAllowanceId(Long id){
        return allowanceService.findTasktypesByAllowanceId(id);
    }

    @Secured({"ALLOWANCE_ADMIN", "ALLOWANCE_USER"})
    @Post
    Mono<HttpResponse<Allowance>> save(@Body Allowance cmd){
        return allowanceService
                .save(cmd)
                .map(allowance -> HttpResponse.created(allowance)
                        .headers(headers -> headers.location(location(allowance.getId()))));
    }

    protected URI location(Long id){
        return URI.create("/allowances/" + id);
    }

    protected URI location(Allowance allowance){
        return location(allowance.getId());
    }
}
