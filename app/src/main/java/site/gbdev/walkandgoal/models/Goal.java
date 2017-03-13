package site.gbdev.walkandgoal.models;

import java.text.DecimalFormat;
import java.util.Date;

import site.gbdev.walkandgoal.util.Units;

/**
 * Created by gavin on 07/02/2017.
 */

public class Goal {

    private String name;
    private double distance;
    private int unit;
    private boolean active;
    private Date date;

    public Goal(String name, double distance, int unit, Date date, boolean active) {
        this.active = active;
        this.distance = distance;
        this.name = name;
        this.unit = unit;
        this.date = date;
    }

    public Goal(String name, double distance, int unit, Date date) {
        this.active = false;
        this.distance = distance;
        this.name = name;
        this.unit = unit;
        this.date = date;
    }

    public Goal(Goal otherGoal) {
        this.active = otherGoal.isActive();
        this.distance = otherGoal.getDistance();
        this.name = otherGoal.getName();
        this.unit = otherGoal.getUnit();
        this.date = otherGoal.getDate();
    }

    public boolean isActive() {return active;}
    public void setActive(boolean active) {this.active = active;}

    public double getDistance() {return distance;}
    public void setDistance(double distance) {this.distance = distance;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getUnit() {return unit;}
    public void setUnit(int unit) {this.unit = unit;}

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}

    public String getDisplayDistance(){
        DecimalFormat format = new DecimalFormat("0.#");
        return (format.format(Units.convertFromSteps(distance, unit)) + " " + Units.getUNITS()[unit].getName());
    }
}
