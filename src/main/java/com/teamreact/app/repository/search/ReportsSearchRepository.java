package com.teamreact.app.repository.search;

import com.teamreact.app.domain.Reports;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reports entity.
 */
public interface ReportsSearchRepository extends ElasticsearchRepository<Reports, Long> {
}
