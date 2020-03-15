package com.example.transport;

public class Item {

    Integer type;
    String name;
  //  Integer sum;
    String id;
    String hexColor;
    Integer nds;
    String jsonString;
    Integer responceCode;

    Float sum;

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Float getSum() {
        return sum;
    }

    public Integer getResponceCode() {
        return responceCode;
    }

    public void setResponceCode(Integer responceCode) {
        this.responceCode = responceCode;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public Integer getNds() {
        return nds;
    }

    public void setNds(Integer nds) {
        this.nds = nds;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



//    public Integer getSum() {
//        return sum;
//    }
//
//    public void setSum(Integer sum) {
//        this.sum = sum;
//    }
}
