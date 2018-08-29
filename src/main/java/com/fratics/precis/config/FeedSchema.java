package com.fratics.precis.config;

import java.util.ArrayList;
import java.util.List;

public class FeedSchema {

    private String version;
    private String schemaName;
    private List<FeedSchemaElement> list = new ArrayList<FeedSchemaElement>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public List<FeedSchemaElement> getList() {
        return list;
    }

    public void setList(List<FeedSchemaElement> list) {
        this.list = list;
    }
}
