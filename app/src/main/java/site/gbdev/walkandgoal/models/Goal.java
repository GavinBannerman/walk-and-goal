package site.gbdev.walkandgoal.models;

/**
 * Created by gavin on 07/02/2017.
 */

public class Goal {

    private String name;
    private int distance;
    private String unit;
    private boolean active;

    public Goal(String name, int distance, String unit, boolean active) {
        this.active = active;
        this.distance = distance;
        this.name = name;
        this.unit = unit;
    }

    public Goal(String name, int distance, String unit) {
        this.active = false;
        this.distance = distance;
        this.name = name;
        this.unit = unit;
    }

    public Goal(Goal otherGoal) {
        this.active = otherGoal.isActive();
        this.distance = otherGoal.getDistance();
        this.name = otherGoal.getName();
        this.unit = otherGoal.getUnit();
    }

    public boolean isActive() {return active;}
    public void setActive(boolean active) {this.active = active;}

    public int getDistance() {return distance;}
    public void setDistance(int distance) {this.distance = distance;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getUnit() {return unit;}
    public void setUnit(String unit) {this.unit = unit;}
}
