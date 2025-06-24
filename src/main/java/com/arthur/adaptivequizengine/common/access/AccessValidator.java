package com.arthur.adaptivequizengine.common.access;

import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AccessValidator {

    public void validateIsAdmin(User currentUser) {
        if(currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admin can access this resource");
        }
    }

    public void validateCanDeleteUser(User currentUser, Long targetUserId) {
        if(!currentUser.getId().equals(targetUserId) && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You don't have permission to delete this user");
        }
    }
}
