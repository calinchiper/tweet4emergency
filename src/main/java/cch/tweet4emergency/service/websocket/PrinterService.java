package cch.tweet4emergency.service.websocket;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import org.springframework.stereotype.Service;

@Service
public class PrinterService {

    public void print(EarthquakeRelatedInfo info) {
        System.out.println(info);
    }
}
