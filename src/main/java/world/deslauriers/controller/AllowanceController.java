package world.deslauriers.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.repository.AllowanceRepository;

import java.net.URI;

@Controller("/allowances")
public class AllowanceController {

    protected final AllowanceRepository allowanceRepository;

    public AllowanceController(AllowanceRepository allowanceRepository) {
        this.allowanceRepository = allowanceRepository;
    }

    @Get
    Flux<Allowance> getAll(){
        return allowanceRepository.findAll();
    }

    @Post
    Mono<HttpResponse<Allowance>> save(@Body Allowance cmd){
        return allowanceRepository
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
