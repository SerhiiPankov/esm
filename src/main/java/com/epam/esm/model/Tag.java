package com.epam.esm.model;

import com.epam.esm.annotation.DbColumn;
import com.epam.esm.annotation.DbId;
import java.math.BigInteger;
import lombok.Data;

@Data
public class Tag {
    @DbColumn
    @DbId
    private BigInteger id;
    @DbColumn
    private String name;
}
