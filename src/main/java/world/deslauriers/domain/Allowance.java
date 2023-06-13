package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Set;

@Serdeable
@MappedEntity
public class Allowance {

    @Id
    private Long id;
    private Double balance;
    @NonNull
    private String userUuid;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "allowance")
    @Nullable
    @JsonIgnore
    private Set<TasktypeAllowance> tasktypeAllowances;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "allowance")
    @JsonIgnore
    private Set<TaskAllowance> taskAllowances;

    public Allowance() {
    }

    public Allowance(Double balance, String userUuid) {
        this.balance = balance;
        this.userUuid = userUuid;
    }

    public Allowance(Long id, Double balance, String userUuid) {
        this.id = id;
        this.balance = balance;
        this.userUuid = userUuid;
    }

    public Allowance(Long id, Double balance, String userUuid, Set<TasktypeAllowance> tasktypeAllowances, Set<TaskAllowance> taskAllowances) {
        this.id = id;
        this.balance = balance;
        this.userUuid = userUuid;
        this.tasktypeAllowances = tasktypeAllowances;
        this.taskAllowances = taskAllowances;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Set<TasktypeAllowance> getTasktypeAllowances() {
        return tasktypeAllowances;
    }

    public void setTasktypeAllowances(Set<TasktypeAllowance> tasktypeAllowances) {
        this.tasktypeAllowances = tasktypeAllowances;
    }

    public Set<TaskAllowance> getTaskAllowances() {
        return taskAllowances;
    }

    public void setTaskAllowances(Set<TaskAllowance> taskAllowances) {
        this.taskAllowances = taskAllowances;
    }

    @Override
    public String toString() {
        return "Allowance{" +
                "id=" + id +
                ", balance=" + balance +
                ", userUuid='" + userUuid + '\'' +
                ", tasktypeAllowances=" + tasktypeAllowances +
                ", taskAllowances=" + taskAllowances +
                '}';
    }
}
