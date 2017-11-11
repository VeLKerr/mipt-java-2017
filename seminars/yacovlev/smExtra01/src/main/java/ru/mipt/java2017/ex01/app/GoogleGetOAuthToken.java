package ru.mipt.java2017.ex01.app;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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

public class GoogleGetOAuthToken {
    private HttpClient httpClient = HttpClients.createSystem();

    private static final String GOOGLE_AUTH_API_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_API_URL = "https://www.googleapis.com/oauth2/v4/token";
    private static final String GOOGLE_DRIVE_API_URL = "https://www.googleapis.com/drive/v2";
    private static final String SCOPE = "https://www.googleapis.com/auth/drive";
    private static final String oauthClientId;
    private static final String oauthClientSecret;

    private String responseCode = ""; //declared as field for use by SWT event

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("keys-keep-in-secret.properties"));
        } catch (IOException e) {
            System.err.println("Please create secret properties file!");
            System.exit(1);
        }
        oauthClientId = properties.getProperty("oauthClientId");
        oauthClientSecret = properties.getProperty("oauthClientSecret");
    }

    public String getAuthCode() {
        String url = "";
        try {
            URIBuilder builder = new URIBuilder(GOOGLE_AUTH_API_URL);
            builder.addParameter("client_id", oauthClientId);
            builder.addParameter("response_type", "code");
            builder.addParameter("scope", SCOPE);
            builder.addParameter("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
            builder.addParameter("access_type", "offline");
            url = builder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setText("Please login");
        Browser browser = new Browser(shell, 0);
        browser.setUrl(url);
        browser.addTitleListener(titleEvent -> {
            String value = titleEvent.title;
//            System.out.println("Title changed: " + value);
            if (value.startsWith("Success code=")) {
                String resultCode = value.substring(13);
                responseCode = resultCode;
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
        return responseCode;
    }

    public String getAuthToken(String code) throws IOException {
        URIBuilder builder = null;
        HttpPost request = null;
        try {
            builder = new URIBuilder(GOOGLE_TOKEN_API_URL);
            builder.addParameter("client_id", oauthClientId);
            builder.addParameter("client_secret", oauthClientSecret);
            builder.addParameter("code", code);
            builder.addParameter("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
            builder.addParameter("grant_type", "authorization_code");
            request = new HttpPost(builder.build());
            request.setHeader("content-type", "application/x-www-form-urlencoded");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode()!=200) {
            System.err.println("Error: " + String.valueOf(response.getStatusLine().getStatusCode()));
            return "";
        }
        InputStream responseContents = response.getEntity().getContent();
        String jsonString = IOUtils.toString(responseContents, Charset.forName("UTF-8"));
        JSONObject jsonObject = new JSONObject(jsonString);
        String token = jsonObject.getString("access_token");
        return token;
    }

    public List<String> listFiles(String token) throws IOException {
        URIBuilder uriBuilder = null;
        HttpGet request = null;
        try {
            uriBuilder = new URIBuilder(GOOGLE_DRIVE_API_URL+"/files");
            uriBuilder.addParameter("client_id", oauthClientId);
            uriBuilder.addParameter("client_secret", oauthClientSecret);
            uriBuilder.addParameter("access_token", token);
            request = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode()!=200) {
            System.err.println("Error: " + String.valueOf(response.getStatusLine().getStatusCode()));
            return null;
        }
        InputStream responseContents = response.getEntity().getContent();
        String jsonString = IOUtils.toString(responseContents, Charset.forName("UTF-8"));
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray items = jsonObject.getJSONArray("items");
        List<String> result = new LinkedList<>();
        items.forEach((object) -> {
            JSONObject item = (JSONObject) object;
            String title = item.getString("title");
            result.add(title);
        });
        return result;
    }

    public static void main(String args[]) throws IOException {
        GoogleGetOAuthToken googleClient = new GoogleGetOAuthToken();
        String code = googleClient.getAuthCode();
        System.out.println("Got code: " + code);
        String token = googleClient.getAuthToken(code); // might be saved for future use
        System.out.println("Got token: " + token);
        List<String> files = googleClient.listFiles(token);
        files.forEach(title -> {
            System.out.println("File: " + title);
        });
    }

}
