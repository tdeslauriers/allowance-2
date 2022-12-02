package world.deslauriers.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.service.AllowanceService;
import world.deslauriers.service.dto.TaskDto;

import java.net.URI;

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

    @Get("/{id}")
    Mono<Allowance> getTasksByAllowanceId(Long id){
        return allowanceService.findTasktypesByAllowanceId(id);
    }

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
