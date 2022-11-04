package com.graphicless.modeltest.model;

public class ModelTestListModel {

   private final String name, address;

   public ModelTestListModel(String name, String address) {
      this.name = name;
      this.address = address;
   }

   public String getName() {
      return name;
   }

   public String getAddress() {
      return address;
   }
}
