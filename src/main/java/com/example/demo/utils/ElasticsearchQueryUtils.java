package com.example.demo.utils;

import java.util.List;
import java.util.stream.Collectors;

public class ElasticsearchQueryUtils {

    private static final String TERM_QUERY_EXPR_FORMAT = "{ \"term\": { \"%s\": \"%s\" }}";

    public static String getTagsQueryExpr(List<String> tags) {
        return tags.stream().map(s -> String.format(TERM_QUERY_EXPR_FORMAT, "tags", s)).collect(Collectors.joining(","));
    }
}
