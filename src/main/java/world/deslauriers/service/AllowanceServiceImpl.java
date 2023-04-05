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
import world.deslauriers.domain.Tasktype;
import world.deslauriers.repository.AllowanceRepository;
import world.deslauriers.service.dto.AllowanceDto;
import world.deslauriers.service.dto.MetricsDto;
import world.deslauriers.service.dto.TaskDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.stream.Collectors;

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
    public Mono<Allowance> getByUuid(String uuid) { return allowanceRepository.findByUserUuid(uuid);}

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
                                                            if (t.getComplete().intValue() == 1 && t.getSatisfactory().intValue() == 1) remittance += taskValue;
                                                            if (t.getComplete().intValue() == 1 && t.getSatisfactory().intValue() == 0) remittance += (taskValue / 2);
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

    @Override
    public Mono<MetricsDto> getAllowanceMetrics(String uuid){
        return getByUuid(uuid)
                .zipWith(taskService.getTasksFromPastWeek(uuid).collectList())
                .map(objects -> {
                    // set up allowance metrics
                    var total = objects.getT2()
                            .stream()
                            .filter(taskDto -> !taskDto.getCadence().equals("Adhoc"))
                            .toList()
                            .size();
                    var totalCompleted = objects.getT2()
                            .stream()
                            .filter(taskDto -> taskDto.getComplete().intValue() == 1) // byte to boolean
                            .filter(taskDto -> !taskDto.getCadence().equals("Adhoc")) // Adhoc not tied to date (yet)
                            .toList()
                            .size();
                    var percentageComplete = totalCompleted > 0 ? ((double) totalCompleted / (double) total) * 100d : 0;
                    var totalSatisfactory = objects.getT2()
                            .stream()
                            .filter(taskDto -> taskDto.getSatisfactory().intValue() == 1) // byte to boolean
                            .filter(taskDto -> !taskDto.getCadence().equals("Adhoc")) //// Adhoc not tied to date (yet)
                            .toList()
                            .size();
                    var percentageSatisfactory = totalSatisfactory > 0 ? ((double) totalSatisfactory / (double) total) * 100d : 0;

                    var tt = objects.getT2()
                            .stream()
                            .map(taskDto -> {
                                var t = new TaskDto();
                                t.setName(taskDto.getName());
                                t.setCadence(taskDto.getCadence());
                                t.setCategory(taskDto.getCategory());
                                t.setArchived(taskDto.getArchived());
                                return t;
                            })
                            .collect(Collectors.toCollection(HashSet::new));

                    return new MetricsDto(
                            objects.getT1().getUserUuid(),
                            Math.round(objects.getT1().getBalance() * 100.0)/100.0,
                            total,
                            totalCompleted,
                            (double) Math.round(percentageComplete),
                            totalSatisfactory,
                            (double) Math.round(percentageSatisfactory),
                            tt
                    );
                });
    }
}
