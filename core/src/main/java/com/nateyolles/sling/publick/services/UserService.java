package com.nateyolles.sling.publick.services;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * API to access information about users and groups.
 */
public interface UserService {

    /**
     * Get the authorable status of the current user.
     *
     * @param resourceResolver a resolver identifying the user
     * @return true if the current user is an admin or author.
     */
    boolean isAuthorable(ResourceResolver resourceResolver);
}