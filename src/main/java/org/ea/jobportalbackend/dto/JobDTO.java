package org.ea.jobportalbackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.model.enums.EducationLevel;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record JobDTO(@NotBlank String jobTitle,
                     @NotBlank String companyName,
                     @NotBlank String location,
                     @NotNull JobType jobType,
                     @DecimalMin(value = "0.0") BigDecimal minSalary,
                     @DecimalMin(value = "0.0") BigDecimal maxSalary,
                     @NotBlank String jobDescription,
                     @NotEmpty List<@NotBlank String> requirements,
                     @NotNull ExperienceLevel experienceLevel,
                     @NotNull EducationLevel educationLevel,
                     @NotBlank String industry,
                     LocalDate applicationDeadline,
                     @NotBlank String howtoApply,
                     List<@NotBlank String> benefits,
                     List<Tag> tags,
                     String source) {
}
