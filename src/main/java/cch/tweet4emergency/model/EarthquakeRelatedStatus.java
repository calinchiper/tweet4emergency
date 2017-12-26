package cch.tweet4emergency.model;

import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cch.tweet4emergency.model.GeoLocationAccuracy.HIGH;
import static cch.tweet4emergency.model.GeoLocationAccuracy.LOW;
import static cch.tweet4emergency.model.GeoLocationAccuracy.MEDIUM;

public class EarthquakeRelatedStatus implements Serializable {
    private final String content;
    private final String owner;
    private final Date date;
    private final Optional<GeoLocation> tweetGeoLocation;
    private final Optional<GeoLocation> countryGeoLocation;
    private GeoLocationAccuracy geoLocationAccuracy;
    private boolean plausibility;

    public EarthquakeRelatedStatus(Status status) {
        this.content = status.getText();
        this.owner = status.getUser().getName();
        this.date = status.getCreatedAt();
        this.tweetGeoLocation = Optional.ofNullable(status.getGeoLocation());
        this.countryGeoLocation = Optional.ofNullable(findCentre(status.getPlace()));
        this.setGeoLocationAccuracy();
    }

    public String getContent() {
        return this.content;
    }

    public String getOwner() {
        return this.owner;
    }

    public Date getDate() {
        return this.date;
    }

    public GeoLocation getTweetGeoLocation() {
        return tweetGeoLocation.orElse(new GeoLocation(-1, -1));
    }

    public GeoLocation getCountryGeoLocation() {
        return countryGeoLocation.orElse(new GeoLocation(-1, -1));
    }

    public GeoLocationAccuracy getGeoAccuracy() {
        return this.geoLocationAccuracy;
    }

    public boolean isPlausible() {
        return plausibility;
    }

    public void setPlausibility(boolean value) {
        this.plausibility = value;
    }

    private GeoLocation findCentre(Place place) {
        if (place != null) {

            List<GeoLocation> coordinates = Arrays.stream(place.getBoundingBoxCoordinates())
                    .flatMap(array -> Arrays.stream(array))
                    .collect(Collectors.toList());
            double latitude = coordinates.stream()
                    .mapToDouble(value -> value.getLatitude())
                    .sum();
            double longitude = coordinates.stream()
                    .mapToDouble(value -> value.getLongitude())
                    .sum();

            return new GeoLocation(latitude / 4.0, longitude / 4.0);
        } else {
            return null;
        }


    }

    private void setGeoLocationAccuracy() {
        if (tweetGeoLocation.isPresent()) {
            geoLocationAccuracy = HIGH;
        } else if (countryGeoLocation.isPresent()) {
            geoLocationAccuracy = MEDIUM;
        } else {
            geoLocationAccuracy = LOW;
        }
    }

    @Override
    public String toString() {
        return "EarthquakeRelatedStatus{" +
                "content='" + content + '\'' +
                ", owner='" + owner + '\'' +
                ", date=" + date +
                ", tweetGeoLocation=" + tweetGeoLocation +
                ", countryGeoLocation=" + countryGeoLocation +
                ", geoLocationAccuracy=" + geoLocationAccuracy +
                ", plausibility=" + plausibility +
                '}';
    }
}
