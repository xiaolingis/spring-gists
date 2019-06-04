package generator.comment;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * Created on 2018-11-01
 *
 * @author zhongyongbin
 */
public class RemarksCommentGenerator extends DefaultCommentGenerator {
    private Properties properties;
    private boolean suppressDate;
    private boolean suppressAllComments;
    private String currentDateStr;

    public RemarksCommentGenerator() {
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    @Override
    public void addComment(XmlElement xmlElement) {
    }

    @Override
    public void addRootComment(XmlElement rootElement) {
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
    }

    @Override
    protected String getDateString() {
        String result = null;
        if (!suppressDate) {
            result = currentDateStr;
        }
        return result;
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        innerEnum.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(sb.toString());
        innerEnum.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            StringBuilder sb = new StringBuilder();
            field.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(remarks);
            field.addJavaDocLine(sb.toString());
            field.addJavaDocLine(" */");
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(String.format(" * Created on %s", DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())));
        topLevelClass.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * 数据库表: ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(sb.toString());
        topLevelClass.addJavaDocLine(" * ");
        topLevelClass.addJavaDocLine(" * @author Mybatis Generator");
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
    }

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
    }
}
