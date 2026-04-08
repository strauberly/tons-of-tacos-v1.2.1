package com.adamstraub.tonsoftacos.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@Table(name ="owner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Owner implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "owners_pk")
    private Integer ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "psswrd")
    private String psswrd;

    @Column(name = "contact")
    private String contact;

// will become seperate classes and tables once the actual need for roles is there(i.e. customer accounts, employee accounts)

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return psswrd;
    }


    public enum Role {
    ADMIN
    }
}
