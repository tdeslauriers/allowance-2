package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Serdeable
@Entity
public class Allowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;
    private String userUuid;

    @OneToMany(mappedBy = "allowance")
    @Nullable
    @JsonIgnore
    private Set<TasktypeAllowance> tasktypeAllowances;

    @OneToMany(mappedBy = "allowance")
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
}
