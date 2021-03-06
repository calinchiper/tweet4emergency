package cch.tweet4emergency.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cch.tweet4emergency.model.GeoLocationAccuracy.HIGH;
import static cch.tweet4emergency.model.GeoLocationAccuracy.LOW;
import static cch.tweet4emergency.model.GeoLocationAccuracy.MEDIUM;

@Entity
public class EarthquakeRelatedInfo implements Serializable {
    @Id
    @GeneratedValue
    @Column private long id;
    @Column private final String owner;
    @Column private final String content;
    @Column private final Date date;
    @Transient private final Optional<GeoLocation> tweetGeoLocation;
    @Transient private final Optional<GeoLocation> countryGeoLocation;
    @Column private GeoLocationAccuracy geoLocationAccuracy;
    @Column private Certitude certitude = Certitude.NEUTRAL;

    public EarthquakeRelatedInfo(Status status) {
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

    public String getDate() {
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
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

    public Certitude getCertitude() {
        return certitude;
    }

    public void setCertitude(Certitude certitude) {
        this.certitude = certitude;
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
        return "EarthquakeRelatedInfo{" +
                "content='" + content + '\'' +
                ", owner='" + owner + '\'' +
                ", date=" + date +
                ", tweetGeoLocation=" + tweetGeoLocation +
                ", countryGeoLocation=" + countryGeoLocation +
                ", geoLocationAccuracy=" + geoLocationAccuracy +
                ", certitude=" + certitude +
                '}';
    }

    public String toJsonText() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
