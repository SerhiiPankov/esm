package com.epam.esm.model;

import com.epam.esm.annotation.DbColumn;
import com.epam.esm.annotation.DbId;
import lombok.Data;

import java.math.BigInteger;

@Data
public class Tag {
    @DbColumn
    @DbId
    private BigInteger id;
    @DbColumn
    private String name;
}
