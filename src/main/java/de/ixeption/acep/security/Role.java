package de.ixeption.acep.security;

import de.ixeption.acep.domain.Authority;

/**
 * Constants for Spring Security authorities.
 */
public enum Role {

    ROLE_USER, ROLE_ADMIN, ANONYMOUS;

    private final Authority authority;

    Role() {
        Authority authority = new Authority();
        authority.setName(this.name());
        this.authority = authority;
    }

    public Authority getAuthority() {
        return authority;
    }

}



