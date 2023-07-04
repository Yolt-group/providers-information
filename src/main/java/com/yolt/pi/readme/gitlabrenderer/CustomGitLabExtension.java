package com.yolt.pi.readme.gitlabrenderer;

import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CustomGitLabExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension, Formatter.FormatterExtension {

    public static CustomGitLabExtension create() {
        return new CustomGitLabExtension();
    }

    @Override
    public void rendererOptions(final @NotNull MutableDataHolder options) {
        //NOP
    }

    @Override
    public void parserOptions(final MutableDataHolder options) {
        //NOP
    }

    @Override
    public void extend(final Formatter.Builder formatterBuilder) {
        //NOP
    }

    @Override
    public void extend(final com.vladsch.flexmark.parser.Parser.Builder parserBuilder) {
        //NOP
    }

    @Override
    public void extend(final com.vladsch.flexmark.html.HtmlRenderer.Builder htmlRendererBuilder,
                       final @NotNull String rendererType) {
        if (htmlRendererBuilder.isRendererType("HTML")) {
            htmlRendererBuilder.nodeRendererFactory(new CustomGitLabNodeRenderer.Factory());
        }
    }
}