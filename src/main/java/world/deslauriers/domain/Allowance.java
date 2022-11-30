package world.deslauriers.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long userId;

    @OneToMany(mappedBy = "allowance")
    Set<TasktypeAllowance> tasktypeAllowances;

    public Allowance() {
    }

    public Allowance(Double balance, Long userId) {
        this.balance = balance;
        this.userId = userId;
    }

    public Allowance(Long id, Double balance, Long userId) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }

    public Allowance(Long id, Double balance, Long userId, Set<TasktypeAllowance> tasktypeAllowances) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
        this.tasktypeAllowances = tasktypeAllowances;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<TasktypeAllowance> getTasktypeAllowances() {
        return tasktypeAllowances;
    }

    public void setTasktypeAllowances(Set<TasktypeAllowance> tasktypeAllowances) {
        this.tasktypeAllowances = tasktypeAllowances;
    }
}
