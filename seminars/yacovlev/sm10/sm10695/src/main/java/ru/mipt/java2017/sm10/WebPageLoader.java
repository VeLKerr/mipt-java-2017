package ru.mipt.java2017.sm10;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class WebPageLoader {

    private final HttpClient client = HttpClients.createSystem();

    public String loadPageAsString(String url) {
        HttpUriRequest request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            // TODO report an error
            return "";
        }
        if (response.getStatusLine().getStatusCode()!=200) {
            // TODO report an error
            return "";
        }
        try {
            InputStream dataStream = response.getEntity().getContent();
            return IOUtils.toString(dataStream, "UTF-8");
        } catch (IOException e) {
            // TODO report an error
            return "";
        }
    }

    public static void main(String args[]) {
        String habrLink = "https://habrahabr.ru";
        String contents = new WebPageLoader().loadPageAsString(habrLink);
        System.out.println(contents);
    }

}
