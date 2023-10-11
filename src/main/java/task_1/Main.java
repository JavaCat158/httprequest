package task_1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String json = EntityUtils.toString(entity);
                List<CatFact> catFacts = Arrays.asList(new ObjectMapper().readValue(json, CatFact[].class));

                List<CatFact> filteredFacts = catFacts.stream()
                        .filter(fact -> fact.getUpvotes() != null)
                        .toList();

                for (CatFact fact : filteredFacts) {
                    System.out.println(fact.getText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

