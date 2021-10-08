package com.example.bankapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @Column(name = "ID")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "BILL_NUMBER")
    private String billNumber;
    @Column(name = "BALANCE")
    private double balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bill")
    @JsonManagedReference
    private List<Card> cards;
}
