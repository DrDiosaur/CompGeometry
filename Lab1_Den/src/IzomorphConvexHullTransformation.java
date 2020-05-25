import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class IzomorphConvexHullTransformation extends JFrame
{
    private int margin = 50, width = 800, height = 600, radius = 10, flag = 0;
    private int n = 9, xO1, xO2, yO1, yO2, angleO1, angleO2;

    private ArrayList<String> results;
    private ArrayList<Point.Double> O1, O2;
    private ArrayList<Point> O1draw, O2draw;

    private void initConvexHull(int x0, int y0, int radius, int turn, ArrayList<Point.Double> convexHull){
        double angle = 2*Math.PI / n, x1 = x0 - radius, y1 = y0, x2, y2;
        double rotation = turn * (Math.PI / 180);

        ArrayList<Point.Double> prevHull = new ArrayList<>();
        prevHull.add(new Point.Double(x1, y1));
        for (int i = 0; i < n; i++) {
            x2 = (x1 - x0) * Math.cos(angle) - (y1 - y0) * Math.sin(angle) + x0;
            y2 = (x1 - x0) * Math.sin(angle) + (y1 - y0) * Math.cos(angle) + y0;
            x1 = x2;
            y1 = y2;
            prevHull.add(new Point.Double(x1, y1));
        }

        for (int i = 0; i < prevHull.size(); i++) {
            double newX = (prevHull.get(i).x - x0) * Math.cos(rotation) - (prevHull.get(i).y - y0) * Math.sin(rotation) + x0;
            double newY = (prevHull.get(i).x - x0) * Math.sin(rotation) + (prevHull.get(i).y - y0) * Math.cos(rotation) + y0;
            prevHull.get(i).setLocation(newX, newY);
        }

        convexHull.addAll(prevHull);
    }

    private ArrayList<Point> drawConvexHull(int x0, int y0, int radius, int turn,  Graphics graphics, Color color){
        graphics.setColor(color);

        ArrayList<Point> hullToDraw = new ArrayList<>();
        ArrayList<Point.Double> convexHull = new ArrayList<>();
        double angle = 2*Math.PI / n, x1 = x0 - radius, y1 = y0, x2, y2;
        double rotation = turn * (Math.PI / 180);

        convexHull.add(new Point.Double(x1, y1));
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0){
                x2 = (x1 - x0) * Math.cos(angle) - (y1 - y0) * Math.sin(angle) + x0 + i*3;
                y2 = (x1 - x0) * Math.sin(angle) + (y1 - y0) * Math.cos(angle) + y0;
            } else {
                x2 = (x1 - x0) * Math.cos(angle) - (y1 - y0) * Math.sin(angle) + x0;
                y2 = (x1 - x0) * Math.sin(angle) + (y1 - y0) * Math.cos(angle) + y0+ i*3;
            }
            x1 = x2;
            y1 = y2;
            convexHull.add(new Point.Double(x1, y1));
        }



        for (int i = 0; i < convexHull.size(); i++) {
            double newX = (convexHull.get(i).x - x0) * Math.cos(rotation) - (convexHull.get(i).y - y0) * Math.sin(rotation) + x0;
            double newY = (convexHull.get(i).x - x0) * Math.sin(rotation) + (convexHull.get(i).y - y0) * Math.cos(rotation) + y0;
            convexHull.get(i).setLocation(newX, newY);
        }

        convexHull = AlgorithmQuickHall.quickHullAlgoMain(convexHull);

        for (Point.Double aDouble : convexHull) {
            hullToDraw.add(new Point(convX(aDouble.x), convY(aDouble.y)));
        }

        for (int i = 0; i < hullToDraw.size(); i++) {
            if (i == hullToDraw.size()-1){
                graphics.drawLine(hullToDraw.get(i).x, hullToDraw.get(i).y, hullToDraw.get(0).x, hullToDraw.get(0).y);
            } else {
                graphics.drawLine(hullToDraw.get(i).x, hullToDraw.get(i).y, hullToDraw.get(i+1).x, hullToDraw.get(i+1).y);
            }
        }

        return  hullToDraw;
    }

    private void findConvexHullTransformation(){
        double[] x = new double[2];
        double[] y = new double[2];
        double[] trans = new double[2];

        double[][] matrix1 = new double[4][4];
        double[][] matrix2 = new double[4][4];
        double[][] divider = new double[3][3];

        for (int i = 0; i < divider.length; i++) {
            for (int j = 0; j < divider[i].length; j++) {
                switch (i){
                    case 0 :
                        divider[i][j] = O1.get(j).x;
                        break;
                    case 1 :
                        divider[i][j] = O1.get(j).y;
                        break;
                    case 2 :
                        divider[i][j] = 1;
                        break;
                }
            }
        }

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 1; j < matrix1[i].length; j++) {
                switch (i){
                    case 0 :
                        matrix1[i][j] = O2.get(j).x;
                        matrix2[i][j] = O2.get(j).y;
                        break;
                    case 1 :
                        matrix1[i][j] = O1.get(j).x;
                        matrix2[i][j] = O1.get(j).x;
                        break;
                    case 2 :
                        matrix1[i][j] = O1.get(j).y;
                        matrix2[i][j] = O1.get(j).y;
                        break;
                    case 3 :
                        matrix1[i][j] = 1;
                        matrix2[i][j] = 1;
                        break;
                }
            }
        }

        double dividerDet = divider[0][0] * divider[1][1] * divider[2][2] + divider[0][2] * divider[1][0] * divider[2][1] + divider[0][1] * divider[1][2] * divider[2][0] -
                divider[0][2] * divider[1][1] * divider[2][0] - divider[0][0] * divider[1][2] * divider[2][1] - divider[0][1] * divider[1][0] * divider[2][2];
        double sum = matrix1[0][1] * matrix1[2][2] * matrix1[3][3] + matrix1[0][3] * matrix1[2][1] * matrix1[3][2] + matrix1[0][2] * matrix1[2][3] * matrix1[3][1];
        double differ = matrix1[0][3] * matrix1[2][2] * matrix1[3][1] + matrix1[0][1] * matrix1[2][3] * matrix1[3][2] + matrix1[0][2] * matrix1[2][1] * matrix1[3][3];
        x[0] =  (sum - differ) / dividerDet;
        sum = matrix2[0][1] * matrix2[2][2] * matrix2[3][3] + matrix2[0][3] * matrix2[2][1] * matrix2[3][2] + matrix2[0][2] * matrix2[2][3] * matrix2[3][1];
        differ = matrix2[0][3] * matrix2[2][2] * matrix2[3][1] + matrix2[0][1] * matrix2[2][3] * matrix2[3][2] + matrix2[0][2] * matrix2[2][1] * matrix2[3][3];
        x[1] =  (sum - differ) / dividerDet;
        sum = matrix1[0][1] * matrix1[1][2] * matrix1[3][3] + matrix1[0][3] * matrix1[1][1] * matrix1[3][2] + matrix1[0][2] * matrix1[1][3] * matrix1[3][1];
        differ = matrix1[0][3] * matrix1[1][2] * matrix1[3][1] + matrix1[0][1] * matrix1[1][3] * matrix1[3][2] + matrix1[0][2] * matrix1[1][1] * matrix1[3][3];
        y[0] =  (sum - differ) / dividerDet;
        sum = matrix2[0][1] * matrix2[1][2] * matrix2[3][3] + matrix2[0][3] * matrix2[1][1] * matrix2[3][2] + matrix2[0][2] * matrix2[1][3] * matrix2[3][1];
        differ = matrix2[0][3] * matrix2[1][2] * matrix2[3][1] + matrix2[0][1] * matrix2[1][3] * matrix2[3][2] + matrix2[0][2] * matrix2[1][1] * matrix2[3][3];
        y[1] =  (sum - differ) / dividerDet;
        sum = matrix1[0][1] * matrix1[1][2] * matrix1[2][3] + matrix1[0][3] * matrix1[1][1] * matrix1[2][2] + matrix1[0][2] * matrix1[1][3] * matrix1[2][1];
        differ = matrix1[0][3] * matrix1[1][2] * matrix1[2][1] + matrix1[0][1] * matrix1[1][3] * matrix1[2][2] + matrix1[0][2] * matrix1[1][1] * matrix1[2][3];
        trans[0] =  (sum - differ) / dividerDet;
        sum = matrix2[0][1] * matrix2[1][2] * matrix2[2][3] + matrix2[0][3] * matrix2[1][1] * matrix2[2][2] + matrix2[0][2] * matrix2[1][3] * matrix2[2][1];
        differ = matrix2[0][3] * matrix2[1][2] * matrix2[2][1] + matrix2[0][1] * matrix2[1][3] * matrix2[2][2] + matrix2[0][2] * matrix2[1][1] * matrix2[2][3];
        trans[1] =  (sum - differ) / dividerDet;


        results  = new ArrayList<>();
        results.add("<html><p style='padding-left: 40px; font-size: 14px'>Ізоморфне перетворення " + n + "-вершинних опуклих "
                + "оболонок <font color='orange'>О1</font> в <font color='blue'>О2</font> має вигляд: </p></html>");
        results.add("");
        results.add("<html><p style='padding-left: 180px; font-size: 12px'>( " + Math.round(y[0] * 100) / 100.0 + "   "
                + Math.round(x[0] * 100) / 100.0 + " )( x ) + ( " + Math.round(trans[0] * 100) / 100.0 + " )</p></html>");
        results.add("<html><p style='padding-left: 180px; font-size: 12px'>( " + Math.round(y[1] * 100) / 100.0 + "   "
                + Math.round(x[1] * 100) / 100.0 + " )( y )  ( " + Math.round(trans[1] * 100) / 100.0 + " )</p></html>");
        results.add("");
        results.add("<html><p style='padding-left: 120px; font-size: 12px'>Кут повороту: " + Math.round(Math.acos(x[0]) * 180 / Math.PI) + "&deg</p></html>");
        results.add("");
        results.add("<html><p style='padding-left: 120px; font-size: 12px'>Трансляція на: ( " + Math.round(trans[0] * 100) / 100.0
                + ", " + Math.round(trans[1] * 100) / 100.0 + " )</p></html>");
    }

    private int convX(double x){
        double x_log = x / 100;
        return (int)(margin + (1.0 / 2) * (x_log + 1) * (width - 2 * margin));
    }

    private int convY(double y){
        double y_log = y / 100;
        return (int) (margin + (-1.0 / 2) * (y_log - 1) * (height - 2 * margin));
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawLine(convX(0), convY(-100), convX(0), convY(100));
        g.drawLine(convX(-100), convY(0), convX(100), convY(0));
        O1draw = drawConvexHull(xO1, yO1, radius, angleO1, g, Color.ORANGE);
        O2draw = drawConvexHull(xO2, yO2, radius, angleO2,  g, Color.BLUE);
        findLinearInterpolation(g);
        if (flag == 0){
            repaint();
            flag++;
        }

    }

    private void findLinearInterpolation(Graphics g){
        g.setColor(Color.BLACK);
        for (int i = 0; i < O1draw.size(); i++) {
            System.out.println(O1draw.get(i).x + " - " + O2draw.get(i).y);
            g.drawLine(O1draw.get(i).x, O1draw.get(i).y, O2draw.get(i).x, O2draw.get(i).y);
        }
    }

    public IzomorphConvexHullTransformation(int n, int xO1, int yO1, int angleO1, int xO2, int yO2, int angleO2)
    {
        super("Ізоморфне перетворення опуклих оболонок");

        this.n = n;
        this.xO1 = xO1;
        this.xO2 = xO2;
        this.yO1 = yO1;
        this.yO2 = yO2;
        this.angleO1 = angleO1;
        this.angleO2 = angleO2;

        O1 = new ArrayList<>();
        O2 = new ArrayList<>();
        initConvexHull(xO1, yO1, radius, angleO1, O1);
        initConvexHull(xO2, yO2, radius, angleO2, O2);
        findConvexHullTransformation();

        setLayout(new GridLayout(30, 2));
        for (int i = 0; i < 21; i++) {
            add(Box.createHorizontalBox());
        }

        for (String result : results) {
            JLabel label = new JLabel(result);
            add(label);
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        pack();
        setSize(width, height + 200);
        setLocation(350, 0);
        setResizable(false);
        setVisible(true);
    }
}