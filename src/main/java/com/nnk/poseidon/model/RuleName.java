package com.nnk.poseidon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "RuleName")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "name", length = 125)
    private String name;

    @Column(name = "description", length = 125)
    private String description;

    @Column(name = "json", length = 125)
    private String json;

    @Column(name = "template", length = 512)
    private String template;

    @Column(name = "sqlStr", length = 125)
    private String sqlStr;

    @Column(name = "sqlPart", length = 125)
    private String sqlPart;
}
