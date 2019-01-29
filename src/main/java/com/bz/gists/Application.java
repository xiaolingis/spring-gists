package com.bz.gists;

import com.bz.gists.util.ProfileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.UnknownHostException;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
@EnableAsync
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(final String[] args) throws UnknownHostException {
        final SpringApplication app = new SpringApplication(Application.class);
        final Environment env = app.run(args).getEnvironment();
        ProfileUtil.addEnvironment(env);

        log();
    }

    private static void log() throws UnknownHostException {
        StringBuilder log = new StringBuilder();
        log.append("\n----------------------------------------------------------\n\t");
        log.append("Running with spring profile : {}\n\t");
        log.append("Application '{}' is running! Access URLs:\n\t");
        log.append("Local: \t\thttp://127.0.0.1:{}\n\t");
        log.append("External: \thttp://{}:{}");
        log.append("\n----------------------------------------------------------");

        LOGGER.info(log.toString(), ProfileUtil.getActiveProfile(), ProfileUtil.getApplicationName(), ProfileUtil.getServerPort(), ProfileUtil.getHostAddress(), ProfileUtil.getServerPort());
    }
}
