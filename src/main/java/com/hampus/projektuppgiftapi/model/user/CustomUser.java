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
    private List<Integer> favoriteGameIds = new ArrayList<>();

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


    public List<Integer> getFavoriteGameIds() {
        return favoriteGameIds;
    }

    public void setFavoriteGameIds(List<Integer> favoriteGameIds) {
        this.favoriteGameIds = favoriteGameIds;
    }

    public UserRoles getRole(){
        return role;
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
