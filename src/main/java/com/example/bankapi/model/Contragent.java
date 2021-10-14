package com.example.bankapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "contragent")
public class Contragent {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    @Column(name = "VERSION")
    private Integer version;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "BILL_NUMBER")
    private String billNumber;
    @Column(name = "deposit_sum")
    private BigDecimal depositSum;

    public Contragent(User user, String billNumber, BigDecimal depositSum) {
        this(null, user, billNumber, depositSum);
    }

    public Contragent(Integer id, User user, String billNumber, BigDecimal depositSum) {
        this.id = id;
        this.user = user;
        this.billNumber = billNumber;
        this.depositSum = depositSum;
    }

    public Contragent() {
    }
}
