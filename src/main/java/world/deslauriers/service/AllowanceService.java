package world.deslauriers.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;

public interface AllowanceService {
    Flux<Allowance> findAll();

    Mono<Allowance> save(Allowance cmd);

    Mono<Allowance> findTasktypesByAllowanceId(Long id);

    Mono<Allowance> findById(Long allowanceId);

    Flux<Allowance> conductRemittance();
}
