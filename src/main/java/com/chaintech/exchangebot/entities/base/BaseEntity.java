package com.chaintech.exchangebot.entities.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@Column(name = "created_date", nullable = false)
	@CreatedDate
	private Instant createdDate;

	@Column(name = "last_modified_date", nullable = false)
	@LastModifiedDate
	private Instant lastModifiedDate;

}
