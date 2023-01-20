package world.deslauriers.repository;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
public class TasktypeDaoTest {

    @Inject
    private TasktypeRepository tasktypeRepository;

    @Test
    void testTaskTypeDaoCrud(){

        var dailys = tasktypeRepository.findDailyTasktypes(1L).subscribe(System.out::println);
    }
}
