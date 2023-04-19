package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import world.deslauriers.config.EncryptionConfiguration;
import world.deslauriers.service.dto.*;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Singleton
public class BackupServiceImpl implements BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupServiceImpl.class);

    private final EncryptionConfiguration encryptionConfiguration;
    private final AllowanceService allowanceService;
    private final TasktypeService tasktypeService;
    private final TaskService taskService;
    private final TasktypeAllowanceService tasktypeAllowanceService;
    private final TaskAllowanceService taskAllowanceService;
    private final CryptoService cryptoService;

    public BackupServiceImpl(EncryptionConfiguration encryptionConfiguration, AllowanceService allowanceService, TasktypeService tasktypeService, TaskService taskService, TasktypeAllowanceService tasktypeAllowanceService, TaskAllowanceService taskAllowanceService, CryptoService cryptoService) {
        this.encryptionConfiguration = encryptionConfiguration;
        this.allowanceService = allowanceService;
        this.tasktypeService = tasktypeService;
        this.taskService = taskService;
        this.tasktypeAllowanceService = tasktypeAllowanceService;
        this.taskAllowanceService = taskAllowanceService;
        this.cryptoService = cryptoService;
    }

    @Override
    public Flux<BackupAllowance> getAllowanceBackup() {

        return allowanceService.findAll()
                .map(allowance -> {

                    var encryptedId = "";
                    var encryptedBalance = "";
                    var encryptedUuid = "";

                    try {
                        encryptedId = cryptoService.encryptAes256Gcm(toByteArray(allowance.getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedBalance = cryptoService.encryptAes256Gcm(toByteArray(allowance.getBalance()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedUuid = cryptoService.encryptAes256Gcm(toByteArray(allowance.getUserUuid()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e) {
                        log.error("Encryption service failed to encrypt allowance: {}", allowance);
                        throw new RuntimeException(e);
                    }

                    return new BackupAllowance(encryptedId, encryptedBalance, encryptedUuid);
                });
    }

    @Override
    public Flux<BackupTasktype> getTasktypeBackup() {

        return tasktypeService.getAll()
                .map(tasktype -> {

                    var encyptedId = "";
                    var encryptedName = "";
                    var encryptedCadence = "";
                    var encryptedCategory = "";
                    var encryptedArchived = "";

                    try {
                        encyptedId = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedName = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getName()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedCadence = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getCadence()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedCategory = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getCategory()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedArchived = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getArchived()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption Service failed to encrypt tasktype: {}", tasktype);
                        throw new RuntimeException(e);
                    }

                    return new BackupTasktype(encyptedId, encryptedName, encryptedCadence, encryptedCategory, encryptedArchived);
                });
    }

    @Override
    public Flux<BackupTask> getTaskBackup(){
        return taskService.getAll()
                .map(task -> {

                    var encryptedId = "";
                    var encryptedDate = "";
                    var encryptedComplete = "";
                    var encryptedSatifactory = "";
                    var encryptedTasktypeId = "";

                    try {
                        encryptedId = cryptoService.encryptAes256Gcm(toByteArray(task.getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedDate = cryptoService.encryptAes256Gcm(toByteArray(task.getDate().toLocalDate().toEpochDay()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedComplete = cryptoService.encryptAes256Gcm(toByteArray(task.getComplete()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedSatifactory = cryptoService.encryptAes256Gcm(toByteArray(task.getSatisfactory()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedTasktypeId = cryptoService.encryptAes256Gcm(toByteArray(task.getTasktype().getId()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption Service failed to encrypt task: {}", task);
                        throw new RuntimeException(e);
                    }

                    return new BackupTask(encryptedId, encryptedDate, encryptedComplete, encryptedSatifactory, encryptedTasktypeId);
                });
    }

    @Override
    public Flux<BackupTasktypeAllowance> getTasktypeAlowanceBackups() {
        return tasktypeAllowanceService.getAll()
                .map(tasktypeAllowance -> {

                    var encryptedId = "";
                    var encryptedTasktypeId = "";
                    var encryptedAllowanceId = "";

                    try {
                        encryptedId = cryptoService.encryptAes256Gcm(toByteArray(tasktypeAllowance.getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedTasktypeId = cryptoService.encryptAes256Gcm(toByteArray(tasktypeAllowance.getTasktype().getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedAllowanceId = cryptoService.encryptAes256Gcm(toByteArray(tasktypeAllowance.getAllowance().getId()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption service unable to encrypt TasktypeAllowance: {}", tasktypeAllowance);
                        throw new RuntimeException(e);
                    }

                    return new BackupTasktypeAllowance(encryptedId, encryptedTasktypeId, encryptedAllowanceId);
                });
    }

    @Override
    public Flux<BackupTaskAllowance> getTaskAlowanceBackups() {
        return taskAllowanceService.getAll()
                .map(taskAllowance -> {

                    var encryptedId = "";
                    var encryptedTasktypeId = "";
                    var encryptedAllowanceId = "";

                    try {
                        encryptedId = cryptoService.encryptAes256Gcm(toByteArray(taskAllowance.getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedTasktypeId = cryptoService.encryptAes256Gcm(toByteArray(taskAllowance.getTask().getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedAllowanceId = cryptoService.encryptAes256Gcm(toByteArray(taskAllowance.getAllowance().getId()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption service unable to encrypt TasktypeAllowance: {}", taskAllowance);
                        throw new RuntimeException(e);
                    }

                    return new BackupTaskAllowance(encryptedId, encryptedTasktypeId, encryptedAllowanceId);
                });
    }

    // convert values from current type to byte-array
    private <T> byte[] toByteArray(T val){

        if (val instanceof Long){
            return ByteBuffer.allocate(Long.BYTES).putLong((Long) val).array();
        } else if (val instanceof Double) {
            return ByteBuffer.allocate(Double.BYTES).putDouble((Double) val).array();
        } else if (val instanceof Boolean) {
            byte[] bool = new byte[1];
            bool[0] = ((byte) ((Boolean) val ? 1: 0));
            return bool;
        } else if (val instanceof String str){
            return str.getBytes(UTF_8);
        } else {
            log.error("No type conversion in toByteArray(T val) method.");
            return null;
        }
    }
}
