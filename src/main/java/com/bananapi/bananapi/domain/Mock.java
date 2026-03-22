package com.bananapi.bananapi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "api_mocks", indexes = @Index(name = "url", columnList = "url", unique = true))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Mock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String url;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "last_usage", nullable = false)
    private LocalDateTime lastUsageDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_data", columnDefinition = "jsonb")
    private Map<String, Object> jsonData;
}
