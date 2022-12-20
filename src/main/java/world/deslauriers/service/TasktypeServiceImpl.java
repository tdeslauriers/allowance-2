package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Cadence;
import world.deslauriers.domain.Category;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;
import world.deslauriers.repository.TasktypeAllowanceRepository;
import world.deslauriers.repository.TasktypeRepository;
import world.deslauriers.service.dto.AssignCmd;
import world.deslauriers.service.dto.TaskDto;

import javax.validation.ValidationException;
import java.util.Objects;

@Singleton
public class TasktypeServiceImpl implements TasktypeService {

    private final TasktypeRepository tasktypeRepository;
    private final AllowanceService allowanceService;
    private final TasktypeAllowanceRepository tasktypeAllowanceRepository;

    public TasktypeServiceImpl(TasktypeRepository tasktypeRepository, AllowanceService allowanceService, TasktypeAllowanceRepository tasktypeAllowanceRepository) {
        this.tasktypeRepository = tasktypeRepository;
        this.allowanceService = allowanceService;
        this.tasktypeAllowanceRepository = tasktypeAllowanceRepository;
    }

    @Override
    public Flux<Tasktype> getAllActive() {
        return tasktypeRepository.findByArchivedFalse();
    }

    @Override
    public Mono<Tasktype> getById(Long id){
        return tasktypeRepository.findByIdAndArchivedFalse(id);
    }

    // need
    @Override
    public Flux<Tasktype> findDailyTasktypes(Long allowanceId){
        return tasktypeRepository.findDailyTasktypes(allowanceId);
    }

    @Override
    public Mono<Tasktype> findById(Long id){
        return tasktypeRepository.findById(id);
    }

    @Override
    public Mono<Tasktype> save(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())){
            throw new ValidationException("Incorrect cadence provided.");
        }
        return tasktypeRepository.save(new Tasktype(cmd.getName(), cmd.getCadence(), cmd.getCategory(), cmd.getArchived()));
    }

    @Override
    public Mono<Tasktype> update(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())){
            throw new ValidationException("Incorrect cadence provided.");
        }

        if (!isValidCategory(cmd.getCategory())){
            throw new ValidationException("Incorrect category provided.");
        }
        return tasktypeRepository.update(new Tasktype(cmd.getId(), cmd.getName(), cmd.getCadence(), cmd.getCategory(), cmd.getArchived()));
    }

    @Override
    public Flux<TaskDto> getDailyTasks(Long allowanceId) {
        return tasktypeRepository.findToDoList(allowanceId);
    }

    @Override
    public Mono<TasktypeAllowance> assign(AssignCmd cmd) {

        // add error handling for if association already exists.
        return tasktypeRepository.findById(cmd.tasktypeId())
                .zipWith(allowanceService.findById(cmd.allowanceId()))
                .flatMap(tta -> tasktypeAllowanceRepository.save(new TasktypeAllowance(tta.getT1(), tta.getT2())));
    }

    private Boolean isValidCadence(String cadence){

        for (var c: Cadence.values()){
            if (Objects.equals(cadence.toUpperCase(), c.toString())){
                return true;
            }
        }
        return false;
    }

    private Boolean isValidCategory(String category){

        for (var c: Category.values()){
            if (Objects.equals(category.toUpperCase(), c.toString())){
                return true;
            }
        }
        return false;
    }
}
