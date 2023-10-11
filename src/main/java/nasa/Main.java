package nasa;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();

            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=6G09KL8cHz0rxDtt1LylWrtqHvE5l4u94gSDX95Q");
            CloseableHttpResponse response = httpClient.execute(request);

            String jsonString = EntityUtils.toString(response.getEntity());


            ObjectMapper mapper = new ObjectMapper();
            NasaApi nasaResponse = mapper.readValue(jsonString, NasaApi.class);

            saveJsonToFile(jsonString, "nasa.json");

            String fileName = nasaResponse.getUrl().substring(nasaResponse.getUrl().lastIndexOf("/") + 1);

            try (InputStream in = new URL(nasaResponse.getUrl()).openStream()) {
                Files.copy(in, Paths.get(fileName));
            }

            response.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveJsonToFile(String json, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

