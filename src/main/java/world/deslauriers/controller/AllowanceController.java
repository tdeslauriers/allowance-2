package world.deslauriers.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.service.AllowanceService;
import world.deslauriers.service.TaskService;
import world.deslauriers.service.dto.MetricsDto;

import java.net.URI;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/allowances")
public class AllowanceController {

    protected final AllowanceService allowanceService;
    protected final TaskService taskService;

    public AllowanceController(AllowanceService allowanceService, TaskService taskService) {
        this.allowanceService = allowanceService;
        this.taskService = taskService;
    }

    @Secured({"ALLOWANCE_ADMIN"})
    @Get
    Flux<Allowance> getAll(){
        return allowanceService.findAll();
    }

    @Secured({"ALLOWANCE_USER"})
    @Get("/dashboard")
    Mono<MetricsDto> getMetricsByAllowanceId(Authentication authentication){
        return allowanceService.getAllowanceMetrics(authentication.getAttributes().get("user_uuid").toString());
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
