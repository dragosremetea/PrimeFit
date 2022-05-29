package com.primefit.tool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "diet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diet {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @SequenceGenerator(name = "diet_sequence", sequenceName = "diet_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false, columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "pdfUrl", nullable = false, columnDefinition = "TEXT")
    private String pdfUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_category", nullable = false, columnDefinition = "TEXT")
    private DietCategory dietCategory;
}
