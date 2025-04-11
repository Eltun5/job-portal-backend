package org.ea.jobportalbackend.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record JobCreateRequest(@NotNull JobDTO jobDTO, MultipartFile file) {
}
