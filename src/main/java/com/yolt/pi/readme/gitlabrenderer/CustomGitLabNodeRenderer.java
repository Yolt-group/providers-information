package com.yolt.pi.readme.gitlabrenderer;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.HtmlRendererOptions;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.data.DataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

class CustomGitLabNodeRenderer implements NodeRenderer {

    protected static final String MERMAID_DIV_CLASS = "mermaid";
    protected static final String JSON_DIV_CLASS = "json";
    protected static final String OTHER_DIV_CLASS = "other";

    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        Set<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(FencedCodeBlock.class, this::render));
        return set;
    }

    private void render(final FencedCodeBlock node,
                        final NodeRendererContext context,
                        final HtmlWriter html) {
        HtmlRendererOptions htmlOptions = context.getHtmlOptions();

        html.line();
        String divClass = switch (node.getInfo().toString()) {
            case "mermaid" -> MERMAID_DIV_CLASS;
            case "json5" -> JSON_DIV_CLASS;
            default -> OTHER_DIV_CLASS;
        };
        html.srcPosWithTrailingEOL(node.getChars())
                .attr("class", divClass)
                .withAttr()
                .tag("div")
                .line()
                .openPre();
        context.renderChildren(node);
        html.closePre().tag("/div");
        html.lineIf(htmlOptions.htmlBlockCloseTagEol);
    }

    public static class Factory implements NodeRendererFactory {

        @NotNull
        public NodeRenderer apply(@NotNull DataHolder options) {
            return new CustomGitLabNodeRenderer();
        }
    }
}
