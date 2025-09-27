package com.example.ritmofit.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PageResponse<T> {

    @SerializedName("_embedded")
    private Embedded<T> embedded;

    @SerializedName("page")
    private PageInfo pageInfo;

    public static class Embedded<T> {
        @SerializedName("courses")
        private List<T> content;

        public List<T> getContent() {
            return content;
        }
    }

    public static class PageInfo {
        private int size;
        private int totalElements;
        private int totalPages;
        private int number;

        // getters
    }

    public List<T> getContent() {
        return embedded != null ? embedded.getContent() : null;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }
}