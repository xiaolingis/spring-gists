package tools.spring;

import com.bz.gists.Application;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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

    private static final String AOP_VIEW_FILE = "aop-view.properties";

    private static final String MESSAGE_FORMAT = "%s=%s";

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查看容器中所有的 Bean
     */
    @Test
    public void lookupAllBeans() throws Exception {
        ArrayList<Message> messages = new ArrayList<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            String name = beanName;
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            if (StringUtils.contains(beanName, ".")) {
                String[] beanNameData = beanName.split("\\.");
                name = "." + beanNameData[beanNameData.length - 1];
            }
            Message message = new Message();
            message.key = typeName;
            message.value = String.format(MESSAGE_FORMAT, name, typeName);
            messages.add(message);
        });
        messages.sort(Comparator.comparing(message -> message.key));
        writeView(BEAN_VIEW_FILE, messages.stream()
                .map(message -> message.value).collect(Collectors.toList()));
    }

    /**
     * 查看容器中所有的 {@link ConfigurationProperties}
     */
    @Test
    public void lookupAllConfigurationProperties() throws Exception {
        ArrayList<Message> messages = new ArrayList<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            if (bean.getClass().isAnnotationPresent(ConfigurationProperties.class)) {
                ConfigurationProperties configurationProperties = bean.getClass().getAnnotation(ConfigurationProperties.class);
                String prefix = configurationProperties.prefix();
                prefix = StringUtils.isNotBlank(prefix) ? prefix : configurationProperties.value();
                Message message = new Message();
                message.key = prefix;
                message.value = String.format(MESSAGE_FORMAT, prefix, typeName);
                messages.add(message);
            }
        });
        messages.sort(Comparator.comparing(message -> message.key));
        writeView(CONFIGURATION_PROPERTIES_VIEW_FILE, messages.stream()
                .map(message -> message.value).collect(Collectors.toList()));
    }

    /**
     * 查看容器中所有用到的 {@link Value}
     * @throws Exception
     */
    @Test
    public void lookupAllValue() throws Exception {
        ArrayList<Message> messages = new ArrayList<>();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            Arrays.stream(bean.getClass().getFields()).forEach(field -> {
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    Message message = new Message();
                    message.key = value.value();
                    message.value = String.format(MESSAGE_FORMAT, value.value(), typeName);
                    messages.add(message);
                }
            });
        });
        messages.sort(Comparator.comparing(message -> message.key));
        writeView(VALUE_VIEW_FILE, messages.stream()
                .map(message -> message.value).collect(Collectors.toList()));
    }

    /**
     * 查看容器中所有定义的切面
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void lookupAllAop() throws Exception {
        Class[] aops = {After.class, AfterReturning.class, AfterThrowing.class, Around.class, Aspect.class, Before.class, Pointcut.class};
        ArrayList<Message> messages = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            String typeName = applicationContext.getBean(beanName).getClass().getTypeName();
            for (Method method : bean.getClass().getMethods()) {
                for (Class<? extends Annotation> aop : aops) {
                    if (method.isAnnotationPresent(aop)) {
                        Annotation annotation = method.getAnnotation(aop);
                        Message message = new Message();
                        message.key = typeName;
                        message.value = String.format(MESSAGE_FORMAT,
                                aop.getSimpleName() + "#" + aop.getMethod("value").invoke(annotation), typeName);
                        messages.add(message);
                    }
                }
            }
        }
        messages.sort(Comparator.comparing(message -> message.key));
        writeView(AOP_VIEW_FILE, messages.stream()
                .map(message -> message.value).collect(Collectors.toList()));
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

    class Message {
        String key;
        String value;
    }
}
