package com.nateyolles.sling.publick;

import java.security.Principal;
import java.util.NoSuchElementException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlManager;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setup application by creating user groups and setting privileges.
 * This class runs on activation of the core bundle.
 */
public class Activator implements BundleActivator {

    /**
     * Logger instance to log and debug errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    /**
     * rep:write mixin.
     */
    private static final String REP_WRITE = "rep:write";

    /**
     * Display Name property for user groups.
     */
    private static final String GROUP_DISPLAY_NAME = "displayName";

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        LOGGER.info(bundleContext.getBundle().getSymbolicName() + " started");
        ServiceReference ResourceResolverFactoryReference = bundleContext.getServiceReference(ResourceResolverFactory.class.getName());
        ResourceResolverFactory resolverFactory = (ResourceResolverFactory) bundleContext.getService(ResourceResolverFactoryReference);

        if (resolverFactory != null) {
            ResourceResolver resolver = null;
            try {
                resolver = resolverFactory.getAdministrativeResourceResolver(null);
                removeSlingContent(resolver);
                createGroups(resolver);
                setPermissions(resolver);
            } catch (LoginException e) {
                LOGGER.error("Could not login to repository", e);
            } finally {
                if (resolver != null && resolver.isLive()) {
                    resolver.close();
                }
            }
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        LOGGER.info(bundleContext.getBundle().getSymbolicName() + " stopped");
    }

    private void setPermissions(ResourceResolver resolver) {
        setWritable(resolver, PublickConstants.BLOG_PATH);
        setWritable(resolver, PublickConstants.ASSET_PATH);

    }


    private void removeSlingContent(ResourceResolver resolver) {

        Resource resource = resolver.getResource("/index");

        if (resource != null) {
            try {
                resolver.delete(resource);
                resolver.commit();
            } catch (PersistenceException e) {
                LOGGER.error("Could not delete resource", e);
            }
        }

    }

    private void setWritable(ResourceResolver resolver, String path) {
        Resource resource = resolver.getResource(path);
        if (resource != null) {
            JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);
            if (session != null) {
                try {
                    JackrabbitAccessControlManager accessControlManager =
                        (JackrabbitAccessControlManager) session.getAccessControlManager();
                    Group user = (Group) session.getUserManager().getAuthorizable(PublickConstants.GROUP_ID_AUTHORS);
                    Principal principal = user.getPrincipal();

                    Privilege[] privileges = new Privilege[]{
                        accessControlManager.privilegeFromName(Privilege.JCR_WRITE),
                        accessControlManager.privilegeFromName(REP_WRITE)
                    };
                    JackrabbitAccessControlList acl;

                    try {
                        acl = (JackrabbitAccessControlList) accessControlManager.getApplicablePolicies(path).nextAccessControlPolicy();
                    } catch (NoSuchElementException e) {
                        acl = (JackrabbitAccessControlList) accessControlManager.getPolicies(path)[0];
                    }

                    acl.addEntry(principal, privileges, true);
                    accessControlManager.setPolicy(path, acl);

                    session.save();
                } catch (Exception e) {
                    LOGGER.error("Unable to modify ACLs for path " + path, e);
                }
            }
        }
    }

    private void createGroups(ResourceResolver resolver) {
        Session session = null;
        try {
            session = resolver.adaptTo(Session.class);

            if (session != null && session instanceof JackrabbitSession) {
                UserManager userManager = ((JackrabbitSession) session).getUserManager();
                ValueFactory valueFactory = session.getValueFactory();

                Authorizable authors = userManager.getAuthorizable(PublickConstants.GROUP_ID_AUTHORS);

                if (authors == null) {
                    authors = userManager.createGroup(PublickConstants.GROUP_ID_AUTHORS);
                    authors.setProperty(GROUP_DISPLAY_NAME, valueFactory.createValue(PublickConstants.GROUP_DISPLAY_AUTHORS));
                }

                Authorizable testers = userManager.getAuthorizable(PublickConstants.GROUP_ID_TESTERS);

                if (testers == null) {
                    testers = userManager.createGroup(PublickConstants.GROUP_ID_TESTERS);
                    testers.setProperty(GROUP_DISPLAY_NAME, valueFactory.createValue(PublickConstants.GROUP_DISPLAY_TESTERS));
                }

                if (session.hasPendingChanges()) {
                    session.save();
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("Could not get session", e);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
    }
}