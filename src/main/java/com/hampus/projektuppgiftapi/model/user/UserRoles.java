package com.hampus.projektuppgiftapi.model.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.hampus.projektuppgiftapi.model.user.UserPermissions.*;

public enum UserRoles {
    ADMIN(List.of(
            ADMIN_CAN_READ,
            ADMIN_CAN_WRITE)),
    USER(List.of(USER_CAN_READ,
            USER_CAN_WRITE)),;

    public final List<UserPermissions> userPermissions;

    UserRoles(List<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthoritiesList = new ArrayList<>();
        simpleGrantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        userPermissions.forEach(permission ->
                simpleGrantedAuthoritiesList.add(new SimpleGrantedAuthority(permission.getPERMISSIONS()))
        );

        return simpleGrantedAuthoritiesList;
    }
}

