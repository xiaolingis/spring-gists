package generator.formatter;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created on 2019/6/3
 *
 * @author zhongyongbin
 */
public class FormatJavaFormatter extends DefaultJavaFormatter {
    private static final String SPACE = "    ";

    private static final String MAPPER_SUFFIX = "Mapper";

    private static final String EXAMPLE_SUFFIX = "Example";

    @Override
    public String getFormattedContent(CompilationUnit compilationUnit) {
        Type type;
        String name = compilationUnit.getType().getShortName();
        if (StringUtils.endsWith(name, MAPPER_SUFFIX)) {
            type = Type.MAPPER;
        } else if (StringUtils.endsWith(name, EXAMPLE_SUFFIX)) {
            type = Type.EXAMPLE;
        } else {
            type = Type.ENTITY;
        }
        String formattedContent = compilationUnit.getFormattedContent();
        formattedContent = addComment(formattedContent, type);
        return formattedContent;
    }

    private String addComment(String content, Type type) {
        if (type == Type.ENTITY) {
            return content;
        }
        StringBuilder newContent = new StringBuilder();
        try {
            StringReader stringReader = new StringReader(content);
            BufferedReader bufferedReader = new BufferedReader(stringReader);
            String line;
            boolean start = true;
            while ((line = bufferedReader.readLine()) != null) {
                newContent.append(System.lineSeparator());
                if (StringUtils.endsWith(line, "{") && start) {
                    newContent.append("/**");
                    newContent.append(System.lineSeparator());
                    newContent.append(String.format(" * Created on %s", DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())));
                    newContent.append(System.lineSeparator());
                    newContent.append(" *");
                    newContent.append(System.lineSeparator());
                    newContent.append(" * @author Mybatis Generator");
                    newContent.append(System.lineSeparator());
                    newContent.append(" */");
                    newContent.append(System.lineSeparator());
                    newContent.append(line);
                    if (type == Type.MAPPER) {
                        newContent.append(comment("/* ======== start:自动生成 ======== */"));
                    }
                    start = false;
                } else if (StringUtils.startsWith(line, "}") && type == Type.MAPPER) {
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

    enum Type {
        ENTITY, MAPPER, EXAMPLE
    }
}
