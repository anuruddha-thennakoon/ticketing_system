package com.teamreact.app.repository.search;

import com.teamreact.app.domain.SmartCard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SmartCard entity.
 */
public interface SmartCardSearchRepository extends ElasticsearchRepository<SmartCard, Long> {
}
