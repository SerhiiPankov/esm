package com.epam.esm.model;

import com.epam.esm.annotation.DbColumn;
import com.epam.esm.annotation.DbId;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class GiftCertificate {
    @DbColumn
    @DbId
    private BigInteger id;
    @DbColumn
    private String name;
    @DbColumn
    private String description;
    @DbColumn
    private BigDecimal price;
    @DbColumn
    private Integer duration;
    @DbColumn
    private LocalDateTime createDate;
    @DbColumn
    private LocalDateTime lastUpdateDate;

    private List<Tag> tags;
}
