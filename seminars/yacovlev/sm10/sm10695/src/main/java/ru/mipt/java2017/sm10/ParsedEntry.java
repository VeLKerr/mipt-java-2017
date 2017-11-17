package ru.mipt.java2017.sm10;

public class ParsedEntry {
    private String title;
    private String link;
    private String annotationText;
    private String annotationHtml;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: " + title + "\n");
        builder.append("Link: " + link + "\n");
        builder.append("Annotation: " + annotationHtml + "\n\n");
        return builder.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }

    public String getAnnotationHtml() {
        return annotationHtml;
    }

    public void setAnnotationHtml(String annotationHtml) {
        this.annotationHtml = annotationHtml;
    }
}
