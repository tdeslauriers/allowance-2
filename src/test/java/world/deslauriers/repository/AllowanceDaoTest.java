package world.deslauriers.repository;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;
import world.deslauriers.domain.Allowance;

import java.util.ArrayList;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AllowanceDaoTest {

    @Inject
    private  AllowanceRepository allowanceRepository;

    @Test
    void testAllowanceCrud(){

        var ids = new ArrayList<Long>();


    }
}
