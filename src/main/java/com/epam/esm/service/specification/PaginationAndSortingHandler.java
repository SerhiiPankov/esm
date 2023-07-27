package com.epam.esm.service.specification;

import com.epam.esm.exception.RequestException;
import java.util.Map;
import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaginationAndSortingHandler {
    private final String[] fields;
    private final int defaultCount;
    private final int defaultPage;

    @Autowired
    public PaginationAndSortingHandler() {
        fields = new String[]{"page", "count", "sortBy"};
        defaultCount = 6;
        defaultPage = 0;
    }

    public String handle(Map<String, String> params) {
        int page = params.containsKey(fields[0])
                ? (Integer.parseInt(params.get(fields[0])) - 1) :
                defaultPage;
        int count = params.containsKey(fields[1])
                ? Integer.parseInt(params.get(fields[1])) :
                defaultCount;
        if (page < 0) {
            throw new RequestException("Parameter page must be > 0");
        }
        return getSort(params) + " LIMIT " + page + ", " + count;
    }

    private String getSort(Map<String, String> params) {
        if (params.containsKey(fields[2])) {
            StringJoiner sort = new StringJoiner(", ", " ORDER BY ", " ");
            String sortBy = params.get(fields[2]);
            if (sortBy.contains(":")) {
                String[] sortingField = sortBy.split(";");
                for (String field : sortingField) {
                    if (field.contains(":")) {
                        String[] fieldAndDirection = field.split(":");
                        sort.add(fieldAndDirection[0] + " " + fieldAndDirection[1].toUpperCase());
                    } else {
                        sort.add(field + " ASC");
                    }
                }
            }
            return sort.toString();
        }
        return StringUtils.EMPTY;
    }

    public String[] getFields() {
        return fields;
    }
}
