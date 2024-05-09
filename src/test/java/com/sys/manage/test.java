package com.sys.manage;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@SpringBootTest
public class test {

    @Value("${videoFilePath}")
    private String videoFilePath;

    @Test
    public void contextLoads() throws IOException {
        File resource = new File(videoFilePath+File.separator+"gif");
        File[] files = resource.listFiles();
        ArrayList<String> resultList = new ArrayList<>();
        for(File file: Objects.requireNonNull(files)){
            String name = file.getName();
            resultList.add(name);
            System.out.println(name);
        }

    }
}
