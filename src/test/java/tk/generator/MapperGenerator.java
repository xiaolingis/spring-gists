package tk.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.apache.ibatis.io.Resources.getResourceAsStream;

/**
 * Created on 2019/1/23
 *
 * @author zhongyongbin
 */
public final class MapperGenerator {

    private static final String CONFIG_FILE = "tk/generatorConfig.xml";

    private static final String GENERATED_SOURCES = "generated-sources";


    public static void main(String[] args) throws Exception {
        new File(GENERATED_SOURCES).mkdirs();

        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config =
                cp.parseConfiguration(getResourceAsStream(CONFIG_FILE));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
