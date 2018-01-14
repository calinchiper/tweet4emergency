package cch.tweet4emergency.factory;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import org.springframework.stereotype.Service;
import twitter4j.Status;

@Service
public class EarthquakeRelatedInfoFactory {

    public EarthquakeRelatedInfo extract(Status status) {
        return new EarthquakeRelatedInfo(status);
    }
}
