package com.primefit.tool.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Role {

    @Id
    @Column(name = "id", updatable = false, columnDefinition = "INTEGER")
    @GeneratedValue(strategy = IDENTITY)
    @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", allocationSize = 1)
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    public Role(String name) {
        this.name = name;
    }
}