package com.bz.gists.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static String[] getHostNames() throws SocketException {
        return Arrays.stream(getInetAddresses()).map(InetAddress::getHostName).collect(Collectors.toList()).toArray(new String[]{});
    }

    public static String[] getHostAddresses() throws SocketException {
        return Arrays.stream(getInetAddresses()).map(InetAddress::getHostAddress).collect(Collectors.toList()).toArray(new String[]{});
    }

    private static InetAddress[] getInetAddresses() throws SocketException {
        List<InetAddress> inetAddresses = new ArrayList<>();
        Collections.list(NetworkInterface.getNetworkInterfaces())
                .stream()
                .filter(networkInterface -> !networkInterface.isVirtual())
                .filter(networkInterface -> {
                    try {
                        return !networkInterface.isLoopback();
                    } catch (SocketException e) {
                        return false;
                    }
                })
                .filter(networkInterface -> {
                    try {
                        return networkInterface.isUp();
                    } catch (SocketException e) {
                        return false;
                    }
                })
                .filter(networkInterface -> {
                    try {
                        return !networkInterface.isPointToPoint();
                    } catch (SocketException e) {
                        return false;
                    }
                })
                .forEach(networkInterface -> Collections.list(networkInterface.getInetAddresses())
                        .stream()
                        .filter(address -> address.getAddress().length == 4)
                        .forEach(inetAddresses::add));
        return inetAddresses.toArray(new InetAddress[]{});
    }

    public static int getServerPort() {
        String serverPort = env.getProperty("server.port");
        return Objects.nonNull(serverPort) ? Integer.valueOf(serverPort) : 8080;
    }
}
