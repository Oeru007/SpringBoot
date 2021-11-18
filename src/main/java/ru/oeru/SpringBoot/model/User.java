package ru.oeru.SpringBoot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(min = 3, message = "First name must be more than 3 characters")
    @NotBlank(message = "First name can not be blank")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name can not be blank")
    @Size(min = 3, message = "Last name must be more than 3 characters")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email is not valid")
    private String email;

    @Column(name = "username")
    @NotBlank(message = "Username can not be blank")
    @Size(min = 3, message = "Username must be more than 3 characters")
    private String username;

    @Column
    @Min(value = 0, message = "Age is not valid")
    private Integer age;

    @Column
    @NotBlank(message = "Password can not be blank")
    @Size(min = 3, message = "Password must be more than 3 characters")
    private String password;

    @Transient
    @NotBlank(message = "Password confirm can not be blank")
    @Size(min = 3, message = "Password confirm must be more than 3 characters")
    private String passwordConfirm;

    @Transient
    private String confirm;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addRole(Role role){
        roles.add(role);
        role.getUsers().add(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserEmailWithRoles() {
        List<Role> list = new ArrayList<>(roles);
        list.sort(Comparator.comparing(Role::getName));
        StringBuilder emailWithRoles = new StringBuilder(getEmail() + " with roles: ");
        for (Role role : list){
            emailWithRoles.append(role.getName().replace("ROLE_",""));
            emailWithRoles.append(" ");
        }
        return emailWithRoles.toString();
    }

    public String getRolesString() {
        List<Role> list = new ArrayList<>(roles);
        list.sort(Comparator.comparing(Role::getName));
        StringBuilder roles = new StringBuilder();
        for (Role role : list){
            roles.append(role.getName().replace("ROLE_",""));
            roles.append(" ");
        }
        if (roles.length() != 0) {
            roles.deleteCharAt(roles.lastIndexOf(" "));
        }
        return roles.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
