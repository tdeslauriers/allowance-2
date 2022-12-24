package world.deslauriers.repository;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import world.deslauriers.domain.Allowance;
import world.deslauriers.domain.Tasktype;
import world.deslauriers.domain.TasktypeAllowance;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class TasktypeAllowanceDaoTest {

    @Inject
    private TasktypeAllowanceRepository tasktypeAllowanceRepository;

    @Inject
    private AllowanceRepository allowanceRepository;

    @Inject
    private TasktypeRepository tasktypeRepository;

    @Test
    void testTasktypeAllowanceCrud(){

        // vader's uuid > test data
        var vader = allowanceRepository.findById(1L).block();  // blocking to grab value
        assertEquals("f29ac64d-c666-4f31-8219-f2bbd0469dee", vader.getUserUuid());

        var tt = tasktypeRepository.findById(1L).block();
        assertEquals("Adhoc", tt.getCadence().toString());

        assertNotNull(tasktypeAllowanceRepository.findByTasktypeAndAllowance(tt, vader));

        // luke
        var xref = allowanceRepository.findById(2L)
                .flatMap(allowance -> {
                    return tasktypeAllowanceRepository
                            .findByTasktypeAndAllowance(tt, allowance)
                            .switchIfEmpty(tasktypeAllowanceRepository.save(new TasktypeAllowance(tt, allowance)));
                });
        assertNotNull(xref.block().getId());

    }
}
