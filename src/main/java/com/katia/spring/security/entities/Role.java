package com.katia.spring.security.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "roles")
@NoArgsConstructor
@Setter
@Getter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> roles;

    public Role(Long id, String roleName, Set<User> roles) {
        this.id = id;
        this.roleName = roleName;
        this.roles = roles;
    }

    @Override
    public String getAuthority() {
        return getRoleName();
    }
}
