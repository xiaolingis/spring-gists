package tk.generator.formatter;

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
public class MyXmlFormatter extends DefaultXmlFormatter {

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
                    newContent.append(comment("<!-- ============ 通用查询映射结果 ============ -->"));
                }
                if (StringUtils.startsWith(line, SPACE + "<resultMap extends=\"BaseResultMap\" id=\"ResultMapWithBLOBs\"")) {
                    newContent.append(comment("<!-- 大字段查询映射结果 -->"));
                }
                if (StringUtils.equals(line, SPACE + "<sql id=\"Base_Column_List\">")) {
                    newContent.append(comment("<!-- 通用查询结果列 -->"));
                }
                if (StringUtils.equals(line, SPACE + "<sql id=\"Blob_Column_List\">")) {
                    newContent.append(comment("<!-- 大字段查询结果列 -->"));
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
