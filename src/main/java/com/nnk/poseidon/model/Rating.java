package com.nnk.poseidon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "moodysRating", length = 125)
    private String moodysRating;

    @Column(name = "sandPRating", length = 125)
    private String sandPRating;

    @Column(name = "fitchRating", length = 125)
    private String fitchRating;

    @Column(name = "orderNumber")
    private Integer orderNumber;
}
