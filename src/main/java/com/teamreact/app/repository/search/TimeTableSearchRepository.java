package com.teamreact.app.repository.search;

import com.teamreact.app.domain.TimeTable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TimeTable entity.
 */
public interface TimeTableSearchRepository extends ElasticsearchRepository<TimeTable, Long> {
}
