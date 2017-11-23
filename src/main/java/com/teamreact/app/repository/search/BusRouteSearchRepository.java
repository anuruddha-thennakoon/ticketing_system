package com.teamreact.app.repository.search;

import com.teamreact.app.domain.BusRoute;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusRoute entity.
 */
public interface BusRouteSearchRepository extends ElasticsearchRepository<BusRoute, Long> {
}
