package com.nateyolles.sling.publick;

/**
 * Constants used throughout the application
 */
public class PublickConstants {

    /** Private constructor to prevent instantiation of class */
    private PublickConstants() {
    }

    /** JCR root path */
    public static final String ROOT_PATH = "/";

    /** Content path */
    public static final String CONTENT_PATH = "/content";

    /** Blog path */
    public static final String BLOG_PATH = CONTENT_PATH + "/blog";

    /** Comments path */
    public static final String COMMENTS_PATH = CONTENT_PATH + "/comments";

    /** Admin path */
    public static final String ADMIN_PATH = CONTENT_PATH + "/admin";

    /** Assets path */
    public static final String ASSET_PATH = CONTENT_PATH + "/assets";

    /** Images path */
    public static final String IMAGE_PATH = ASSET_PATH + "/images";

    /** PDF path */
    public static final String PDF_PATH = ASSET_PATH + "/pdf";

    /** Audio path */
    public static final String AUDIO_PATH = ASSET_PATH + "/audio";

    /** Video path */
    public static final String VIDEO_PATH = ASSET_PATH + "/video";

    /** Admin dashboard path */
    public static final String ADMIN_LANDING_PATH = ADMIN_PATH + "/index";

    /** Admin blog list path */
    public static final String ADMIN_LIST_PATH = ADMIN_PATH + "/list";

    /** Admin edit blog path */
    public static final String ADMIN_EDIT_PATH = ADMIN_PATH + "/edit";

    /** Admin assets blog path */
    public static final String ADMIN_ASSETS_PATH = ADMIN_PATH + "/assets";

    /** Admin config path */
    public static final String ADMIN_CONFIG_PATH = ADMIN_PATH + "/config";

    /** Admin reCAPTCHA config path */
    public static final String RECAPTCHA_CONFIG_PATH = ADMIN_CONFIG_PATH + "/recaptcha";

    /** Admin system config path */
    public static final String SYSTEM_CONFIG_PATH = ADMIN_CONFIG_PATH + "/system";

    /** Admin email config path */
    public static final String EMAIL_CONFIG_PATH = ADMIN_CONFIG_PATH + "/email";

    /** Page base resource type */
    private static final String PAGE_TYPE = "publick/pages";

    /** Admin page resource type */
    public static final String PAGE_TYPE_ADMIN = PAGE_TYPE + "/adminPage";

    /** Basic page resource type */
    public static final String PAGE_TYPE_BASIC = PAGE_TYPE + "/basicPage";

    /** Blog page resource type */
    public static final String PAGE_TYPE_BLOG = PAGE_TYPE + "/blogPage";

    /** Authors group name */
    public static final String GROUP_ID_AUTHORS = "authors";

    /** Testers group name */
    public static final String GROUP_ID_TESTERS = "testers";

    /** Authors group display name */
    public static final String GROUP_DISPLAY_AUTHORS = "Authors";

    /** Testers group display name */
    public static final String GROUP_DISPLAY_TESTERS = "Testers";

    /** Servlet admin path */
    public static final String SERVLET_PATH_ADMIN = "/bin/admin";

    /** Servlet public path */
    public static final String SERVLET_PATH_PUBLIC = "/bin/publick";

    /** Password replacement text */
    public static final String PASSWORD_REPLACEMENT = "****************";
}