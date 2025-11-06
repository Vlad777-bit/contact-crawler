package com.github.vlad777bit.contactcrawler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "task")
@Entity
@Table(
        name = "contact_info",
        indexes = {
                @Index(name = "idx_contact_type", columnList = "type"),
                @Index(name = "idx_contact_value", columnList = "normalizedValue"),
                @Index(name = "idx_contact_task", columnList = "task_id")
        }
)
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ContactType type;

    /** Исходное значение, как найдено на странице */
    @Column(nullable = false, length = 1024)
    private String rawValue;

    /** Нормализованное значение (для поиска/уникальности) */
    @Column(nullable = false, length = 512)
    private String normalizedValue;

    /** URL страницы, где найден контакт */
    @Column(nullable = false, length = 1024)
    private String sourceUrl;

    /** Якорный текст/контекст (опционально) */
    @Column(length = 1024)
    private String context;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private CrawlingTask task;
}
