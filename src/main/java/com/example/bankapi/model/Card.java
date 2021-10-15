package com.example.bankapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "card")
public class Card {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    @Column(name = "VERSION")
    private Integer version;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Bill bill;
    @Column(name = "card_number")
    private String number;

    public Card(User user, Bill bill, String number) {
        this(null, user, bill, number);
    }

    public Card(Integer id, User user, Bill bill, String number) {
        this.id = id;
        this.user = user;
        this.bill = bill;
        this.number = number;
    }

    public Card() {
    }
}
