package com.teamreact.app.repository.search;

import com.teamreact.app.domain.Journey;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Journey entity.
 */
public interface JourneySearchRepository extends ElasticsearchRepository<Journey, Long> {
}
