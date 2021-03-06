package org.noahsark.http.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@Slf4j
public class HttpClientRunner implements ApplicationRunner {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    @Qualifier("h2OkHttpClient")
    private OkHttpClient okHttpClient;
    @Value("${server.url}")
    private String url;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        callWithClient(url);
        callWithRestTemplate(url);

    }

    private void callWithClient(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        log.info("Response: {}", response);
        log.info("Response Body: {}", response.body().string());
    }

    private void callWithRestTemplate(String url) {
        ResponseEntity<String> body = restTemplate.getForEntity(url, String.class);
        log.info("Call {}, return [{}]", url, body);
    }
}
