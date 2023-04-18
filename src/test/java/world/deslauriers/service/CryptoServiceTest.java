package world.deslauriers.service;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.LocalDate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class CryptoServiceTest {

    @Inject
    private CryptoService cryptoService;

    @Property(name = "backup.encryption.aes256GcmPassword")
    private String PW;
    @Test
    void testAesMethods() throws Exception {

        var secret = "No, I am your father.";
        var encrypted = cryptoService.encryptAes256Gcm(secret.getBytes(UTF_8), PW);
        assertNotNull(encrypted);
        assertEquals(new String(cryptoService.decryptAes256Gcm(encrypted, PW), UTF_8), secret);

        var date = LocalDate.now();
        byte[] byteDate = ByteBuffer.allocate(Long.BYTES)
                .putLong(date.toEpochDay())
                .array();
        var encryptedDate = cryptoService.encryptAes256Gcm(byteDate, PW);
        System.out.println(encryptedDate);
        var decryptedDate = cryptoService.decryptAes256Gcm(encryptedDate, PW);
        long epochDay = ByteBuffer.wrap(decryptedDate).getLong();
        LocalDate dateAgain = LocalDate.ofEpochDay(epochDay);
        assertEquals(dateAgain, date);

        var uuid = "mZz4Qe8Y9bxTGimjK/4nqfN1pxUquGnGwZ69baOR8PPzIqS1m/mNKSddgDOLtR4pPyqJdMwEb4KUOJnF7MUYJSMHtjjsxwQB1ZRRbuuJR+Q=";
        var decryptedUuid = cryptoService.decryptAes256Gcm(uuid, PW);
        assertEquals("f29ac64d-c666-4f31-8219-f2bbd0469dee", new String(decryptedUuid, UTF_8));

        var cadence = "JS1f9NCYadqeCB1aWUl1yVRo7uj+D81W/vf6S4HYGvY6FO06YnyPV2AoVRFV+dUDBQ==";
        var decryptedCadence = cryptoService.decryptAes256Gcm(cadence, PW);
        assertEquals("Daily", new String(decryptedCadence, UTF_8));

        var complete = "0CWy8/cAUIEVIx8svcF86HOdw5fm7atRiYb8vqbRm5slpMPI3seV4kHcfDVg";
        var decryptedBytes = cryptoService.decryptAes256Gcm(complete, PW);
        var decryptedComplete = (decryptedBytes[0] == 1);
        assertTrue(decryptedComplete);

    }
}
