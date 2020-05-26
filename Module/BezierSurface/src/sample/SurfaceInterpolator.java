package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SurfaceInterpolator {
    private static double step = 0.005;

    public static List<Point> buildBezierSurface(List<List<Point>> points) {
        int n = points.size() - 1;
        int m = points.get(0).size() - 1;
        List<Point> result = new ArrayList<>();

        for (double u = 0; u <= 1; u += step) {
            for (double v = 0; v <= 1; v += step) {
                double x = 0;
                double y = 0;
                double z = 0;

                for (int i = 0; i < n + 1; i++) {
                    for (int j = 0; j < m + 1; j++) {
                        x += bernsteinPolynomial(i, n, u) * bernsteinPolynomial(j, m, v) * points.get(i).get(j).getX();
                        y += bernsteinPolynomial(i, n, u) * bernsteinPolynomial(j, m, v) * points.get(i).get(j).getY();
                        z += bernsteinPolynomial(i, n, u) * bernsteinPolynomial(j, m, v) * points.get(i).get(j).getZ();
                    }
                }

                result.add(new Point(x, y, z, Color.SILVER));
            }
        }

        return result;
    }

    private static double bernsteinPolynomial(int i, int n, double t) {
        return binomialCoefficient(i, n) * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }

    private static double binomialCoefficient(int i, int n) {
        double result = 1;
        int max = Math.max(i, n - i);
        int min = Math.min(i, n - i);

        for (int j = max + 1; j < n + 1; j++) {
            result *= j;
        }

        for (int j = 1; j < min + 1; j++) {
            result /= j;
        }

        return result;
    }
}
