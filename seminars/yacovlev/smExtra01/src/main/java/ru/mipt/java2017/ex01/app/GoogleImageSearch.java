package ru.mipt.java2017.ex01.app;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class GoogleImageSearch {
    private HttpClient httpClient = HttpClients.createSystem();

    private static final String GOOGLE_API_URL = "https://www.googleapis.com/customsearch/v1";
    private static final String apiKey;
    private static final String searchContext;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("keys-keep-in-secret.properties"));
        } catch (IOException e) {
            System.err.println("Please create secret properties file!");
            System.exit(1);
        }
        apiKey = properties.getProperty("apiKey");
        searchContext = properties.getProperty("searchContext");
    }

    public List<URL> searchImages(String query) throws IOException {
        URIBuilder uriBuilder = null;
        HttpGet request = null;
        try {
            uriBuilder = new URIBuilder(GOOGLE_API_URL);
            uriBuilder.addParameter("key", apiKey);
            uriBuilder.addParameter("cx", searchContext);
            uriBuilder.addParameter("safe", "high");
            uriBuilder.addParameter("num", "10");
            uriBuilder.addParameter("searchType", "image");
            uriBuilder.addParameter("imgSize", "medium");
            uriBuilder.addParameter("fileType", "jpg");
            uriBuilder.addParameter("q", query);
            request = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode()!=200) {
            // TODO report an error
            return null;
        }
        InputStream responseContents = response.getEntity().getContent();
        String jsonString = IOUtils.toString(responseContents, Charset.forName("UTF-8"));
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray items = jsonObject.getJSONArray("items");
        List<URL> result = new LinkedList<>();
        items.forEach((object) -> {
            JSONObject item = (JSONObject) object;
            String link = item.getString("link");
            try {
                result.add(new URL(link));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public static void main(String args[]) throws IOException {
        GoogleImageSearch imageSearch = new GoogleImageSearch();
        List<URL> allBirds = imageSearch.searchImages("bird");
        allBirds.forEach((link) -> {
            System.out.println(link);
        });
    }

}
