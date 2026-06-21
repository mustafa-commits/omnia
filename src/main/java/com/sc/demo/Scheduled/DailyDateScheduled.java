package com.sc.demo.Scheduled;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DailyDateScheduled {

    public List<String> data = null;

    String url = "https://www.sistani.org/";


    // سيتم استدعاء هذه الدالة كل يوم عند الساعة 00:00 لتحديث التاريخ
    @Scheduled(cron = "0 0/5 0-1 * * ?")
    public void refreshDataDaily() throws InterruptedException {
        log.info("re sync");
        data = null;
        fetchDataFromSource();
    }

    public List<String> fetchDataFromSource() throws InterruptedException {

        int retryCount = 5;

        while (retryCount > 0) {
            try {
                String html = new RestTemplate().getForObject(url, String.class);

                Document doc = Jsoup.parse(html);
                Element spanElement = doc.selectFirst("span[style=\"margin-left:9px;\"]");
                String tagValue = spanElement.text();

                String[] splitValues = tagValue.split("-");

                List<String> splitList = Arrays.asList(splitValues);
                splitList.set(0, splitList.get(0).split(" ")[1]);
                data = splitList;
                return splitList;
            } catch (Exception e) {
                retryCount--;
                if (retryCount > 0) {
                    log.info("Request failed, retrying... (" + retryCount + " retries remaining)");
                    Thread.sleep(50);
                } else {
                    log.info("Request failed after 5 retries");
                }
            }
        }
        return null;
    }

}
