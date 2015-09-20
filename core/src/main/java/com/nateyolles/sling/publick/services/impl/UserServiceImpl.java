package com.nateyolles.sling.publick.services.impl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateyolles.sling.publick.PublickConstants;
import com.nateyolles.sling.publick.services.UserService;

/**
 * Access information about users and groups.
 */
@Service
@Component
public class UserServiceImpl implements UserService {

    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public boolean isAuthorable(ResourceResolver resourceResolver) {
        boolean authorable = false;

        JackrabbitSession js = ((JackrabbitSession) resourceResolver.adaptTo(Session.class));

        try {
            Group authors = (Group)js.getUserManager().getAuthorizable(PublickConstants.GROUP_ID_AUTHORS);
            User user = (User)js.getUserManager().getAuthorizable(js.getUserID());

            authorable = user.isAdmin() || authors.isMember(user);
        } catch (RepositoryException e) {
            LOGGER.error("Could not determine group membership", e);
        }

        return authorable;
    }
}