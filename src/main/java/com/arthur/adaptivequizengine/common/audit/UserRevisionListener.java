package com.arthur.adaptivequizengine.common.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class UserRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        var userRevision = (UserRevisionEntity) revisionEntity;

        var context = SecurityContextHolder.getContext();
        if(context != null && context.getAuthentication() != null) {
            var principal = context.getAuthentication().getPrincipal();

            String email;
            if(principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                email = userDetails.getUsername();
            } else {
                email = principal.toString();
            }

            userRevision.setPerformedBy(email);
        } else {
            userRevision.setPerformedBy("anonymous");
        }

    }
}
