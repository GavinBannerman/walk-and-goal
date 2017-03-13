package site.gbdev.walkandgoal.models;

import android.util.Log;

/**
 * Created by gavin on 13/03/2017.
 */

public class Units {

    public enum Unit {
        STEPS ("Steps", 1.0),
        METRES ("Metres", 0.762),
        YARDS ("Yards", 0.833333),
        KM ("Km", 0.000762),
        MILES ("Miles", 0.000473);

        private final String name;
        private final double conversion;

        Unit(String name, double conversion){
            this.name = name;
            this.conversion = conversion;
        }

        public double getConversion() {return conversion;}
        public String getName() {return name;}
    }

    private static final Unit[] UNITS = {Unit.STEPS, Unit.METRES, Unit.YARDS, Unit.KM, Unit.MILES};
    public static Unit[] getUNITS() {return UNITS;}

    public static double convertFromSteps(double distance, Unit newUnit){
        return (distance * newUnit.getConversion());
    }

    public static double convertFromSteps(double distance, int unitId){
        return (distance * UNITS[unitId].getConversion());
    }

    public static double convertToSteps(double distance, Unit currentUnit){
        return (distance / currentUnit.getConversion());
    }

    public static double convertToSteps(double distance, int unitId){
        return (distance / UNITS[unitId].getConversion());
    }

    public static double convertToSteps(double distance, String unit){
        Unit unitEnum = Unit.STEPS;

        for (Unit possibleUnit : UNITS){
            if (possibleUnit.getName().equals(unit)){
                unitEnum = possibleUnit;
            }
        }

        return (distance / unitEnum.getConversion());
    }

    public static int getIdFromString(String unit){
        int index = -1;

        for (int i = 0; i < UNITS.length; i++){
            if (UNITS[i].getName().equals(unit)){
                index = i;
            }
        }

        return index;
    }
}
