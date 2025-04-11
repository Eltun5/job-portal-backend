package org.ea.jobportalbackend.mapper;

import org.ea.jobportalbackend.dto.JobUpdateDTO;
import org.ea.jobportalbackend.model.Job;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJobFromDto(JobUpdateDTO dto, @MappingTarget Job job);
}

