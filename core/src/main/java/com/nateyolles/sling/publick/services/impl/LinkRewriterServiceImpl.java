package com.nateyolles.sling.publick.services.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateyolles.sling.publick.services.LinkRewriterService;
import com.nateyolles.sling.publick.services.SystemSettingsService;

/**
 * Service to rewrite links according to the user defined system settings value
 * for extensionless URLs. If extensionless URLs is enabled, rewrite paths by
 * removing ".html" extensions, removing trailing slashes, and removing index
 * file names.
 */
@Service
@Component
public class LinkRewriterServiceImpl implements LinkRewriterService {

    @Reference
    SystemSettingsService systemSettingsService;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkRewriterServiceImpl.class);

    /**
     * Rewrite links based on the extensionless URLs settings.
     *
     * @param link The link URL.
     * @param requestHost The host name from the request.
     * @return The rewritten link URL.
     */
    public String rewriteLink(final String link, final String requestHost) {
        String newLink = link;

        try {
            URI uri = new URI(link);
            String linkHost = uri.getHost();

            if (linkHost == null || linkHost.equals(requestHost)) {
                String path = uri.getPath();

                path = StringUtils.removeStart(path, "/content");

                if (systemSettingsService != null && systemSettingsService.getExtensionlessUrls()
                        && StringUtils.isNotBlank(link)) {

                    path = StringUtils.removeEnd(path, ".html");
                    path = StringUtils.removeEnd(path, "index");

                    if (!"/".equals(path)) {
                        path = StringUtils.removeEnd(path, "/");
                    }
                } else if (StringUtils.isNotBlank(path) && !path.endsWith("/") && !path.endsWith(".html")) {
                    path = path.concat(".html");
                }

                newLink = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path, uri.getQuery(), uri.getFragment()).toString();
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Could not rewrite link do to link syntax.", e);
        }

        return newLink;
    }

    /**
     * Rewrite all links in an HTML string based on the extensionless URLs settings.
     *
     * @param html The HTML string.
     * @param requestHost The host name from the request.
     * @return The HTML string with rewritten URLs.
     */
    public String rewriteAllLinks(final String html, final String requestHost) {
        Document document = Jsoup.parse(html);
        Elements links = document.select("a[href]");
        Elements metas = document.select("meta[content]");

        updateAttribute(links, "href", requestHost);
        updateAttribute(metas, "content", requestHost);

        return document.toString();
    }

    /**
     * Loop through jsoup collections and rewrite the links.
     *
     * @param elements The collection of jsoup elements
     * @param attribute The attribute of the elements to rewrite.
     * @param requestHost The hostname from the request.
     */
    private void updateAttribute(final Elements elements, final String attribute, final String requestHost) {
        for (Element element : elements)  {
            String newLink = rewriteLink(element.attr(attribute), requestHost);
            element.attr(attribute, newLink);
        }
    }
}