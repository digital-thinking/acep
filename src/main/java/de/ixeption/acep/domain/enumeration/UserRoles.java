package de.ixeption.acep.domain.enumeration;

import de.ixeption.acep.domain.Authority;

public enum UserRoles {

    ROLE_USER, ROLE_ADMIN;

    private final Authority authority;

    UserRoles() {
        Authority authority = new Authority();
        authority.setName(this.name());
        this.authority = authority;
    }

    public Authority getAuthority() {
        return authority;
    }
}
