package org.ea.jobportalbackend.dto;

import lombok.*;
import org.ea.jobportalbackend.model.enums.EducationLevel;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobUpdateDTO {
    private String jobTitle;
    private String companyName;
    private String location;
    private JobType jobType;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String jobDescription;
    private List<String> requirements;
    private ExperienceLevel experienceLevel;
    private EducationLevel educationLevel;
    private String industry;
    private LocalDate applicationDeadline;
    private String howtoApply;
    private String companyLogo;
    private List<String> benefits;
    private List<Long> tagIds;
    private String source;
}
