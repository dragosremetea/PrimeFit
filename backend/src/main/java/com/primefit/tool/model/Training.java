package com.primefit.tool.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "training")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @SequenceGenerator(name = "training_sequence", sequenceName = "training_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false, columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "pdfUrl", nullable = false, columnDefinition = "TEXT")
    private String pdfUrl;

    @Column(name = "duration", nullable = false, columnDefinition = "FLOAT")
    private float duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_intensity", nullable = false, columnDefinition = "TEXT")
    private TrainingIntensity trainingIntensity;
}
