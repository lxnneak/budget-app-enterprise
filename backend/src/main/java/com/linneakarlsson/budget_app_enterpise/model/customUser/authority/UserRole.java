package com.linneakarlsson.budget_app_enterpise.model.customUser.authority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.linneakarlsson.budget_app_enterpise.model.customUser.authority.UserPermission.*;

public enum UserRole {

    USER(
            UserRoleName.USER.getRoleName(),
            Set.of(
                    READ,
                    WRITE
            )
    ),

    ADMIN(
            UserRoleName.ADMIN.getRoleName(),
            Stream.concat(
                    Set.of(
                            DELETE
                    ).stream(),
                    USER.getUserPermissions().stream()
            ).collect(Collectors.toSet())
    );

    private final String userRoleName;
    private final Set<UserPermission> userPermissions;

    UserRole(String userRoleName, Set<UserPermission> userPermissions) {
        this.userRoleName = userRoleName;
        this.userPermissions = userPermissions;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public List<SimpleGrantedAuthority> getUserAuthorities() {

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        authorityList.add(new SimpleGrantedAuthority(this.userRoleName));
        authorityList.addAll(
                this.userPermissions.stream().map(
                        userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission())
                ).toList()
        );

        return authorityList;
    }
}
