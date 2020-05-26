package sample;

import exceptions.XOutOfBoundsException;

import java.util.List;

import static java.lang.Math.pow;

public class Interpolator {
    private List<Point> points;

    public Interpolator(List<Point> points) {
        points.sort(new Point.XComparator());
        this.points = points;
    }

    public double f(double x) throws XOutOfBoundsException {
        checkBoundaries(x);

        Point left = null;
        Point right = null;
        for (Point p : points) {
            left = right;
            right = p;
            if (p.getX() > x) {
                break;
            }
        }

        return cubicHermiteSpline(x, left, right);
    }

    private void checkBoundaries(double x) throws XOutOfBoundsException {
        double lowerBound = points.get(0).getX();
        double upperBound = points.get(points.size() - 1).getX();
        String message = "Can not interpolate function beyond the bounds (x = " + x +
                "; bounds - [" + lowerBound + ", " + upperBound + "])";

        if (x < lowerBound || x > upperBound) {
            throw new XOutOfBoundsException(message);
        }
    }

    private double cubicHermiteSpline(double x, Point left, Point right) {
        double t = affineTransformation(x, left, right);
        double m1 = tan(left);
        double m2 = tan(right);
        double p1 = left.getY();
        double p2 = right.getY();
        double dX = right.getX() - x;

        double h00 = 2 * pow(t, 3) - 3 * pow(t, 2) + 1;
        double h10 = pow(t, 3) - 2 * pow(t, 2) + t;
        double h01 = -2 * pow(t, 3) + 3 * pow(t, 2);
        double h11 = pow(t, 3) - pow(t, 2);

        return h00 * p1 + h10 * m1 + h01 * p2 + h11 * m2;
    }

    private double affineTransformation(double x, Point left, Point right) {
        return (x - left.getX()) / (right.getX() - left.getX());
    }

    private double tan(Point p) {
        Point left = nextPoint(p);
        Point right = previousPoint(p);

        double x1 = 0;
        double y1 = 0;
        double x2 = p.getX();
        double y2 = p.getY();
        double x3 = 0;
        double y3 = 0;

        if (left != null) {
            x1 = left.getX();
            y1 = left.getY();
        }

        if (right != null) {
            x3 = right.getX();
            y3 = right.getY();
        }

        // use one-sided finite difference formula at the endpoints
        if (left == null) {
            return finiteDifference(x2, y2, x3, y3);
        }
        if (right == null) {
            return finiteDifference(x1, y1, x2, y2);
        }

        // use three-point finite difference formula otherwise
        return finiteDifference(x2, y2, x3, y3) + finiteDifference(x1, y1, x2, y2);
    }

    private double finiteDifference(double x1, double y1, double x2, double y2) {
        return ((y2 - y1) / (x2 - x1)) / 2;
    }

    private Point nextPoint(Point p) {
        if (p.equals(points.get(points.size() - 1))) {
            return null;
        }

        return points.get(points.indexOf(p) + 1);
    }

    private Point previousPoint(Point p) {
        if (p.equals(points.get(0))) {
            return null;
        }

        return points.get(points.indexOf(p) - 1);
    }
}
