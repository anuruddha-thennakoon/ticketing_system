package com.teamreact.app.repository.search;

import com.teamreact.app.domain.TransportManager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransportManager entity.
 */
public interface TransportManagerSearchRepository extends ElasticsearchRepository<TransportManager, Long> {
}
