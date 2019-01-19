package com.bz.gists.util;

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
        return env.getProperty("spring.profiles.active");
    }

    public static String getApplicationName() {
        return env.getProperty("spring.application.name");
    }

    public static int getServerPort() {
        String serverPort = env.getProperty("server.port");
        return Objects.nonNull(serverPort) ? Integer.valueOf(serverPort) : 80;
    }

    public static String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
