package com.teamreact.app.service.mapper;

import com.teamreact.app.domain.*;
import com.teamreact.app.service.dto.TimeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Time and its DTO TimeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TimeMapper extends EntityMapper<TimeDTO, Time> {

    

    

    default Time fromId(Long id) {
        if (id == null) {
            return null;
        }
        Time time = new Time();
        time.setId(id);
        return time;
    }
}
