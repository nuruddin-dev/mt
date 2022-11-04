package com.graphicless.modeltest.model;

import java.util.List;

public class OuterDataModel {

    private final String outerData;
    private final List<MiddleDataModel> middleDataModels;
    private boolean isExpandable;

    public OuterDataModel(String outerData, List<MiddleDataModel> middleDataModels) {
        this.outerData = outerData;
        this.middleDataModels = middleDataModels;
        this.isExpandable = true;
    }

    public String getOuterData() {
        return outerData;
    }

    public List<MiddleDataModel> getMiddleDataModels() {
        return middleDataModels;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
