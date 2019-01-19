package generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.plugins.ToStringPlugin;

import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
  * Created on 2018-11-01
  *
  * @author zhongyongbin
  */
public class MyToStringPlugin extends ToStringPlugin {

    private boolean useToStringFromRoot;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        useToStringFromRoot = isTrue(properties.getProperty("useToStringFromRoot"));
    }

    @Override
    public boolean validate(List<String> warnings) {
        return super.validate(warnings);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    private void generateToString(IntrospectedTable introspectedTable,
                                  TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString");
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }

        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            context.getCommentGenerator().addGeneralMethodAnnotation(method,
                    introspectedTable, topLevelClass.getImportedTypes());
        } else {
            context.getCommentGenerator().addGeneralMethodComment(method,
                    introspectedTable);
        }

        String className = topLevelClass.getType().getShortName();
        method.addBodyLine(String.format("StringBuilder sb = new StringBuilder(\"%s{\");", className));
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Field field : topLevelClass.getFields()) {
            String property = field.getName();
            sb.setLength(0);
            String javaType = field.getType().getShortName();
            if (i == 0) {
                if ("String".equals(javaType)) {
                    sb.append("sb.append(\"").append(property)
                            .append("=\'\")").append(".append(").append(property)
                            .append(")").append(".append(\"\'\");");
                } else {
                    sb.append("sb.append(\"").append(property)
                            .append("=\")").append(".append(").append(property)
                            .append(");");
                }
            } else {
                if ("String".equals(javaType)) {
                    sb.append("sb.append(\"").append(", ").append(property)
                            .append("=\'\")").append(".append(").append(property)
                            .append(")").append(".append(\"\'\");");
                } else {
                    sb.append("sb.append(\"").append(", ").append(property)
                            .append("=\")").append(".append(").append(property)
                            .append(");");
                }
            }
            ++i;
            method.addBodyLine(sb.toString());
        }

        method.addBodyLine("sb.append(\"}\");");
        if (useToStringFromRoot && topLevelClass.getSuperClass() != null) {
            method.addBodyLine("sb.append(\", from super class \");");
            method.addBodyLine("sb.append(super.toString());");
        }
        method.addBodyLine("return sb.toString();");

        topLevelClass.addMethod(method);
    }
}
