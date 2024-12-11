package com.hampus.projektuppgiftapi.model.user;

public enum UserPermissions {
    USER_CAN_READ("user:read"),
    USER_CAN_WRITE("user:write"),
    ADMIN_CAN_WRITE("admin:write"),
    ADMIN_CAN_READ("admin:read");

    private final String PERMISSION;

    UserPermissions(String permission) {
        this.PERMISSION = permission;
    }

    public String getPERMISSIONS() {
        return PERMISSION;
    }
}
