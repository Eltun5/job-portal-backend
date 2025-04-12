package org.ea.jobportalbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.ea.jobportalbackend.model.enums.EducationLevel;
import org.ea.jobportalbackend.model.enums.ExperienceLevel;
import org.ea.jobportalbackend.model.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "job_title")
    private String jobTitle;

    @Column(nullable = false, name = "company_name")
    private String companyName;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "job_type")
    private JobType jobType;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    @Column(nullable = false, name = "job_description", length = 1000)
    private String jobDescription;

    @ElementCollection
    @Column(nullable = false)
    private List<String> requirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "experience_level")
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "education_level")
    private EducationLevel educationLevel;

    @Column(nullable = false)
    private String industry;

    @Column(name = "poated_date")
    private LocalDate postedDate;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(nullable = false, name = "how_to_apply")
    private String howtoApply;

    @Column(name = "company_logo")
    private String companyLogo;

    @ElementCollection
    private List<String> benefits;

    @ManyToMany
    @JoinTable(
            name = "job_tags",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    private String source;

    @PrePersist
    private void init() {
        postedDate = LocalDate.now();
    }
}
