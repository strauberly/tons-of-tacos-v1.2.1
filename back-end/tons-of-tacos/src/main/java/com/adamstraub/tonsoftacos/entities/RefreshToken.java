package com.adamstraub.tonsoftacos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_pk")
    private int id;
    @Column (name="exp")
    private Date exp;
//    @Column (name="exp")
//    private Instant exp;
    @Column(name="token")
    private String token;

    @OneToOne
    @JoinColumn (name = "owner_fk", referencedColumnName = "owners_pk")
    private Owner ownerInfo;
}
