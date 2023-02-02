package world.deslauriers.service;

import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.client.GatewayFetcher;
import world.deslauriers.client.dto.LoginRequest;
import world.deslauriers.domain.Allowance;
import world.deslauriers.repository.AllowanceRepository;
import world.deslauriers.service.dto.AllowanceDto;

import java.time.LocalDate;
import java.time.Period;

@Singleton
public class AllowanceServiceImpl implements AllowanceService{

    private static final Logger log = LoggerFactory.getLogger(AllowanceServiceImpl.class);

    @Property(name = "allowance.service.credentials.username")
    protected String username;

    @Property(name = "allowance.service.credentials.password")
    protected String password;

    private final TaskService taskService;
    private final GatewayFetcher gatewayFetcher;
    private final AllowanceRepository allowanceRepository;

    public AllowanceServiceImpl(TaskService taskService, GatewayFetcher gatewayFetcher, AllowanceRepository allowanceRepository) {
        this.taskService = taskService;
        this.gatewayFetcher = gatewayFetcher;
        this.allowanceRepository = allowanceRepository;
    }

    @Override
    public Flux<Allowance> findAll(){
        return allowanceRepository.findAll();
    }

    @Override
    public Mono<Allowance> save(Allowance cmd){
        return allowanceRepository.save(cmd);
    }

    @Override
    public Mono<Allowance> findTasktypesByAllowanceId(Long id){
        return allowanceRepository.findById(id);
    }

    @Override
    public Mono<Allowance> findById(Long id) {
        return allowanceRepository.findById(id);
    }

    @Override
    public Flux<Allowance> conductRemittance() {

       return gatewayFetcher.login(new LoginRequest(username, password))
               .flatMapMany(loginResponse -> {
                    return allowanceRepository.findAll()
                            .flatMap(allowance -> {
                                return gatewayFetcher.getUserProfileByUuid(
                                        "Bearer " + loginResponse.access_token(), allowance.getUserUuid())
                                        .map(profile -> {
                                            return new AllowanceDto(
                                                    allowance.getId(),
                                                    allowance.getBalance(),
                                                    allowance.getUserUuid(),
                                                    profile.username(),
                                                    profile.firstname(),
                                                    profile.lastname(),
                                                    Period.between(LocalDate.parse(profile.birthday()), LocalDate.now()).getYears()
                                            );
                                        })
                                        .flatMap(allowanceDto -> {
                                            return taskService.getTasksFromPastWeek(allowanceDto.userUuid()).collectList()
                                                    .flatMap(tasks -> {
                                                        var total = (double) tasks.size();
                                                        var taskValue = ((double) allowanceDto.age()) / total;
                                                        double remittance = 0d;
                                                        for (var t : tasks) {
                                                            if (t.getComplete() && t.getSatisfactory()) remittance += taskValue;
                                                            if (t.getComplete() && !t.getSatisfactory())
                                                                remittance += (taskValue / 2);
                                                        }
                                                        log.info("Account: {} {}:\nTotal possible: {}\nTotal Earned: {}",
                                                                allowanceDto.firstname(), allowanceDto.lastname(), allowanceDto.age(), remittance);
                                                        var balance = allowanceDto.balance() + remittance;
                                                        return allowanceRepository.update(
                                                                new Allowance(allowanceDto.id(), balance, allowanceDto.userUuid()));
                                                    });
                                        });
                            });
               });

    }
}
