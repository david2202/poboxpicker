package au.com.auspost.poboxpicker.controller;

import au.com.auspost.poboxpicker.BoxResponse;
import au.com.auspost.poboxpicker.domain.Box;
import au.com.auspost.poboxpicker.service.BoxIndex;
import au.com.auspost.poboxpicker.service.BoxSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class BoxRESTController {
    @Value("${au.com.auspost.poboxpicker.arduinoUrl}")
    private String arduinoUrl;

    @Autowired
    private BoxSearchService boxSearchService;
/*
    @RequestMapping("/rest/box/")
    public BoxResponse box(@RequestParam("number") Integer box) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(arduinoUrl + box);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
        BoxResponse br = new BoxResponse();
        br.setBox(box);
        return br;
    }
*/
    @RequestMapping("/rest/box")
    public List<Box> box(@RequestParam("searchTerm") String searchTerm) {
        return boxSearchService.search(searchTerm);
    }
}
