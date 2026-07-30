package com.linneakarlsson.budget_app_enterpise.model.customUser.authority;

public enum UserPermission {

    // TODO - update?
    READ("READ"),
    WRITE("WRITE"),
    DELETE("DELETE");

    private final String userPermission;

    UserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getUserPermission() {
        return userPermission;
    }
}
