package com.teamreact.app.repository;

import com.teamreact.app.domain.TimeTable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the TimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    @Query("select distinct time_table from TimeTable time_table left join fetch time_table.routes")
    List<TimeTable> findAllWithEagerRelationships();

    @Query("select time_table from TimeTable time_table left join fetch time_table.routes where time_table.id =:id")
    TimeTable findOneWithEagerRelationships(@Param("id") Long id);

}
