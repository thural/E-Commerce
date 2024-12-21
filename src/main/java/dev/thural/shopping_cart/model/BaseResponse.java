package dev.thural.shopping_cart.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseResponse {

    private Long id;
    private Integer version;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}