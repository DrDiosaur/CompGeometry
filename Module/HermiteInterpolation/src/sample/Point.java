package sample;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Comparator;

public class Point extends Circle {
    private static final double DEFAULT_RADIUS = 3;
    private static final Paint defaultFill = Color.RED;
    private static int windowHeight = 600;

    private double x;
    private double y;

    public Point(double x, double y) {
        super(x, windowHeight - y, DEFAULT_RADIUS, defaultFill);
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, double radius, Paint fill) {
        super(x, windowHeight - y, radius, fill);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static void setWindowHeight(int windowHeight) {
        Point.windowHeight = windowHeight;
    }

    @Override
    public String toString() {
        return "x = " + x + "; y = " + y;
    }

    public static class XComparator implements Comparator<Point> {

        @Override
        public int compare(Point first, Point second) {
            return Double.compare(first.getX(), second.getX());
        }
    }
}
