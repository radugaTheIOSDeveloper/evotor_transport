package com.example.transport;

public class ItemCost {

    String  stop_point_to_name;
    String price;
    String stop_point_to_id;
    String privilege_price;
    String jsonString;

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    String cost;

    public String getCost(String jsonString) {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStop_point_to_name() {
        return stop_point_to_name;
    }

    public void setStop_point_to_name(String stop_point_to_name) {
        this.stop_point_to_name = stop_point_to_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStop_point_to_id() {
        return stop_point_to_id;
    }

    public void setStop_point_to_id(String stop_point_to_id) {
        this.stop_point_to_id = stop_point_to_id;
    }

    public String getPrivilege_price() {
        return privilege_price;
    }

    public void setPrivilege_price(String privilege_price) {
        this.privilege_price = privilege_price;
    }
}
