package org.ea.jobportalbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "create_at")
    private Instant updateAt;

    @PrePersist
    private void init() {
        createAt = updateAt = Instant.now();
    }
}
