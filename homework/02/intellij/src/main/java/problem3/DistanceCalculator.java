package problem3;

/**
 * Created by tom on 2/22/17.
 */
public final class DistanceCalculator {

    private static final DistanceCalculator DISTANCE = new DistanceCalculator();

    public double measureDistance(PointWritable point1, PointWritable point2) {
        double xResult = Double.parseDouble(point2.getFirst().toString()) - Double.parseDouble(point1.getFirst().toString());
        double yResult = Double.parseDouble(point2.getSecond().toString()) - Double.parseDouble(point1.getSecond().toString());
        double sum = (xResult * xResult) + (yResult * yResult);
        return Math.sqrt(sum);
    }

}