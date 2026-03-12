package com.nnk.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @NotBlank
    @Column(name = "moodysRating", length = 125)
    private String moodysRating;

    @NotBlank
    @Column(name = "sandPRating", length = 125)
    private String sandPRating;

    @NotBlank
    @Column(name = "fitchRating", length = 125)
    private String fitchRating;

    @NotNull
    @Column(name = "orderNumber")
    private Integer orderNumber;
}
