package tools.spring;

import com.bz.gists.Application;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created on 2019/4/29
 *
 * @author zhongyongbin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public final class SpringViewer {

    private static final String FILE_PATH = "generated-sources/spring";

    private static final String BEAN_VIEW_FILE = "bean-view.properties";

    private static final String CONFIGURATION_PROPERTIES_VIEW_FILE = "configuration-properties-view.properties";

    private static final String VALUE_VIEW_FILE = "value-view.properties";

    private static final String MESSAGE_FORMAT = "%s=%s";

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void lookupAllBeans() throws Exception {
        List<String> message = new ArrayList<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            String[] beanNameData = beanName.split("\\.");
            message.add(String.format(MESSAGE_FORMAT, beanNameData[beanNameData.length - 1], applicationContext.getBean(beanName).getClass().getCanonicalName()));
        });
        writeView(BEAN_VIEW_FILE, message);
    }

    @Test
    public void lookupAllConfigurationProperties() throws Exception {
        List<String> message = new ArrayList<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            if (bean.getClass().isAnnotationPresent(ConfigurationProperties.class)) {
                ConfigurationProperties configurationProperties = bean.getClass().getAnnotation(ConfigurationProperties.class);
                String prefix = configurationProperties.prefix();
                prefix = StringUtils.isNotBlank(prefix) ? prefix : configurationProperties.value();
                message.add(String.format(MESSAGE_FORMAT, prefix, bean.getClass().getCanonicalName()));
            }
        });

        writeView(CONFIGURATION_PROPERTIES_VIEW_FILE, message);
    }

    @Test
    public void lookupAllValue() throws Exception {
        List<String> message = new ArrayList<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            Arrays.stream(bean.getClass().getFields()).forEach(field -> {
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    message.add(String.format(MESSAGE_FORMAT, value.value(), bean.getClass().getCanonicalName()));
                }
            });
        });

        writeView(VALUE_VIEW_FILE, message);
    }

    private void writeView(String fileName, List<String> message) throws Exception {
        File path = new File(FILE_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        File viewFile = new File(path, fileName);
        if (!viewFile.createNewFile()) {
            viewFile.delete();
            viewFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(viewFile);
        for (String s : message) {
            fileWriter.append(s);
            fileWriter.append(System.lineSeparator());
        }

        fileWriter.flush();
    }
}
