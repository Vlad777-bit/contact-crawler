package com.github.vlad777bit.contactcrawler.api.dto;

import com.github.vlad777bit.contactcrawler.model.ContactInfo;
import com.github.vlad777bit.contactcrawler.model.ContactType;

import java.time.Instant;

public record ContactInfoResponse(
        Long id,
        ContactType type,
        String rawValue,
        String normalizedValue,
        String sourceUrl,
        String context,
        Instant createdAt
) {
    public static ContactInfoResponse from(ContactInfo c) {
        return new ContactInfoResponse(
                c.getId(),
                c.getType(),
                c.getRawValue(),
                c.getNormalizedValue(),
                c.getSourceUrl(),
                c.getContext(),
                c.getCreatedAt()
        );
    }
}
