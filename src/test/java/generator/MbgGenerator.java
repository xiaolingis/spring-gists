package generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/1/15
 *
 * 将包 generator 以及 resources 都放置于生产项目的测试包中，其涉及的依赖 scope 声明为 test 。直接调用 main 方法生成文件。
 *
 * @author zhongyongbin
 */
public final class MbgGenerator {

    private static final String CONFIG_FILE = "generatorConfig.xml";

    private static final String GENERATED_SOURCES = "generated-sources";

    private static final String RESOURCES = "src/test/resources";

    public static void main(String[] args) throws Exception {
        new File(GENERATED_SOURCES + File.separator + RESOURCES).mkdirs();

        List<String> warnings = new ArrayList<>();
        File configFile = new File(MbgGenerator.class.getResource("/" + CONFIG_FILE).toURI());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
