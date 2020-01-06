package de.oth.tdanylenko.tdanbank.entity;

public enum RoleTypes {
    ROLE_MANAGER,
    ROLE_USER;


public static String parseRoleValue(RoleTypes role) {
    switch (role) {
        case ROLE_MANAGER:
            return "ROLE_MANAGER";
        case ROLE_USER:
            return "ROLE_USER";
        default:
            return "UNDEFINED";
    }
}
}
