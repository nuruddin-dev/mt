package com.graphicless.modeltest.model;

import java.util.List;

public class MiddleDataModel {

   private final String middleData;
   private final List<InnerDataModel> innerDataModels;
   private boolean isExpandable;

   public MiddleDataModel(String outerData, List<InnerDataModel> innerDataModel) {
      this.middleData = outerData;
      this.innerDataModels = innerDataModel;
      this.isExpandable = true;
   }

   public String getMiddleData() {
      return middleData;
   }

   public List<InnerDataModel> getInnerDataModels() {
      return innerDataModels;
   }

   public boolean isExpandable() {
      return isExpandable;
   }

   public void setExpandable(boolean expandable) {
      isExpandable = expandable;
   }
}
