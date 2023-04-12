package world.deslauriers.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(transactional = false)
public class CryptoServiceTest {

    @Inject
    private CryptoService cryptoService;

    private static final String PW = "Darth Vader";
    @Test
    void testAesMethods() throws Exception {

        var secret = "No, I am your father.";
        var encrypted = cryptoService.encryptAes256Gcm(secret.getBytes(StandardCharsets.UTF_8), PW);
        System.out.println(encrypted);
        System.out.println(cryptoService.decryptAes256Gcm(encrypted, PW));
    }
}
