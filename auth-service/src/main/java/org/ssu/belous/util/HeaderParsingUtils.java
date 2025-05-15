package org.ssu.belous.util;

public class HeaderParsingUtils {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    public static String getTokenByAuthorizationHeader(String header) {
        return header.substring(AUTHORIZATION_HEADER_PREFIX.length());
    }
}
