package ru.mipt.java2017.sm10;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class JSoupParser {

    public List<ParsedEntry> parseHtml(String contents) {
        List<ParsedEntry> result = new LinkedList<>();

        Document document = Jsoup.parse(contents);
        document.getElementsByTag("article")
                .forEach(articleElement -> {
                    ParsedEntry parsedEntry = new ParsedEntry();
                    Elements aHrefElements = articleElement
                        .getElementsByTag("a");
                    aHrefElements.forEach(aHrefElement -> {
                        if (aHrefElement.attr("class")
                                .equalsIgnoreCase("post__title_link")) {
                            String title = aHrefElement.text();
                            String link = aHrefElement.attr("href");
                            parsedEntry.setTitle(title);
                            parsedEntry.setLink(link);
                        }
                    });

                    // Another way: use jQuery-style
                    Element aHrefElement = articleElement
                            .selectFirst("a.post__title_link");
                    String title = aHrefElement.text();
                    String link = aHrefElement.attr("href");
                    parsedEntry.setTitle(title);
                    parsedEntry.setLink(link);

                    Element divPostTextElement = articleElement
                            .selectFirst("div.post__text");
                    String html = divPostTextElement.html(); // .innerHtml()
                    String text = divPostTextElement.text(); // .innerText()

                    parsedEntry.setAnnotationHtml(html);
                    parsedEntry.setAnnotationText(text);

                    result.add(parsedEntry);
                });

        return result;
    }

    public static void main(String args[]) {
        String contents = new WebPageLoader().loadPageAsString("https://habrahabr.ru");
        List<ParsedEntry> entries = new JSoupParser().parseHtml(contents);
        entries.forEach(entry -> System.out.println(entry));
    }

}
