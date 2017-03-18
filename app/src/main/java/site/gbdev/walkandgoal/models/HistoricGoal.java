package site.gbdev.walkandgoal.models;

/**
 * Created by gavin on 18/03/2017.
 */

public class HistoricGoal {

    private Goal goal;
    private double progress;

    public HistoricGoal(Goal goal, double progress) {
        this.goal = goal;
        this.progress = progress;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getPercentageCompleted(){
        return (int) (progress/goal.getUnitDistance()*100);
    }
}
