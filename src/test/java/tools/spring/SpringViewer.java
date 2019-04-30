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
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


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
        Map<String, String> messageMap = new TreeMap<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            String name = beanName;
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            if (StringUtils.contains(beanName, ".")) {
                String[] beanNameData = beanName.split("\\.");
                name = "." + beanNameData[beanNameData.length - 1];
            }
            messageMap.put(typeName, String.format(MESSAGE_FORMAT, name, typeName));
        });
        writeView(BEAN_VIEW_FILE, new ArrayList<>(messageMap.values()));
    }

    @Test
    public void lookupAllConfigurationProperties() throws Exception {
        Map<String, String> messageMap = new TreeMap<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            if (bean.getClass().isAnnotationPresent(ConfigurationProperties.class)) {
                ConfigurationProperties configurationProperties = bean.getClass().getAnnotation(ConfigurationProperties.class);
                String prefix = configurationProperties.prefix();
                prefix = StringUtils.isNotBlank(prefix) ? prefix : configurationProperties.value();
                messageMap.put(typeName, String.format(MESSAGE_FORMAT, prefix, typeName));
            }
        });

        writeView(CONFIGURATION_PROPERTIES_VIEW_FILE, new ArrayList<>(messageMap.values()));
    }

    @Test
    public void lookupAllValue() throws Exception {
        Map<String, String> messageMap = new TreeMap<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            Arrays.stream(bean.getClass().getFields()).forEach(field -> {
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    messageMap.put(typeName, String.format(MESSAGE_FORMAT, value, typeName));
                }
            });
        });

        writeView(VALUE_VIEW_FILE, new ArrayList<>(messageMap.values()));
    }

    private void writeView(String fileName, List<String> message) throws Exception {
        if (CollectionUtils.isEmpty(message)) {
            return;
        }
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
