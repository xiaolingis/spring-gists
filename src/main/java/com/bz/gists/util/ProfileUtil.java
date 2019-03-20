package com.bz.gists.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public final class ProfileUtil {

    private static Environment env;

    public static void addEnvironment(Environment env) {
        ProfileUtil.env = env;
    }

    public static String getActiveProfile() {
        String activeProfile = env.getProperty("spring.profiles.active");
        return StringUtils.isNotBlank(activeProfile) ? activeProfile : "";
    }

    public static String getApplicationName() {
        String applicationName = env.getProperty("spring.application.name");
        return StringUtils.isNotBlank(applicationName) ? applicationName : "spring-boot-application";
    }

    public static int getServerPort() {
        String serverPort = env.getProperty("server.port");
        return Objects.nonNull(serverPort) ? Integer.valueOf(serverPort) : 8080;
    }

    public static String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }
}
