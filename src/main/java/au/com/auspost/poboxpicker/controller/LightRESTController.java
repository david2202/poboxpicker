package au.com.auspost.poboxpicker.controller;

import au.com.auspost.poboxpicker.LightResponse;
import au.com.auspost.poboxpicker.service.BoxSearchService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LightRESTController {
    private static final Logger LOG = LoggerFactory.getLogger(LightRESTController.class);

    @Value("${au.com.auspost.poboxpicker.arduinoEnabled}")
    private Boolean arduinoEnabled;

    @Value("${au.com.auspost.poboxpicker.arduinoUrl}")
    private String arduinoUrl;

    @Autowired
    private BoxSearchService boxSearchService;

    @RequestMapping("/rest/light/{number}")
    public LightResponse box(@PathVariable Integer number) throws IOException {
        if (arduinoEnabled) {
            LOG.info("Lighting up light number {}", number);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(arduinoUrl + number);
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
        } else {
            LOG.info("Arduino disabled for light number {}", number);
        }
        LightResponse br = new LightResponse();
        br.setNumber(number);
        return br;
    }
}
