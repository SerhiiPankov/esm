package com.epam.esm.service.specification;

public interface SpecificationProvider {
    String getSpecification(String[] params);

    String getFilterKey();
}
