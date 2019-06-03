package generator.formatter;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created on 2019/1/14
 *
 * @author zhongyongbin
 */
public class FormatXmlFormatter extends DefaultXmlFormatter {

    private static final String SPACE = "    ";

    @Override
    public String getFormattedContent(Document document) {
        String formattedContent = document.getFormattedContent();
        formattedContent = format(formattedContent);
        formattedContent = addComment(formattedContent);
        return formattedContent;
    }

    private String addComment(String content) {
        StringBuilder newContent = new StringBuilder();
        try {
            StringReader stringReader = new StringReader(content);
            BufferedReader bufferedReader = new BufferedReader(stringReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (StringUtils.startsWith(line, SPACE + "<resultMap id=\"BaseResultMap\"")) {
                    newContent.append(comment("<!-- ============ MyBatis Generator 生成文件，不要编辑该文件 ============ -->"));
                    newContent.append(comment("<!-- ============ 通用查询映射结果 ============ -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<resultMap extends=\"BaseResultMap\" id=\"ResultMapWithBLOBs\"")) {
                    newContent.append(comment("<!-- 大字段查询映射结果 -->"));
                } else if (StringUtils.equals(line, SPACE + "<sql id=\"Base_Column_List\">")) {
                    newContent.append(comment("<!-- 通用查询结果列 -->"));
                } else if (StringUtils.equals(line, SPACE + "<sql id=\"Blob_Column_List\">")) {
                    newContent.append(comment("<!-- 大字段查询结果列 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<select id=\"selectByExampleWithBLOBs\"")) {
                    newContent.append(comment("<!-- 根据 Example 查询，包含大字段 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<insert id=\"insert\"")) {
                    newContent.append(comment("<!-- 插入记录 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<insert id=\"insertSelective\"")) {
                    newContent.append(comment("<!-- 插入记录，只插入非空字段 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<select id=\"selectByPrimaryKey\"")) {
                    newContent.append(comment("<!-- 根据主键查询 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<update id=\"updateByPrimaryKey\"")) {
                    newContent.append(comment("<!-- 根据主键更新 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<update id=\"updateByPrimaryKeySelective\"")) {
                    newContent.append(comment("<!-- 根据主键更新记录，只更新非空字段 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<delete id=\"deleteByPrimaryKey\"")) {
                    newContent.append(comment("<!-- 根据主键删除 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<sql id=\"Example_Where_Clause\">")) {
                    newContent.append(comment("<!-- Example 查询子句 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<sql id=\"Update_By_Example_Where_Clause\">")) {
                    newContent.append(comment("<!-- Example 更新子句 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<select id=\"selectByExample\"")) {
                    newContent.append(comment("<!-- 根据 Example 查询 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<select id=\"countByExample\"")) {
                    newContent.append(comment("<!-- 根据 Example 查询数量 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<update id=\"updateByExampleSelective\"")) {
                    newContent.append(comment("<!-- 根据 Example 更新非空字段 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<update id=\"updateByExample\"")) {
                    newContent.append(comment("<!-- 根据 Example 更新 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<delete id=\"deleteByExample\"")) {
                    newContent.append(comment("<!-- 根据 Example 删除 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<update id=\"updateByPrimaryKeyWithBLOBs\"")) {
                    newContent.append(comment("<!-- 根据主键更新，包含大字段 -->"));
                } else if (StringUtils.startsWith(line, SPACE + "<update id=\"updateByExampleWithBLOBs\"")) {
                    newContent.append(comment("<!-- 根据 Example 更新，包含大字段 -->"));
                }
                newContent.append(line);
                newContent.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return newContent.toString();
    }

    private String comment(String comment) {
        StringBuilder commentBuilder = new StringBuilder();
        commentBuilder.append(System.lineSeparator());
        commentBuilder.append(SPACE).append(comment);
        commentBuilder.append(System.lineSeparator());
        return commentBuilder.toString();
    }

    private String format(String content) {
        content = RegExUtils.replaceAll(content, ",jdbcType=.*?}", "}");
        content = StringUtils.replace(content, "  ", SPACE);
        return content;
    }
}
