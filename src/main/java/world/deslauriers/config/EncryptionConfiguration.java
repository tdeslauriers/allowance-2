package world.deslauriers.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("encryption")
public class EncryptionConfiguration {
    private String aes256GcmPassword;

    public String getAes256GcmPassword() {
        return aes256GcmPassword;
    }

    public void setAes256GcmPassword(String aes256GcmPassword) {
        this.aes256GcmPassword = aes256GcmPassword;
    }
}
