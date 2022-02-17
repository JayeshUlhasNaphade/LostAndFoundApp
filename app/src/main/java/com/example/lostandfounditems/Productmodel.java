package com.example.lostandfounditems;

public class Productmodel {

    private String thing,location,uri,usrid,description,date;

    private Productmodel(){

    }

    Productmodel(String thing, String location, String uri, String userid, String description,String date){
        this.thing=thing;
        this.location=location;
        this.uri=uri;
        this.usrid=userid;
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

    public void setuserid(String user) {
        this.usrid= user;
    }
    public String getuserid() {
        return usrid;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String geturi() {
        return uri;
    }

    public void seturi(String uri) {
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
class  Users{
//    int mob;
    String name,uri,mob,roll;

    public Users(String name, String uri, String mob, String roll) {
        this.name = name;
        this.uri = uri;
        this.mob = mob;
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getUri() {
        return uri;
    }

    public String getMob() {
        return mob;
    }

    public String getRoll() {
        return roll;
    }
}
