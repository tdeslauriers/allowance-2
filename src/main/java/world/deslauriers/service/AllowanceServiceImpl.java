package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;
import world.deslauriers.repository.AllowanceRepository;

@Singleton
public class AllowanceServiceImpl implements AllowanceService{

    private final AllowanceRepository allowanceRepository;

    public AllowanceServiceImpl(AllowanceRepository allowanceRepository) {
        this.allowanceRepository = allowanceRepository;
    }

    @Override
    public Flux<Allowance> findAll(){
        return allowanceRepository.findAll();
    }

    @Override
    public Mono<Allowance> save(Allowance cmd){

        // add validation call to auth to make sure user real
        return allowanceRepository.save(cmd);
    }

    @Override
    public Mono<Allowance> findTasktypesByAllowanceId(Long id){
        return allowanceRepository.findById(id);
    }
}
