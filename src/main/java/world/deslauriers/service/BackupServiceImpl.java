package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.config.EncryptionConfiguration;
import world.deslauriers.service.dto.*;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.nio.charset.StandardCharsets.UTF_8;

// endpoints take epoch time (unix seconds => Long)
// so that chiller service can request only records
// that have changed since last backup
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
    public Flux<BackupAllowance> getAllowanceBackup(Long epoch) {
        return allowanceService.getAllChangesSinceBackup(dateTimeFromEpoch(epoch))
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(allowance -> {

                    var encryptedBalance = "";
                    var encryptedUuid = "";

                    try {
                        encryptedBalance = cryptoService.encryptAes256Gcm(toByteArray(allowance.getBalance()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedUuid = cryptoService.encryptAes256Gcm(toByteArray(allowance.getUserUuid()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e) {
                        log.error("Encryption service failed to encrypt allowance: {}", allowance);
                        throw new RuntimeException(e);
                    }

                    return new BackupAllowance(allowance.getId(), encryptedBalance, encryptedUuid);
                });
    }

    @Override
    public Flux<BackupTasktype> getTasktypeBackup(Long epoch) {
        return tasktypeService.getAllChangesSinceBackup(dateTimeFromEpoch(epoch))
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(tasktype -> {

                    var encryptedName = "";
                    var encryptedCadence = "";
                    var encryptedCategory = "";
                    var encryptedArchived = "";

                    try {
                        encryptedName = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getName()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedCadence = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getCadence()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedCategory = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getCategory()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedArchived = cryptoService.encryptAes256Gcm(toByteArray(tasktype.getArchived()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption Service failed to encrypt tasktype: {}", tasktype);
                        throw new RuntimeException(e);
                    }

                    return new BackupTasktype(tasktype.getId(), encryptedName, encryptedCadence, encryptedCategory, encryptedArchived);
                });
    }

    @Override
    public Flux<BackupTask> getTaskBackup(Long epoch){
        return taskService.getAllChangesSinceBackup(dateTimeFromEpoch(epoch))
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(task -> {

                    var encryptedDate = "";
                    var encryptedComplete = "";
                    var encryptedSatifactory = "";
                    var encryptedTasktypeId = "";

                    try {
                        encryptedDate = cryptoService.encryptAes256Gcm(toByteArray(task.getDate().toLocalDate().toEpochDay()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedComplete = cryptoService.encryptAes256Gcm(toByteArray(task.getComplete()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedSatifactory = cryptoService.encryptAes256Gcm(toByteArray(task.getSatisfactory()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedTasktypeId = cryptoService.encryptAes256Gcm(toByteArray(task.getTasktype().getId()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption Service failed to encrypt task: {}", task);
                        throw new RuntimeException(e);
                    }

                    return new BackupTask(task.getId(), encryptedDate, encryptedComplete, encryptedSatifactory, encryptedTasktypeId);
                });
    }

    @Override
    public Flux<BackupTasktypeAllowance> getTasktypeAlowanceBackups(Long epoch) {
        return tasktypeAllowanceService.getAllChangesSinceBackup(dateTimeFromEpoch(epoch))
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(tasktypeAllowance -> {
                    var encryptedTasktypeId = "";
                    var encryptedAllowanceId = "";

                    try {
                        encryptedTasktypeId = cryptoService.encryptAes256Gcm(toByteArray(tasktypeAllowance.getTasktype().getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedAllowanceId = cryptoService.encryptAes256Gcm(toByteArray(tasktypeAllowance.getAllowance().getId()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption service unable to encrypt TasktypeAllowance: {}", tasktypeAllowance);
                        throw new RuntimeException(e);
                    }

                    return new BackupTasktypeAllowance(tasktypeAllowance.getId(), encryptedTasktypeId, encryptedAllowanceId);
                });
    }

    @Override
    public Flux<BackupTaskAllowance> getTaskAlowanceBackups(Long epoch) {
        return taskAllowanceService.getAllChangesSinceBackup(dateTimeFromEpoch(epoch))
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(taskAllowance -> {

                    var encryptedTasktypeId = "";
                    var encryptedAllowanceId = "";

                    try {
                        encryptedTasktypeId = cryptoService.encryptAes256Gcm(toByteArray(taskAllowance.getTask().getId()), encryptionConfiguration.getAes256GcmPassword());
                        encryptedAllowanceId = cryptoService.encryptAes256Gcm(toByteArray(taskAllowance.getAllowance().getId()), encryptionConfiguration.getAes256GcmPassword());
                    } catch (Exception e){
                        log.error("Encryption service unable to encrypt TasktypeAllowance: {}", taskAllowance);
                        throw new RuntimeException(e);
                    }

                    return new BackupTaskAllowance(taskAllowance.getId(), encryptedTasktypeId, encryptedAllowanceId);
                });
    }

    @Override
    public Mono<DeletedRecordsDto> cleanupBackupRecords(Long epoch) {
        var lastBackup = dateTimeFromEpoch(epoch);
        var cleanup = new DeletedRecordsDto();
        return Mono.from(taskService.getDeletedRecords(lastBackup, cleanup)
                .flatMap(deleteRecordsDto -> taskAllowanceService.getDeletedRecords(lastBackup, deleteRecordsDto))
                .flatMap(deleteRecordsDto -> allowanceService.getDeletedRecords(lastBackup, deleteRecordsDto))
                .flatMap(deleteRecordsDto -> tasktypeService.getDeletedRecords(lastBackup, deleteRecordsDto))
                .flatMap(deleteRecordsDto -> tasktypeAllowanceService.getDeletedRecords(lastBackup, deleteRecordsDto)));
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

    private LocalDateTime dateTimeFromEpoch(Long epoch){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }
}
