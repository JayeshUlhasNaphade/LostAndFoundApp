package com.example.lostandfounditems;

public class Datamodel {

    private String thing,location,uri,description,date;

    private Datamodel(){

    }


    private Datamodel(String thing, String location, String uri, String description, String date){
        this.thing=thing;
        this.location=location;
        this.uri=uri;
        this.description = description;
        this.date=date;
    }

    public String getThing() {
        return thing;
    }

    public void setThing(String thing) {
        this.thing = thing;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
