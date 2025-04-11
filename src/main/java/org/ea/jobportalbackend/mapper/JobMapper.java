package org.ea.jobportalbackend.mapper;

import org.ea.jobportalbackend.dto.JobDTO;
import org.ea.jobportalbackend.model.Job;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postedDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "source", constant = "internal")
    Job toEntity(JobDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJobFromDto(JobDTO dto, @MappingTarget Job job);
}

