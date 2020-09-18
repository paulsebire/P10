package com.utilisateur.entities;



import com.utilisateur.enums.RoleEnum;
import com.utilisateur.services.BCryptManagerUtil;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "username", nullable = false, unique = true)
    @NotEmpty(message = "votre pseudo doit contenir au minimum 2 caractères")
    @Size(min = 2)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    @Size(min = 4)
    private String password;

    @NotNull
    @Column(name = "email", nullable = false)
    @Email(message = "Veuillez saisir une adresse mail valide")
    private String email;

    @NotNull
    @Size(min = 2)
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Size(min = 2)
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @ElementCollection(targetClass = RoleEnum.class, fetch = FetchType.EAGER)
    @Cascade(value = CascadeType.ALL)
    @JoinTable(
            indexes = {@Index(name = "INDEX_USER_ROLE", columnList = "id_user")},
            name = "roles",
            joinColumns = @JoinColumn(name = "id_user")
    )
    @Enumerated(EnumType.STRING)
    private Set<RoleEnum> roles;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    private boolean enabled;


    public Utilisateur() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.roles = new HashSet<>();
        this.roles.add(RoleEnum.USER);
    }

    public Utilisateur(String username, String password, String firstname, String lastname, String email, Set<RoleEnum> roles) {
        this.username = username;
        this.password = BCryptManagerUtil.passwordencoder().encode(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.roles = roles;
    }



    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEnum> roles) {
        this.roles = roles;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void grantAuthority() {
        if ( roles == null ) roles = new HashSet<>();
        roles.add(RoleEnum.USER);
    }


    public void setPassword(String password) {
        if (!password.isEmpty()) {
            this.password = BCryptManagerUtil.passwordencoder().encode(password);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", roles=" + roles +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                '}';
    }
}


