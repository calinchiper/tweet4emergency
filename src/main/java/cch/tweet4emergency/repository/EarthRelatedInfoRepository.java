package cch.tweet4emergency.repository;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import org.springframework.data.repository.CrudRepository;

public interface EarthRelatedInfoRepository extends CrudRepository<EarthquakeRelatedInfo, Long> {
}
