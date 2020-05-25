import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * нехай пряма має напрям зліва направо (від першої точки до другої, як вектор)
 * */
public class AlgorithmQuickHall {
    private static final double EPS = 0.000000001;
    private static final double EPS_OFFSET = 0.00000001;

    public static ArrayList<Point.Double> quickHullAlgoMain(ArrayList<Point.Double> points){
        int veryLeftPointIndex = 0;
        for (int i = 0; i < points.size(); i++){
            if (points.get(i).getX() < points.get(veryLeftPointIndex).getX()){
                veryLeftPointIndex = i;
            }
        }

        Point.Double l0 = points.get(veryLeftPointIndex);
        Point.Double r0 = new Point.Double(l0.getX(), (l0.getY() - EPS_OFFSET));

        ArrayList<Point.Double> result = quickHall(l0, r0, points);

        //видалення точки r0
        result.remove(result.size() - 1);

        return result;
    }

    private static ArrayList<Point.Double> quickHall(Point.Double l, Point.Double r, ArrayList<Point.Double> points){
        if (points.size() <= 2){ // points має лише точки l і r
            return points;
        }

        int hIndex = mostDistantPointFromLineIndex(l, r, points);
        ArrayList<Point.Double> s1 = new ArrayList<>(),
                            s2 = new ArrayList<>();
        s1.add(l);
        s1.add(points.get(hIndex));
        s2.add(points.get(hIndex));
        s2.add(r);
        for (Point.Double point : points) {
            if (liesRelativeToLine(l, points.get(hIndex), point) == PointRelativeToLine.LEFT) {
                s1.add(point);
            } else if (liesRelativeToLine(points.get(hIndex), r, point) == PointRelativeToLine.LEFT) {
                s2.add(point);
            }
        }

        ArrayList<Point.Double> result1 =  quickHall(l, points.get(hIndex), s1);
        ArrayList<Point.Double> result2 =  quickHall(points.get(hIndex), r, s2);

        result1.remove(result1.size() - 1);

        result1.addAll(result2);

        return result1;
    }

    /**
     * Нехай пряма визначається вектором, спрямованим від точок lineBegin до точок lineEnd.
     * Тому сторони прямої визначені.
     * */
    private static PointRelativeToLine liesRelativeToLine(Point.Double lineBegin, Point.Double lineEnd, Point.Double p){
        double expr = (lineEnd.getX() - lineBegin.getX()) * (p.getY() - lineBegin.getY()) -
                (lineEnd.getY() - lineBegin.getY()) * (p.getX() - lineBegin.getX());
        if (expr > 0.0){
            return PointRelativeToLine.LEFT;
        }
        else if (expr < 0.0){
            return PointRelativeToLine.RIGHT;
        }
        else {
            return PointRelativeToLine.LIES_ON_lINE;
        }
    }

    /**
     * Обчислює площу трикутника за даними 3 точками.
     * Використана формула Герона.
     * */
    private static double calcualteTriangleSquare(Point.Double a, Point.Double b, Point.Double c){
        double ab = Math.pow((Math.pow(b.getX() - a.getX(), 2.0) + Math.pow(b.getY() - a.getY(), 2.0)), 0.5);
        double bc = Math.pow((Math.pow(c.getX() - b.getX(), 2.0) + Math.pow(c.getY() - b.getY(), 2.0)), 0.5);
        double ca = Math.pow((Math.pow(a.getX() - c.getX(), 2.0) + Math.pow(a.getY() - c.getY(), 2.0)), 0.5);
        double p = (ab + bc + ca) / 2;

        return Math.pow(p*(p-ab)*(p-bc)*(p-ca), 0.5);
    }

    /**
     * Пряма задається точками a і b.
     * PointSet визначає набір, з якого повертається індекс знайденої точки.
     * Функція обчислює площу всіх трикутників, що створені на базі 3 точок:
     * a, b і точка взята із набору.
     * Трикутник з найбільшою площою має найбільшу висоту,
     * тож відстань від прямої у третьої точки - максимальна.
     * Якшо 2 трикутника мають однакові площі, обирається трикутник з найбільшим baPoint кутом
     * */
    private static int mostDistantPointFromLineIndex(Point.Double a, Point.Double b, ArrayList<Point.Double> pointsSet){
        double prevTriangleSquare = 0.0;
        int bestChoicePointIndex = 0;
        double currentTriangleSquare = 0.0;

        for (int i=0; i<pointsSet.size(); i++){
            currentTriangleSquare = calcualteTriangleSquare(a, b, pointsSet.get(i));
            if (Math.abs(currentTriangleSquare - prevTriangleSquare) < EPS){ //equal
                if (calcAngleBetweenTwoVectorsWithTheSameOrigin(a, b, pointsSet.get(i)) >
                        calcAngleBetweenTwoVectorsWithTheSameOrigin(a, b, pointsSet.get(bestChoicePointIndex))){
                    bestChoicePointIndex = i;
                }
            }
            else if (currentTriangleSquare > prevTriangleSquare){
                bestChoicePointIndex = i;
                prevTriangleSquare = currentTriangleSquare;
            }
        }

        return bestChoicePointIndex;
    }

    /**
     * Як точка лежить відносно прямої
     * */
    enum PointRelativeToLine{
        LEFT, RIGHT, LIES_ON_lINE
    }

    /**
     * обчислює кут між векторами ab1 і ab2
     * кут має значення в діапазоні між 0 і 180 градусами
     * значення кута повертається в радіанах (між 0 і Pi)
     * */
    private static double calcAngleBetweenTwoVectorsWithTheSameOrigin(Point.Double a, Point.Double b1, Point.Double b2){
        double vectModab1 = Math.pow(Math.pow(b1.getX() - a.getX(), 2) + Math.pow(b1.getY() - a.getY(), 2), 0.5);
        double vectModab2 = Math.pow(Math.pow(b2.getX() - a.getX(), 2) + Math.pow(b2.getY() - a.getY(), 2), 0.5);

        return Math.acos(((b1.getX() - a.getX())*(b2.getX() - a.getX()) + (b1.getY() - a.getY())*(b2.getY() - a.getY()))
                / (vectModab1 * vectModab2));
    }
}