package cch.tweet4emergency.streaming;

import org.apache.spark.storage.StorageLevel;
import twitter4j.FilterQuery;

/**
 * Receives twitter statuses that contains earthquake related keywords
 */
public class TwitterEarthquakeRelatedStatusReceiver extends TwitterStatusReceiver {
    TwitterEarthquakeRelatedStatusReceiver(StorageLevel storageLevel) {
        super(storageLevel);
    }

    @Override
    public FilterQuery onFiltering() {
        FilterQuery filterQuery = new FilterQuery();
        String[] keywords = {
                "earthquake",
                "magnitude",
                "seismic",
                "tremor"
        };
        filterQuery.track(keywords);
        return filterQuery;
    }
}
