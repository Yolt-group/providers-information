import React, { useEffect } from "react";
import 'github-markdown-css';
import { useDispatch, useSelector } from "react-redux";
import { fetchReadme } from "./action";
import { selectSelections } from "../SearchList/searchListSlice";
import { selectReadme } from "./siteReadmeSlice";
import mermaid from "mermaid"


export const SiteReadmeList = () => {
    const dispatch = useDispatch()
    const { selectAllToggled, selectedSiteIds } = useSelector(selectSelections)
    const siteReadmeElement = useSelector(selectReadme)
    mermaid.initialize({startOnLoad:true})
    useEffect(() => {
      mermaid.contentLoaded()
    }, [])

    useEffect(() => {
        dispatch(fetchReadme(0));
    }, [dispatch]);

    const filteredReadme = siteReadmeElement.data.filter((el: any) => {
        return selectAllToggled || selectedSiteIds.includes(el.siteId);
    }).map(
        (el: any) => {
            const decodedReadme = el.content != null ? el.content : "--NULL--";
            const readme = parseMermaid(decodedReadme)
            const dangerousReadmeHtml = { __html: readme };
            return (
                <div className="markdown-body">
                    <div dangerouslySetInnerHTML={dangerousReadmeHtml}></div>
                </div>
            );
        }
    );
    return (
        <div>
            {filteredReadme}
        </div>
    );
}

function parseMermaid(htmlToParse: string) {
    const parser = new DOMParser();
    const htmlDoc = parser.parseFromString(htmlToParse, 'text/html');
    const mermaidDivs = htmlDoc.querySelectorAll('[class="mermaid"]');
    mermaidDivs.forEach(e => {
        var hack = Math.random().toString(36).substring(7).replace(/\d/, "a");
        mermaid.render(hack, e.textContent ?? "", (e1) => {
            e.innerHTML = e1;
        });
    });
    const parsedHtml = htmlDoc.querySelector('body')?.innerHTML ?? "";
    return parsedHtml;
}
