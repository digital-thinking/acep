package de.ixeption.acep.web.rest;

import de.ixeption.acep.domain.User;
import de.ixeption.acep.domain.enumeration.UserRoles;
import de.ixeption.acep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public abstract class AbstractResource {
    @Autowired
    private UserService userService;

    protected User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<User> user = userService.getUserByName(username);
            if (user.isPresent()) {
                return user.get();
            }
        }
        return null;
    }

    public boolean isAdminUser() {
        User loggedInUser = getLoggedInUser();
        return loggedInUser.getAuthorities().contains(UserRoles.ROLE_ADMIN.getAuthority());
    }
}


