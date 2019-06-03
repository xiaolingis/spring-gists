package generator.formatter;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created on 2019/6/3
 *
 * @author zhongyongbin
 */
public class FormatJavaFormatter extends DefaultJavaFormatter {
    private static final String SPACE = "    ";

    @Override
    public String getFormattedContent(CompilationUnit compilationUnit) {
        String formattedContent = compilationUnit.getFormattedContent();
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
                newContent.append(System.lineSeparator());
                if (StringUtils.endsWith(line, "{")) {
                    newContent.append(line);
                    newContent.append(comment("/* ======== start:自动生成 ======== */"));
                } else if (StringUtils.startsWith(line, "}")) {
                    newContent.append(comment("/* ======== end:自动生成 ======== */"));
                    newContent.append(line);
                } else {
                    newContent.append(line);
                }

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
}
