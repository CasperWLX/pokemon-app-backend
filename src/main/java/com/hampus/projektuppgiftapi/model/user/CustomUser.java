package com.hampus.projektuppgiftapi.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection = "Users")
public class CustomUser implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private UserRoles role;
    private int numberOfAttempts;
    private int bestAttempt;

    public CustomUser setName(String username){
        this.username = username;
        return this;
    }

    public CustomUser setPassword(String password){
        this.password = password;
        return this;
    }

    public CustomUser setRole(UserRoles role){
        this.role = role;
        return this;
    }

    public UserRoles getRole(){
        return role;
    }

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(int numberOfAttempts) {
        this.numberOfAttempts += numberOfAttempts;
    }

    public int getBestAttempt() {
        return bestAttempt;
    }

    public void setBestAttempt(int attempt) {
        if (attempt < bestAttempt || bestAttempt == 0){
            bestAttempt = attempt;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
