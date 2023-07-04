package com.yolt.pi.readme.gitlabrenderer;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadmeRenderer {

    private static final Parser PARSER;
    private static final HtmlRenderer RENDERER;

    static {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), CustomGitLabExtension.create()));

        PARSER = Parser.builder(options).build();
        RENDERER = HtmlRenderer.builder(options).build();
    }

    public static String render(final String markdownInput) {
        Node document = PARSER.parse(markdownInput);
        return RENDERER.render(document);
    }
}
