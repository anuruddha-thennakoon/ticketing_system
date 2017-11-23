package com.teamreact.app.repository.search;

import com.teamreact.app.domain.Passenger;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Passenger entity.
 */
public interface PassengerSearchRepository extends ElasticsearchRepository<Passenger, Long> {
}
