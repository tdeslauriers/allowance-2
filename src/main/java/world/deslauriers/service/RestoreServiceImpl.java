package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import world.deslauriers.config.EncryptionConfiguration;
import world.deslauriers.domain.*;
import world.deslauriers.service.dto.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Singleton
public class RestoreServiceImpl implements RestoreService{

    private static final Logger log = LoggerFactory.getLogger(RestoreServiceImpl.class);

    private final EncryptionConfiguration encryptionConfiguration;
    private final CryptoService cryptoService;
    private final AllowanceService allowanceService;
    private final TasktypeService tasktypeService;
    private final TaskService taskService;
    private final TasktypeAllowanceService tasktypeAllowanceService;
    private final TaskAllowanceService taskAllowanceService;

    public RestoreServiceImpl(EncryptionConfiguration encryptionConfiguration, CryptoService cryptoService, AllowanceService allowanceService, TasktypeService tasktypeService, TaskService taskService, TasktypeAllowanceService tasktypeAllowanceService, TaskAllowanceService taskAllowanceService) {
        this.encryptionConfiguration = encryptionConfiguration;
        this.cryptoService = cryptoService;
        this.allowanceService = allowanceService;
        this.tasktypeService = tasktypeService;
        this.taskService = taskService;
        this.tasktypeAllowanceService = tasktypeAllowanceService;
        this.taskAllowanceService = taskAllowanceService;
    }


    @Override
    public Mono<Allowance> restoreAllowance(BackupAllowance backupAllowance) {

        byte[] decryptedBalance;
        double balance;

        byte[] decryptedUuid;
        String uuid;

        try {
            decryptedBalance = cryptoService.decryptAes256Gcm(backupAllowance.balance(), encryptionConfiguration.getAes256GcmPassword());
            balance = ByteBuffer.wrap(decryptedBalance).getDouble();

            decryptedUuid = cryptoService.decryptAes256Gcm(backupAllowance.userUuid(), encryptionConfiguration.getAes256GcmPassword());
            uuid = new String(decryptedUuid, StandardCharsets.UTF_8);
        } catch (Exception e){
            log.error("Unable to decrypt allowance id: {}", backupAllowance.userId());
            throw new RuntimeException(e);
        }

        return allowanceService.save(new Allowance(backupAllowance.userId(), balance, uuid));
    }

    @Override
    public Mono<Tasktype> restoreTasktype(BackupTasktype backupTasktype) {

        String name;
        String cadence;
        String category;
        boolean archived = false;

        try {
            name = new String(cryptoService.decryptAes256Gcm(backupTasktype.name(), encryptionConfiguration.getAes256GcmPassword()), StandardCharsets.UTF_8);
            cadence = new String(cryptoService.decryptAes256Gcm(backupTasktype.cadence(), encryptionConfiguration.getAes256GcmPassword()), StandardCharsets.UTF_8);
            category = new String(cryptoService.decryptAes256Gcm(backupTasktype.category(), encryptionConfiguration.getAes256GcmPassword()), StandardCharsets.UTF_8);
            archived = convertToBool(cryptoService.decryptAes256Gcm(backupTasktype.archived(), encryptionConfiguration.getAes256GcmPassword()));
        } catch (Exception e){
            log.error("Failed to decrypt tasktype id: {}", backupTasktype.id());
            throw new RuntimeException(e);
        }
        return tasktypeService.save(new Tasktype(backupTasktype.id(), name, cadence, category, archived));
    }

    @Override
    public Mono<Task> restoreTask(BackupTask backupTask) {

        OffsetDateTime date;
        boolean complete;
        boolean satisfactory;
        long tasktypeId;

        try {

            long epochSeconds = ByteBuffer.wrap(cryptoService.decryptAes256Gcm(backupTask.date(), encryptionConfiguration.getAes256GcmPassword())).getLong();
            date = OffsetDateTime.of(LocalDateTime.ofEpochSecond(epochSeconds,0, ZoneOffset.UTC), ZoneOffset.UTC);

            complete = convertToBool(cryptoService.decryptAes256Gcm(backupTask.complete(), encryptionConfiguration.getAes256GcmPassword()));
            satisfactory = convertToBool(cryptoService.decryptAes256Gcm(backupTask.satisfactory(), encryptionConfiguration.getAes256GcmPassword()));
            tasktypeId = ByteBuffer.wrap(cryptoService.decryptAes256Gcm(backupTask.tasktypeId(), encryptionConfiguration.getAes256GcmPassword())).getLong();
        } catch (Exception e){
            log.error("Unable to decypt task id: {}", backupTask.id());
            throw new RuntimeException(e);
        }

        return tasktypeService
                .getById(tasktypeId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Unable to locate tasktype id {} when trying to map it to task id {}", tasktypeId, backupTask.id());
                    return Mono.empty();
                }))
                .flatMap(tasktype -> taskService.save(new Task(backupTask.id(), date, complete, satisfactory, tasktype)));
    }

    @Override
    public Mono<TasktypeAllowance> restoreTasktypeAlloance(BackupTasktypeAllowance backupTasktypeAllowance) {

        long tasktypeId;
        long allowanceId;

        try {
            tasktypeId = ByteBuffer.wrap(cryptoService.decryptAes256Gcm(backupTasktypeAllowance.tasktypeId(), encryptionConfiguration.getAes256GcmPassword())).getLong();
            allowanceId = ByteBuffer.wrap(cryptoService.decryptAes256Gcm(backupTasktypeAllowance.allowanceId(), encryptionConfiguration.getAes256GcmPassword())).getLong();
        } catch (Exception e){
            log.error("Unable to decrypt tasktypeAllownace id: {}", backupTasktypeAllowance.id());
            throw new RuntimeException(e);
        }

        return tasktypeService
                .getById(tasktypeId)
                .zipWith(allowanceService.findById(allowanceId))
                .flatMap(objects -> tasktypeAllowanceService.save(new TasktypeAllowance(backupTasktypeAllowance.id(), objects.getT1(), objects.getT2())));
    }

    @Override
    public Mono<TaskAllowance> restoreTaskAlloance(BackupTaskAllowance backupTaskAllowance) {

        long taskId;
        long allowanceId;

        try {
            taskId = ByteBuffer.wrap(cryptoService.decryptAes256Gcm(backupTaskAllowance.taskId(), encryptionConfiguration.getAes256GcmPassword())).getLong();
            allowanceId = ByteBuffer.wrap(cryptoService.decryptAes256Gcm(backupTaskAllowance.allowanceId(), encryptionConfiguration.getAes256GcmPassword())).getLong();
        } catch (Exception e){
            log.error("Unable to decrypt taskAllowance: {}", backupTaskAllowance.id());
            throw new RuntimeException(e);
        }

        return taskService
                .getById(taskId)
                .zipWith(allowanceService.findById(allowanceId))
                .flatMap(objects -> taskAllowanceService.save(new TaskAllowance(backupTaskAllowance.id(), objects.getT1(), objects.getT2())));
    }

    private boolean convertToBool(byte[] bytesVal){
        var bool = false;
        for (byte i: bytesVal){
            if (i != 0){
                bool = true;
                break;
            }
        }
        return bool;
    }
}
