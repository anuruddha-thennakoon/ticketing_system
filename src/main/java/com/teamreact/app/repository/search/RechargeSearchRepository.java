package com.teamreact.app.repository.search;

import com.teamreact.app.domain.Recharge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Recharge entity.
 */
public interface RechargeSearchRepository extends ElasticsearchRepository<Recharge, Long> {
}
