package org.economy.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.ResultSet;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "bank")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long user_id;

    private Double balance;

    private Double loan;

}
