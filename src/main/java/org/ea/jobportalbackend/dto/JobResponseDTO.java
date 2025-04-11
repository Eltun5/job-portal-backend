package org.ea.jobportalbackend.dto;

import org.ea.jobportalbackend.model.Job;

import java.util.List;

public record JobResponseDTO(List<Job> jobs, FilterOptions filterOptions, SelectedFilters selectedFilters) {

}
