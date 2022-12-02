package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Cadence;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.repository.TasktypeRepository;
import world.deslauriers.service.dto.TaskDto;

import javax.validation.ValidationException;
import java.util.Objects;

@Singleton
public class TasktypeServiceImpl implements TasktypeService {

    private final TasktypeRepository tasktypeRepository;

    public TasktypeServiceImpl(TasktypeRepository tasktypeRepository) {
        this.tasktypeRepository = tasktypeRepository;
    }

    @Override
    public Flux<Tasktype> findAll() {
        return tasktypeRepository.findAll();
    }

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
        return tasktypeRepository.save(new Tasktype(cmd.getName(), cmd.getCadence()));
    }

    @Override
    public Mono<Tasktype> update(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())){
            throw new ValidationException("Incorrect cadence provided.");
        }
        return tasktypeRepository.update(new Tasktype(cmd.getId(), cmd.getName(), cmd.getCadence()));
    }

    @Override
    public Flux<TaskDto> getDailyTasks(Long allowanceId) {
        return tasktypeRepository.findToDoList(allowanceId);
    }

    private Boolean isValidCadence(String cadence){

        for (var c: Cadence.values()){
            if (Objects.equals(cadence.toUpperCase(), c.toString())){
                return true;
            }
        }
        return false;
    }
}
