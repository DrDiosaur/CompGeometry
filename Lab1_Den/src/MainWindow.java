import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(){
        super("Ізоморфне перетворення опуклих оболонок");
        JFrame.setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        initComponents();
        setSize(600, 350);
        setLocation(450, 100);
        setResizable(false);
        setVisible(true);
    }

    public void initComponents(){
        setLayout(new GridLayout(9, 1));

        JLabel titleRow = new JLabel("<html><p style='padding-left: 150px; font-size: 16px'>Опуклі оболонки</p></html>");

        JLabel nRow = new JLabel();
        nRow.setLayout(new GridLayout(1, 4));
        JLabel n = new JLabel("<html><p style='padding-left: 6px; font-size: 12px'>Кількість кутів n: </p></html>");
        JTextField nInput = new JTextField();
        nRow.add(Box.createVerticalBox());
        nRow.add(n);
        nRow.add(nInput);
        nRow.add(Box.createVerticalBox());

        JLabel ttlsRow = new JLabel();
        ttlsRow.setLayout(new GridLayout(1, 7));
        JLabel label1 = new JLabel("<html><p style='padding-left: 20px; font-size: 13px' color='orange'>О1</p></html>");
        JLabel label2 = new JLabel("<html><p style='padding-left: 20px; font-size: 13px' color='blue'>О2</p></html>");
        ttlsRow.add(Box.createVerticalBox());
        ttlsRow.add(label1);
        ttlsRow.add(Box.createVerticalBox());
        ttlsRow.add(Box.createVerticalBox());
        ttlsRow.add(Box.createVerticalBox());
        ttlsRow.add(label2);
        ttlsRow.add(Box.createVerticalBox());

        JLabel xRow = new JLabel();
        xRow.setLayout(new GridLayout(1, 5));
        JLabel o1x = new JLabel("<html><p style='padding-left: 3px'>x кола навколо О1: </p></html>");
        JTextField o1xInput = new JTextField();
        JLabel o2x = new JLabel("x кола навколо О2: ");
        JTextField o2xInput = new JTextField();
        xRow.add(o1x);
        xRow.add(o1xInput);
        xRow.add(Box.createVerticalBox());
        xRow.add(o2x);
        xRow.add(o2xInput);

        JLabel yRow = new JLabel();
        yRow.setLayout(new GridLayout(1, 5));
        JLabel o1y = new JLabel("<html><p style='padding-left: 3px'>y кола навколо О1: </p></html>");
        JTextField o1yInput = new JTextField();
        JLabel o2y = new JLabel("y кола навколо О2: ");
        JTextField o2yInput = new JTextField();
        yRow.add(o1y);
        yRow.add(o1yInput);
        yRow.add(Box.createVerticalBox());
        yRow.add(o2y);
        yRow.add(o2yInput);

        JLabel angleRow = new JLabel();
        angleRow.setLayout(new GridLayout(1, 5));
        JLabel o1Angle = new JLabel("<html><p style='padding-left: 8px'>Кут повороту О1: </p></html>");
        JTextField o1AngleInput = new JTextField();
        JLabel o2Angle = new JLabel("<html><p style='padding-left: 8px'>Кут повороту О2: </p></html>");
        JTextField o2AngleInput = new JTextField();
        angleRow.add(o1Angle);
        angleRow.add(o1AngleInput);
        angleRow.add(Box.createVerticalBox());
        angleRow.add(o2Angle);
        angleRow.add(o2AngleInput);

        JLabel btnRow = new JLabel();
        btnRow.setLayout(new GridLayout(1, 3));
        JButton btn = new JButton("Знайти перетворення");
        btn.addActionListener(e -> {
            if (!nInput.getText().equals("") && !o1xInput.getText().equals("") && !o1yInput.getText().equals("") && !o2xInput.getText().equals("")
                    && !o2yInput.getText().equals("") && !o1AngleInput.getText().equals("") && !o2AngleInput.getText().equals("")){
                int nTrans = Integer.parseInt(nInput.getText());
                int xO1 = Integer.parseInt(o1xInput.getText());
                int yO1 = Integer.parseInt(o1yInput.getText());
                int xO2 = Integer.parseInt(o2xInput.getText());
                int yO2 = Integer.parseInt(o2yInput.getText());
                int angleO1 = Integer.parseInt(o1AngleInput.getText());
                int angleO2 = Integer.parseInt(o2AngleInput.getText());
                if (nTrans < 3){
                    JOptionPane.showMessageDialog(null, "<html><p style='font-size: 17px'>n повинне бути не менше 3</p></html>");
                } else {
                    new IzomorphConvexHullTransformation(nTrans, xO1, yO1, angleO1, xO2, yO2, angleO2);
                }
            } else {
                JOptionPane.showMessageDialog(null, "<html><p style='font-size: 17px'>Заповніть всі поля</p></html>");
            }

        });
        btnRow.add(Box.createVerticalBox());
        btnRow.add(btn);
        btnRow.add(Box.createVerticalBox());

        add(titleRow);
        add(nRow);
        add(ttlsRow);
        add(xRow);
        add(yRow);
        add(angleRow);
        add(Box.createHorizontalBox());
        add(btnRow);
        add(Box.createHorizontalBox());
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
