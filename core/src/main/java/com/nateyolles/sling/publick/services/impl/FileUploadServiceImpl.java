package com.nateyolles.sling.publick.services.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateyolles.sling.publick.services.FileUploadService;

/**
 * Service to handle file uploading.
 */
@Service
@Component
public class FileUploadServiceImpl implements FileUploadService {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    /**
     * Uploads a file from a POST request to the specified location.
     *
     * TODO: upload multiple files and return array
     *
     * @param request The Sling HTTP servlet request.
     * @param path The path of the parent node to save the file under.
     */
    public String uploadFile(SlingHttpServletRequest request, String path) {
        final RequestParameterMap params = request.getRequestParameterMap();
        ResourceResolver resolver = request.getResourceResolver();

        for (final Map.Entry<String, RequestParameter[]> pairs : params.entrySet()) {
            final RequestParameter[] pArr = pairs.getValue();
            final RequestParameter param = pArr[0];

            if (!param.isFormField() && param.getSize() > 0) {
                final String name = param.getFileName();

                try {
                    final InputStream stream = param.getInputStream();

                    Resource imagesParent = resolver.getResource(path);
                    Resource fileResource = resolver.create(imagesParent, name, new HashMap<String, Object>() {{
                        put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_FILE);
                    }});
                    Resource contentResource = resolver.create(fileResource, JcrConstants.JCR_CONTENT, new HashMap<String, Object>() {{
                        put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_RESOURCE);
                        put(JcrConstants.JCR_DATA, stream);
                    }});
                    resolver.commit();

                    return contentResource.getPath();
                } catch (java.io.IOException e) {
                    LOGGER.error("Could not get image input stream", e);
                }
            }
        }

        return null;
    }
}