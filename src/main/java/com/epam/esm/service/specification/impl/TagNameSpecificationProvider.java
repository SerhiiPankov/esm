package com.epam.esm.service.specification.impl;

import com.epam.esm.service.specification.SpecificationProvider;
import java.util.StringJoiner;
import org.springframework.stereotype.Component;

@Component
public class TagNameSpecificationProvider
        implements SpecificationProvider {
    private static final String FILTER_KEY = "tagName";
    private static final String FIELD_NAME = "t.name";

    @Override
    public String getSpecification(String[] tagNames) {
        StringJoiner in = new StringJoiner(", ", FIELD_NAME + " IN (", ") ");
        for (String tagName: tagNames) {
            in.add("'" + tagName + "'");
        }
        return in.toString();
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
