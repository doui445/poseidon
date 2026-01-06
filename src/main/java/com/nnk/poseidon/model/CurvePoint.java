package com.nnk.poseidon.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "CurvePoint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CurveId")
    private Integer CurveId;

    @Column(name = "asOfDate")
    private Timestamp asOfDate;

    @Column(name = "term")
    private Double term;

    @Column(name = "value")
    private Double value;

    @Column(name = "creationDate")
    private Timestamp creationDate;
}
