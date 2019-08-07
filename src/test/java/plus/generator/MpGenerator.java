package plus.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created on 2019/8/7
 *
 * @author zhongyongbin
 */
public class MpGenerator {

    public static void main(String[] args) {
        MpGenerator mpGenerator = new MpGenerator();
        mpGenerator.generate();
    }

    private void generate() {
        AutoGenerator mpg = new AutoGenerator();
        globalConfig(mpg);
        dataSourceConfig(mpg);
        strategyConfig(mpg);
        packageConfig(mpg);
        templateConfig(mpg);
    }

    /**
     * 全局配置
     */
    private void globalConfig(AutoGenerator mpg) {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(GlobalConfigConstants.OUTPUT_DIR);
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);
//        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setAuthor(GlobalConfigConstants.AUTHOR);

        gc.setMapperName(GlobalConfigConstants.MAPPER_NAME);
        gc.setXmlName(GlobalConfigConstants.XML_MAPPER_NAME);

        gc.setIdType(GlobalConfigConstants.ID_TYPE);

        mpg.setGlobalConfig(gc);
    }

    /**
     * 数据源配置
     */
    private void dataSourceConfig(AutoGenerator mpg) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DataSourceConfigConstants.DB_TYPE);
        dsc.setDriverName(DataSourceConfigConstants.DRIVER_NAME);
        dsc.setUsername(DataSourceConfigConstants.USER_NAME);
        dsc.setPassword(DataSourceConfigConstants.PASSWORD);
        dsc.setUrl(DataSourceConfigConstants.JDBC_URL);
        mpg.setDataSource(dsc);
    }

    /**
     * 生成策略配置
     */
    private void strategyConfig(AutoGenerator mpg) {
        StrategyConfig strategy = new StrategyConfig();
        if (Objects.nonNull(StrategyConfigConstants.TABLE_PREFIX) && StrategyConfigConstants.TABLE_PREFIX.length > 0) {
            strategy.setTablePrefix(StrategyConfigConstants.TABLE_PREFIX);
        }
        strategy.setNaming(NamingStrategy.underline_to_camel);
        if (Objects.nonNull(StrategyConfigConstants.TABLE_FILL_LIST) && StrategyConfigConstants.TABLE_FILL_LIST.size() > 0) {
            strategy.setTableFillList(StrategyConfigConstants.TABLE_FILL_LIST);
        }

        if (Objects.nonNull(StrategyConfigConstants.INCLUDE) && StrategyConfigConstants.INCLUDE.length > 0) {
            strategy.setInclude(StrategyConfigConstants.INCLUDE);
        }

        if (Objects.nonNull(StrategyConfigConstants.EXCLUDE) && StrategyConfigConstants.EXCLUDE.length > 0) {
            strategy.setExclude(StrategyConfigConstants.EXCLUDE);
        }
    }

    /**
     * 包名配置
     */
    private void packageConfig(AutoGenerator mpg) {
        PackageConfig pc = new PackageConfig();
        pc.setParent(PackageConfigConstants.PACKAGE_NAME);
        pc.setModuleName(PackageConfigConstants.MODULE_NAME);
        pc.setMapper(PackageConfigConstants.MAPPER_NAME);
        pc.setEntity(PackageConfigConstants.ENTITY_NAME);
        mpg.setPackageInfo(pc);
    }

    /**
     * 模板配置
     */
    private void templateConfig(AutoGenerator mpg) {
        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntityKt(null);
        tc.setXml(null);

        mpg.setTemplate(tc);
    }

    static class GlobalConfigConstants {

        static final String OUTPUT_DIR = System.getProperty("user.dir");

        static final String AUTHOR = "Mybatis-Plus Generator";

        static final String MAPPER_NAME = "%sMapper";

        static final String XML_MAPPER_NAME = "%sMapper";

        static final IdType ID_TYPE = IdType.AUTO;
    }

    static class DataSourceConfigConstants {

        static final DbType DB_TYPE = DbType.MYSQL;

        static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

        static final String USER_NAME = "root";

        static final String PASSWORD = "root";

        static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8";
    }

    static class StrategyConfigConstants {
        static final String[] TABLE_PREFIX = new String[]{};

        static final List<TableFill> TABLE_FILL_LIST = new ArrayList<TableFill>() {{
            this.add(new TableFill("createTime", FieldFill.INSERT));
            this.add(new TableFill("updateTime", FieldFill.INSERT_UPDATE));
        }};

        static final String[] INCLUDE = new String[]{};

        static final String[] EXCLUDE = new String[]{};
    }

    static class PackageConfigConstants {
        static final String PACKAGE_NAME = "com.bz";

        static final String MODULE_NAME = "gists";

        static final String MAPPER_NAME = "mapper";

        static final String ENTITY_NAME = "domain.entity";
    }
}
