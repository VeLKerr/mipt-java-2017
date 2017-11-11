package ru.mipt.java2017.ex01.app;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class YandexDiskGetFile {

    private HttpClient httpClient = HttpClients.createSystem();
    private CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    private final static String DISK_URL = "https://webdav.yandex.ru";

    public YandexDiskGetFile(String login, String password) {
        Credentials credentials = new UsernamePasswordCredentials(login, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
    }

    public InputStream doGet(String path) throws IOException {
        HttpGet request = null;
        try {
            URIBuilder builder = new URIBuilder(DISK_URL);
            builder.setPath("/"+path);
            request = new HttpGet(builder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credentialsProvider);
        HttpResponse response = httpClient.execute(request, context);
        int code = response.getStatusLine().getStatusCode();
        int codeClass = code / 100;
        if (codeClass==4 || codeClass==5) { // 400, 404, 500, 503 etc
            // TODO report error
            return null;
        }
        return response.getEntity().getContent();
    }

    public static void main(String args[]) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("yandex-login-password-keep-in-secret.properties"));
        String login = properties.getProperty("login");
        String password = properties.getProperty("password");
        YandexDiskGetFile yandexDisk = new YandexDiskGetFile(login, password);

        InputStream stream = yandexDisk.doGet(args[0]);
        String contents = IOUtils.toString(stream, "UTF-8");
        System.out.println(contents);

    }

}
