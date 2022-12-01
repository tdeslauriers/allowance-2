package world.deslauriers.service;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Cadence;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.repository.TasktypeRepository;

import java.util.InputMismatchException;
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
    public Mono<Tasktype> save(Tasktype cmd) {

        if (!isValidCadence(cmd.getCadence())){
            throw new InputMismatchException("Incorrect cadence provided.");
        }
        return tasktypeRepository.save(new Tasktype(cmd.getName(), cmd.getCadence()));
    }

    @Override
    public Mono<Tasktype> update(Tasktype cmd) {
        return null;
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
