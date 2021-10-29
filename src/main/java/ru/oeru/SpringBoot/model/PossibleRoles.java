package ru.oeru.SpringBoot.model;

public enum PossibleRoles {
    ROLE_USER, ROLE_ADMIN;
    public static String getUserRole(){
        return ROLE_USER.name();
    }
    public static String getAdminRole() {
        return ROLE_ADMIN.name();
    }
    public static Role createUserRole() {
        return new Role(getUserRole());
    }
    public static Role createAdminRole() {
        return new Role(getAdminRole());
    }
}
