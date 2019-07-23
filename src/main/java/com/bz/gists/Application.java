package com.bz.gists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
@EnableAsync
@EnableTransactionManagement
@SpringBootApplication(exclude = {JmxAutoConfiguration.class})
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(final String[] args) throws UnknownHostException {
        final SpringApplication app = new SpringApplication(Application.class);
        final Environment env = app.run(args).getEnvironment();
        final String port = env.getProperty("server.port");
        final String[] profiles = env.getActiveProfiles();

        LOGGER.info("\n----------------------------------------------------------\n\t" + "Application '{}' is running! Access URLs:\n\t" + "Local: \t\thttp://127.0.0.1:{}\n\t"
                + "External: \thttp://{}:{}\n\t" + "Profile(s): \t{}\n----------------------------------------------------------", env
                .getProperty("spring.application.name"), port, InetAddress.getLocalHost().getHostAddress(), port, profiles);
    }

    /**
     * Initializes application.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    public void initApplication() {
        LOGGER.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
    }
}