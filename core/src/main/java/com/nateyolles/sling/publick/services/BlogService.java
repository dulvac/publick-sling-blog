package com.nateyolles.sling.publick.services;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

/**
 * API to search and retrieve blog posts.
 */
public interface BlogService {

    /**
     * Get all blog posts in order of newest first.
     *
     * @param request the {@link SlingHttpServletRequest} used to identify the user requesting this information
     * @return All blog posts in order of newest first.
     */
    List<Resource> getPosts(SlingHttpServletRequest request);

    /**
     * Get paginated blog posts in order of newest first.
     *
     * @param request the {@link SlingHttpServletRequest} used to identify the user requesting this information
     * @param offset The starting point of blog posts to get.
     * @param limit The number of blog posts to get.
     * @return The blog posts according to the starting point and length.
     */
    List<Resource> getPosts(SlingHttpServletRequest request, long offset, long limit);

    /**
     * Get the number of blog posts in the system.
     *
     * @param request the {@link SlingHttpServletRequest} used to identify the user requesting this information
     * @return The number of blog posts.
     */
    long getNumberOfPosts(SlingHttpServletRequest request);

    /**
     * Get the number of pagination pages determined by the total
     * number of blog posts and specified number of blog posts
     * per page.
     *
     * @param request the {@link SlingHttpServletRequest} used to identify the user requesting this information
     * @param pageSize The number of blog pages per size.
     * @return The number of pagination pages required to display all
     *            blog posts.
     */
    long getNumberOfPages(SlingHttpServletRequest request, int pageSize);
}