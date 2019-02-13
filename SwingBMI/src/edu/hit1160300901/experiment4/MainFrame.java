package edu.hit1160300901.experiment4;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static edu.hit1160300901.experiment4.SwingBMI.saveFile;

public class MainFrame extends JFrame {
    private DataChartPanel Chart;
    private AddPanel frame1;
    private DelPanel frame2;
    private ModPanel frame3;
    private JMenu List;
    private JMenu BMIStatistics;
    private JTextField text_1;
    private JTextField text_2;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    ResultPanel resultPanel;
    SwingBMI swingBMI;
    int index;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Font font = new Font("Serif", Font.PLAIN, 15);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        setSize(405, 450);
        setResizable(false);

        setTitle("BMI统计分析系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu mnNewMenu = new JMenu("数据管理");
        menuBar.add(mnNewMenu);

        swingBMI = new SwingBMI();
        swingBMI.students.addAll(SwingBMI.readFile("test.txt"));

        Chart = new DataChartPanel(this, ChartFactory.createXYBarChart("BMI Statics",
                "Intervals", false, "Number of Students",
                DataChartPanel.createDataset(String.format("BMI-Mode:%3.2f Middle:%3.2f Average:%3.2f Variance:%3.2f",
                        swingBMI.bmiMode(SwingBMI.Student::getBmis),
                        swingBMI.bmiMedian(SwingBMI.Student::getBmis),
                        swingBMI.bmiAverage(SwingBMI.Student::getBmis),
                        swingBMI.bmiVariance(SwingBMI.Student::getBmis)),
                        DataChartPanel.bmiIntervals, swingBMI.students, SwingBMI.Student::getBmis, 0.9f), PlotOrientation.VERTICAL,
                true, false, false));


        frame1 = new AddPanel(this);
        frame2 = new DelPanel(this);
        frame3 = new ModPanel(this);

        JMenuItem menuItem = new JMenuItem("信息输入");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setContentPane(frame1);
                setVisible(true);
            }
        });
        mnNewMenu.add(menuItem);

        JMenuItem menuItem_1 = new JMenuItem("信息修改");
        menuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setContentPane(frame3);
                setVisible(true);
            }
        });
        mnNewMenu.add(menuItem_1);

        JMenuItem menuItem_2 = new JMenuItem("信息删除");
        menuItem_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setContentPane(frame2);
                setVisible(true);
            }
        });
        mnNewMenu.add(menuItem_2);

        resultPanel = new ResultPanel(this);
        addSortComboBox(resultPanel, 5, 260, 380, 30);

        List = new JMenu("数据列表");
        menuBar.add(List);
        List.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                setContentPane(resultPanel);
                setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        BMIStatistics = new JMenu("数据统计");
        menuBar.add(BMIStatistics);
        BMIStatistics.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                setContentPane(Chart);
                setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        setContentPane(resultPanel);
        setVisible(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    class ResultPanel extends JPanel {
        MainFrame frame;
        JTextArea ta;

        void toupdate() {
            StringBuffer sb = new StringBuffer();
            for (SwingBMI.Student st : frame.swingBMI.students) {
                sb.append(st.toString()).append("\n");
            }
            ta.setText(sb.toString());
            saveFile(frame.swingBMI.students,"test.txt");
        }

        public ResultPanel(MainFrame frame) {
            this.frame = frame;
            ta = new JTextArea();
            setLayout(null);
            ta.setEditable(false);
            setBackground(Color.WHITE);
            toupdate();
            JScrollPane sp = new JScrollPane(ta);
            sp.setBounds(5, 5, 380, 250);
            //sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            this.add(sp);
            saveFile(frame.swingBMI.students, "test.txt");
            Search();
        }

        public void Search() {

            JButton SearchButton = new JButton("查找");
            SearchButton.setBounds(50, 360, 90, 20);

            text_1 = new JTextField("", 10);
            text_1.setBounds(50, 300, 90, 20);
            text_2 = new JTextField("", 10);
            text_2.setBounds(50, 330, 90, 20);

            JLabel lbId = new JLabel("学号");
            lbId.setBounds(10, 300, 30, 18);
            JLabel lbName = new JLabel("姓名");
            lbName.setBounds(10, 330, 30, 18);

            add(SearchButton);
            add(text_1);
            add(text_2);
            add(lbId);
            add(lbName);
            SwingBMI.Student st = new SwingBMI.Student();
            SearchButton.addActionListener((ActionEvent e) -> {
                st.setIds(text_1.getText());
                st.setNames(text_2.getText());
                for (int i = 0; i < swingBMI.students.size(); i++) {
                    if (text_1.getText().equals(swingBMI.students.get(i).getIds()) || text_2.getText().equals(swingBMI.students.get(i).getNames())) {
                        JOptionPane.showMessageDialog(null, swingBMI.students.get(i).toString(), "Prompt", JOptionPane.INFORMATION_MESSAGE);
                        frame.index = i;
                        frame3.toUpdate();
                        frame2.toUpdate();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "用户不存在", "Alert", JOptionPane.ERROR_MESSAGE);
            });

        }

    }

    private JComboBox addSortComboBox(ResultPanel panel, int x, int y, int width, int height) {
        JComboBox box = new JComboBox();
        box.addItem("按学号排序");
        box.addItem("按姓名排序");
        box.addItem("按身高排序");
        box.addItem("按体重排序");
        box.addItem("按BMI排序");
        box.addItemListener((ItemEvent e) -> {
            switch ((String) e.getItem()) {
                case "按学号排序":
                    swingBMI.sortStudents(new SwingBMI.IdComparator());
                    break;
                case "按姓名排序":
                    swingBMI.sortStudents(new SwingBMI.NameComparator());
                    break;
                case "按身高排序":
                    swingBMI.sortStudents(new SwingBMI.HeightComparator());
                    break;
                case "按体重排序":
                    swingBMI.sortStudents(new SwingBMI.WeightComparator());
                    break;
                case "按BMI排序":
                    swingBMI.sortStudents(new SwingBMI.BmiComparator());
                    break;
                default:
                    break;
            }
            panel.toupdate();
        });
        box.setBounds(x, y, width, height);
        panel.add(box);
        return box;
    }
}



interface StudentData {
    public double getData(SwingBMI.Student e);
}

class DataChartPanel extends ChartPanel {
    private MainFrame frame;
    static double[] bmiIntervals = {16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    static double[] heightIntervals = {1.5f, 1.55f, 1.6f, 1.65f, 1.7f, 1.75f, 1.8f, 1.85f, 1.9f, 1.95f};
    static double[] weightIntervals = {55f, 55.5f, 56f, 56.5f, 57f, 57.5f, 58f, 58.5f, 59f, 59.5f};

    DataChartPanel(MainFrame frame, JFreeChart chart) {
        super(chart);
        this.frame = frame;
        setLayout(null);
        addStaticsComboBox(5, 5, 100, 25);
    }

    private JComboBox addStaticsComboBox(int x, int y, int width, int height) {
        JComboBox box = new JComboBox();
        box.addItem("BMI统计数据");
        box.addItem("身高统计数据");
        box.addItem("体重统计数据");

        box.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                switch ((String) e.getItem()) {
                    case "BMI统计数据":
                        setChart(ChartFactory.createXYBarChart("BMI Statics",
                                "Intervals", false, "Number of Students",
                                createDataset(String.format("BMI-Mode:%3.2f Middle:%3.2f Average:%3.2f Variance:%3.2f",
                                        frame.swingBMI.bmiMode(SwingBMI.Student::getBmis),
                                        frame.swingBMI.bmiMedian(SwingBMI.Student::getBmis),
                                        frame.swingBMI.bmiAverage(SwingBMI.Student::getBmis),
                                        frame.swingBMI.bmiVariance(SwingBMI.Student::getBmis)),
                                        bmiIntervals, frame.swingBMI.students, SwingBMI.Student::getBmis, 0.9f), PlotOrientation.VERTICAL,
                                true, false, false));
                        break;
                    case "身高统计数据":
                        setChart(ChartFactory.createXYBarChart("Height Statics",
                                "Intervals", false, "Number of Students",
                                createDataset(String.format("Height-Mode:%3.2f Middle:%3.2f Average:%3.2f Variance:%3.2f",
                                        frame.swingBMI.bmiMode(SwingBMI.Student::getHeights),
                                        frame.swingBMI.bmiMedian(SwingBMI.Student::getHeights),
                                        frame.swingBMI.bmiAverage(SwingBMI.Student::getHeights),
                                        frame.swingBMI.bmiVariance(SwingBMI.Student::getHeights)),
                                        heightIntervals, frame.swingBMI.students, SwingBMI.Student::getHeights, 0.04f), PlotOrientation.VERTICAL,
                                true, false, false));
                        break;
                    case "体重统计数据":
                        setChart(ChartFactory.createXYBarChart("Weight Statics",
                                "Intervals", false, "Number of Students",
                                createDataset(String.format("Weight-Mode:%3.2f Middle:%3.2f Average:%3.2f Variance:%3.2f",
                                        frame.swingBMI.bmiMode(SwingBMI.Student::getWeights),
                                        frame.swingBMI.bmiMedian(SwingBMI.Student::getWeights),
                                        frame.swingBMI.bmiAverage(SwingBMI.Student::getWeights),
                                        frame.swingBMI.bmiVariance(SwingBMI.Student::getWeights)),
                                        weightIntervals, frame.swingBMI.students, SwingBMI.Student::getWeights, 0.4f), PlotOrientation.VERTICAL,
                                true, false, false));
                        break;
                    default:
                        break;
                }
        });

        box.setBounds(x, y, width, height);
        add(box);
        return box;
    }

    static IntervalXYDataset createDataset(String seriesName, double[] intervals, ArrayList<SwingBMI.Student> list, StudentData stu, float width) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries(seriesName);
        int[] count = new int[10];
        for (SwingBMI.Student e : list) {
            if (stu.getData(e) < intervals[0])
                ++count[0];
            else if (stu.getData(e) < intervals[1])
                ++count[1];
            else if (stu.getData(e) < intervals[2])
                ++count[2];
            else if (stu.getData(e) < intervals[3])
                ++count[3];
            else if (stu.getData(e) < intervals[4])
                ++count[4];
            else if (stu.getData(e) < intervals[5])
                ++count[5];
            else if (stu.getData(e) < intervals[6])
                ++count[6];
            else if (stu.getData(e) < intervals[7])
                ++count[7];
            else if (stu.getData(e) < intervals[8])
                ++count[8];
            else ++count[9];
        }
        for (int i = 0; i != 10; ++i) {
            series.add(intervals[i], count[i]);
        }
        seriesCollection.addSeries(series);
        return new XYBarDataset(seriesCollection, width);
    }


}


interface StuData {
    double get(SwingBMI.Student e);
}


class SwingBMI {
    ArrayList<Student> students = new ArrayList<>();

    //  从键盘输入学生信息，函数返回值为Student类型
    private Student inputStudent() {
        Scanner sc = new Scanner(System.in);
        // Student s = new Student();
        String ids;
        do {
            System.out.println("id:");
            ids = sc.nextLine();
        } while (this.isExists(ids));
//        if (this.isExists(ids)) {
//            System.out.println("the student already exists!");
//
//        }
        Student s = new Student();
        System.out.println("name:");
        String names = sc.nextLine();
        System.out.println("height(m):");
        double heights = sc.nextDouble();
        System.out.println("weight(kg):");
        double weights = sc.nextDouble();
        sc.nextLine();// because of the Scanner bug
        double bmis = weights / (heights * heights);
        System.out.print("BMI：" + String.format("%3.2f", bmis));
        s.setIds(ids);
        s.setHeights(heights);
        s.setNames(names);
        s.setWeights(weights);
        return s;
    }

    //询问用户是否继续输入来决定是否继续输入学生(调用inputStudent)
    public void inputStudents() {
        boolean flag = false;
        Scanner in = new Scanner(System.in);
        do {
            students.add(inputStudent());
            System.out.println();
            System.out.println("是否继续输入（y or n)");
            String sc = in.nextLine();
            if (sc.charAt(0) == 'y') flag = true;
            else flag = false;
        } while (flag);
    }

    // 判断该学生是否已经输入（是否在students中）
    public boolean isExists(String id) {
        for (Student s : students) {
            if (id.equals(s.getIds()))
                return true;
        }
        return false;
    }

    //能够随机生成指定数量的名学生对象，并保存到students中
    public void genStudents(int N) {
        Random r = new Random(System.currentTimeMillis());
        int i = 0;
        double x = 0.0f, y = 0.0f;
        while (i < N) {
            x = r.nextGaussian();
            y = r.nextGaussian();
            Student st = new Student(
                    String.format("%04d", r.nextInt(10000)),
                    genRandomString(6), (1.75 + x * 0.1), (60.0 + y * 5));
            System.out.println(st);
            if (!isExists(st.getIds())) {
                students.add(st);
                ++i;
            }
        }
    }

    //随机生成学生姓名
    public static String genRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    // 统计bmi的均值
    public double bmiAverage(StuData e) {
        int size = students.size();
        //System.out.println(size);
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum = sum + e.get(students.get(i));
        }
        double ave = sum / size;
        return ave;
    }

    // 统计bmi的中值
    public double bmiMedian(StuData e) {
        int size = students.size();
        double[] bmis = new double[size];
        for (int i = 0; i < size; i++) {
            bmis[i] = e.get(students.get(i));
        }
        double tmp;
        for (int j = 1; j < size; j++) {
            for (int i = 0; i < size - j; i++) {
                if (bmis[i] > bmis[i + 1]) {
                    tmp = bmis[i];
                    bmis[i] = bmis[i + 1];
                    bmis[i + 1] = tmp;
                }
            }
        }
        int k = size % 2;
        if (k == 0)
            return (bmis[size / 2 - 1] + bmis[size / 2]) / 2;
        else
            return bmis[size / 2];
    }

    // 统计bmi的众数
    public double bmiMode(StuData e) {
        int size = students.size();
        int[] count = new int[size];
        for (int i = 0; i < size; i++)         //Count each unique element
            count[i] = 1;
        int j = 0;
        for (int i = 0; i < size; ) {
            if (i >= (size - 2))
                break;
            if (e.get(students.get(i))!= e.get(students.get(i + 1)))
                i++;
            else {
                j = i + 1;
                while (e.get(students.get(i)) == e.get(students.get(j))) {
                    count[i]++;
                    j++;
                    if (j == size)
                        break;
                }
                i = j;
            }
        }
        int max = count[0];             //find the element that occurs most often
        for (int i = 0; i < size; i++) {
            if (count[i] > max)
                max = count[i];
        }
        for (int i = 0; i < size; i++) {
            if (count[i] == max) {
                return e.get(students.get(i));
            }
        }
        return 0;
    }

    // 统计bmi的方差
    public double bmiVariance(StuData e) {
        int size = students.size();
        double ave = bmiAverage(e);
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum = sum + (e.get(students.get(i)) - ave) * (e.get(students.get(i)) - ave);
        }
        double s = sum / size;
        return s;
    }

    //  增加五个comparator子类
    public void sortStudents(Comparator<Student> c) {
        Collections.sort(students, c);
    }

    //        Collections.sort(students,(Student a,Student b)->a.getNames().compareTo(b.getNames()));
    // 学号排序
    static class IdComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return st1.getIds().compareTo(st2.getIds());
        }
    }

    // 姓名排序
    static class NameComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return st1.getNames().compareTo(st2.getNames());
        }
    }

    // 身高排序
    static class HeightComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return Double.compare(st1.getHeights(), st2.getHeights());
        }
    }

    // 体重排序
    static class WeightComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return Double.compare(st1.getWeights(), st2.getWeights());
        }
    }

    // BMI 排序
    static class BmiComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return Double.compare(st1.getBmis(), st2.getBmis());
        }
    }

    // 将学生信息students写入到指定的文本文件中
    public static void saveFile(ArrayList<Student> students, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName, false);
            for (Student st : students) {
                writer.write(String.format("%s,%s,%.2f,%.2f,%.2f\r\n",
                        st.getIds(), st.getNames(), st.getHeights(), st.getWeights(), st.getBmis()));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读文件中的数据到学生ArrayList中
    public static ArrayList<Student> readFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        ArrayList<Student> v = new ArrayList<Student>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = reader.readLine();
            while (tempString != null) {
                String[] a = tempString.split(",");
                Student st = new Student(a[0], a[1], Double.parseDouble(a[2]), Double.parseDouble(a[3]), Double.parseDouble(a[4]));
                v.add(st);
                tempString = reader.readLine();

            }
            reader.close();
            return v;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    //  根据学号查询学生信息并显示，然后提示用户是否修改或删除学生
    public void findStudent() {
        System.out.println("请输入要查询学生的学号：");
        Scanner in = new Scanner(System.in);
        String SstudentId = in.nextLine();
        for (int i = 0; i < students.size(); i++)
            if (SstudentId.equals(students.get(i).getIds())) {
                System.out.println("找到该学生！");
                System.out.println(students.get(i).toString());
                System.out.println("接下来要：增加（z）？删除（s)？修改（x）？");
                String sc = in.nextLine();
                if (sc.equals("z")) {
                    students.add(inputStudent());
                }
                if (sc.equals("s")) {
                    deleteStudent(i);
                }
                if (sc.equals("x")) {
                    modifyStudent(i);
                }
            }
    }

    //  修改学生信息
    public void modifyStudent(int i) {
        Scanner in = new Scanner(System.in);
        System.out.println("请输入学生的新姓名：");
        String xName = in.next();
        System.out.println("请输入学生的新身高（m）：");
        double xHeight = in.nextDouble();
        System.out.println("请输入学生的新体重（kg）：");
        double xWeight = in.nextDouble();
        students.get(i).setNames(xName);
        students.get(i).setHeights(xHeight);
        students.get(i).setWeights(xWeight);
    }

    //  删除该学生
    public void deleteStudent(int i) {
        students.remove(i);
    }

    //菜单
    public static void Menu() {
        System.out.println("Management for OOBMI");
        System.out.println("1.从键盘输入学生信息");
        System.out.println("2.随机生成指定数量的学生");
        System.out.println("3.打印所有学生基本信息");
        System.out.println("4.按照学号排序");
        System.out.println("5.按照姓名排序");
        System.out.println("6.按照身高排序");
        System.out.println("7.按照体重排序");
        System.out.println("8.按照BMI排序");
        System.out.println("9.写入到指定的文本文件");
        System.out.println("10.读取文件");
        System.out.println("11.按学号查找学生");
        System.out.println("0.退出");
        System.out.print("选项:");
    }

    // 内部类Student
    static class Student {
        private String ids;
        private String names;
        private double heights;
        private double weights;
        private double bmis;

        // 构造方法
        Student(String ids, String names, double heights, double weights, double bmis) {
            this.ids = ids;
            this.names = names;
            this.heights = get2Double(heights);
            this.weights = get2Double(weights);
            this.bmis = get2Double(bmis);
        }

        Student(String format, String s, double v, double v1) {
            ids = format;
            names = s;
            heights = get2Double(v);
            weights = get2Double(v1);
            bmis = get2Double(weights / (heights * heights));
        }

        Student() {

        }

        //  get/set方法
        public String getIds() {
            return ids;
        }

        public String getNames() {
            return names;
        }

        public double getHeights() {
            return heights;
        }

        public double getWeights() {
            return weights;
        }

        public double getBmis() {
            return bmis;
        }

        public void setIds(String i) {
            ids = i;
        }

        public void setNames(String n) {
            names = n;
        }

        public void setHeights(double h) {
            heights = get2Double(h);
            bmis = get2Double(weights / (heights * heights));
        }

        public void setWeights(double w) {
            weights = get2Double(w);
            bmis = get2Double(weights / (heights * heights));
        }

        // toString
        public String toString() {
            return "ID：" + ids + " " + names + " " + String.format("%3.2f", heights)
                    + "m" + " " + String.format("%3.2f", weights) + "kg" + "  BMI：" + String.format("%3.2f", bmis);
        }

        public Double get2Double(double to) {
            // System.out.println("what in:"+to);
            BigDecimal bd = new BigDecimal(to);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
        }

    }
}


class ModPanel extends JPanel {
    private JTextField text_1;
    private JTextField text_2;
    private JTextField text_3;
    private JTextField text_4;
    private JTextField text_5;
    private JTextField text_11;
    private JTextField text_22;
    private JTextField text_33;
    private JTextField text_44;
    private JTextField text_55;

    private MainFrame frame;
    private JButton Mbotton;
    private JButton Pbotton;
    private int index;

    public ModPanel(MainFrame frame) {
        super();
        this.frame = frame;
        setLayout(null);
        setBackground(Color.WHITE);
        JLabel lbId = new JLabel("学号");
        lbId.setBounds(20, 50, 50, 18);

        JLabel lbName = new JLabel("姓名");
        lbName.setBounds(20, 100, 50, 18);

        JLabel lbHeight = new JLabel("身高");
        lbHeight.setBounds(20, 150, 50, 18);

        JLabel lbWeight = new JLabel("体重");
        lbWeight.setBounds(20, 200, 50, 18);

        JLabel lbBmi = new JLabel("BMI");
        lbBmi.setBounds(20, 250, 50, 18);

        text_1 = new JTextField("", 10);
        text_1.setBounds(70, 50, 100, 20);
        text_2 = new JTextField("", 10);
        text_2.setBounds(70, 100, 100, 20);
        text_3 = new JTextField("", 10);
        text_3.setBounds(70, 150, 100, 20);
        text_4 = new JTextField("", 10);
        text_4.setBounds(70, 200, 100, 20);
        text_5 = new JTextField("", 10);
        text_5.setBounds(70, 250, 100, 20);

        text_11 = new JTextField("", 10);
        text_11.setEditable(false);
        text_11.setBounds(210, 50, 100, 20);
        text_22 = new JTextField("", 10);
        text_22.setBounds(210, 100, 100, 20);
        text_33 = new JTextField("", 10);
        text_33.setBounds(210, 150, 100, 20);
        text_44 = new JTextField("", 10);
        text_44.setBounds(210, 200, 100, 20);
        text_55 = new JTextField("", 10);
        text_55.setEditable(false);
        text_55.setBounds(210, 250, 100, 20);

        add(lbId);
        add(text_1);
        add(text_11);
        add(lbName);
        add(text_2);
        add(text_22);
        add(lbHeight);
        add(text_3);
        add(text_33);
        add(lbWeight);
        add(text_4);
        add(text_44);
        add(lbBmi);
        add(text_5);
        add(text_55);

        Pbotton = new JButton("查询");
        Pbotton.setBounds(80, 300, 80, 20);
        add(Pbotton);

        Mbotton = new JButton("修改");
        Mbotton.setBounds(220, 300, 80, 20);
        add(Mbotton);
        Pbotton.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < frame.swingBMI.students.size(); i++) {
                if (frame.swingBMI.students.get(i).getIds().equals(text_1.getText())) {
                    index = i;
                    text_11.setText(text_1.getText());
                    text_2.setText(frame.swingBMI.students.get(i).getNames());
                    text_3.setText(String.valueOf(frame.swingBMI.students.get(i).getHeights()));
                    text_4.setText(String.valueOf(frame.swingBMI.students.get(i).getWeights()));
                    text_5.setText(String.valueOf(frame.swingBMI.students.get(i).getBmis()));
                    frame.resultPanel.toupdate();
                    setVisible(true);
                    break;
                }
            }
        });

        Mbotton.addActionListener((ActionEvent e) -> {
            frame.swingBMI.students.get(index).setNames(text_22.getText());
            frame.swingBMI.students.get(index).setHeights(Double.parseDouble(text_33.getText()));
            frame.swingBMI.students.get(index).setWeights(Double.parseDouble(text_44.getText()));
            saveFile(frame.swingBMI.students, "test.txt");
            frame.resultPanel.toupdate();
            JOptionPane.showMessageDialog(null, "修改成功", "Prompt", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    void toUpdate() {
        text_1.setText(frame.swingBMI.students.get(frame.index).getIds());
        text_11.setText(text_1.getText());
        text_2.setText(frame.swingBMI.students.get(frame.index).getNames());
        text_3.setText(String.valueOf(frame.swingBMI.students.get(frame.index).getHeights()));
        text_4.setText(String.valueOf(frame.swingBMI.students.get(frame.index).getWeights()));
        text_5.setText(String.valueOf(frame.swingBMI.students.get(frame.index).getBmis()));
        frame.resultPanel.toupdate();
        setVisible(true);
    }

}


class DelPanel extends JPanel {
    private MainFrame frame;
    private JTextField text_1;
    private JTextField text_2;
    private JTextField text_3;
    private JTextField text_4;
    private JTextField text_5;

    private JButton insertButton;

    DelPanel(MainFrame frame) {
        super();
        this.frame = frame;
        setLayout(null);

        setBackground(Color.WHITE);
        JLabel lbId = new JLabel("学号");
        lbId.setBounds(100, 50, 50, 18);

        text_1 = new JTextField("", 10);
        text_1.setBounds(150, 50, 100, 20);

        add(lbId);
        add(text_1);

        JLabel lbName = new JLabel("姓名");
        lbName.setBounds(100, 100, 50, 18);

        JLabel lbHeight = new JLabel("身高");
        lbHeight.setBounds(100, 150, 50, 18);

        JLabel lbWeight = new JLabel("体重");
        lbWeight.setBounds(100, 200, 50, 18);

        JLabel lbBmi = new JLabel("BMI");
        lbBmi.setBounds(100, 250, 50, 18);

        text_2 = new JTextField("", 10);
        text_2.setEditable(false);
        text_2.setBounds(150, 100, 100, 20);
        text_3 = new JTextField("", 10);
        text_3.setEditable(false);
        text_3.setBounds(150, 150, 100, 20);
        text_4 = new JTextField("", 10);
        text_4.setEditable(false);
        text_4.setBounds(150, 200, 100, 20);
        text_5 = new JTextField("", 10);
        text_5.setEditable(false);
        text_5.setBounds(150, 250, 100, 20);

        add(lbName);
        add(text_2);
        add(lbHeight);
        add(text_3);
        add(lbWeight);
        add(text_4);
        add(lbBmi);
        add(text_5);

        insertButton = new JButton("删除");
        insertButton.setBounds(150, 300, 100, 20);
        add(insertButton);
        insertButton.addActionListener((ActionEvent e) -> {
            if (text_1.getText() == null || text_1.getText() == "" || !frame.swingBMI.isExists(text_1.getText())) {
                JOptionPane.showMessageDialog(null, "用户不存在", "Alert", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                for (int i = 0; i < frame.swingBMI.students.size(); i++)
                    if (text_1.getText().equals(frame.swingBMI.students.get(i).getIds())) {
                        text_1.setText(frame.swingBMI.students.get(i).getIds());
                        text_2.setText(frame.swingBMI.students.get(i).getNames());
                        text_3.setText(String.valueOf(frame.swingBMI.students.get(i).getHeights()));
                        text_4.setText(String.valueOf(frame.swingBMI.students.get(i).getWeights()));
                        text_5.setText(String.valueOf(frame.swingBMI.students.get(i).getBmis()));
                        frame.swingBMI.students.remove(i);
                        frame.resultPanel.toupdate();
                    }
                JOptionPane.showMessageDialog(null, "删除成功", "Prompt", JOptionPane.INFORMATION_MESSAGE);
                frame.resultPanel.toupdate();
            }
        });
    }

    void toUpdate() {
        text_1.setText(frame.swingBMI.students.get(frame.index).getIds());
        text_2.setText(frame.swingBMI.students.get(frame.index).getNames());
        text_3.setText(String.valueOf(frame.swingBMI.students.get(frame.index).getHeights()));
        text_4.setText(String.valueOf(frame.swingBMI.students.get(frame.index).getWeights()));
        text_5.setText(String.valueOf(frame.swingBMI.students.get(frame.index).getBmis()));
        frame.resultPanel.toupdate();
        setVisible(true);
    }

}


class AddPanel extends JPanel {

    private MainFrame frame;
    private JTextField text_1;
    private JTextField text_2;
    private JTextField text_3;
    private JTextField text_4;
    private JTextField text_5;
    private JButton insertButton;

    AddPanel(MainFrame frame) {
        super();
        this.frame = frame;
        display();
    }

    private void display() {

        setLayout(null);
        setBackground(Color.WHITE);
        JLabel lbId = new JLabel("学号");
        lbId.setBounds(100, 50, 50, 18);


        JLabel lbName = new JLabel("姓名");
        lbName.setBounds(100, 100, 50, 18);


        JLabel lbHeight = new JLabel("身高");
        lbHeight.setBounds(100, 150, 50, 18);


        JLabel lbWeight = new JLabel("体重");
        lbWeight.setBounds(100, 200, 50, 18);


        JLabel lbBmi = new JLabel("BMI");
        lbBmi.setBounds(100, 250, 50, 18);


        text_1 = new JTextField("", 10);
        text_1.setBounds(150, 50, 100, 20);
        text_2 = new JTextField("", 10);
        text_2.setBounds(150, 100, 100, 20);
        text_3 = new JTextField("", 10);
        text_3.setBounds(150, 150, 100, 20);
        text_4 = new JTextField("", 10);
        text_4.setBounds(150, 200, 100, 20);
        text_5 = new JTextField("", 10);
        text_5.setBounds(150, 250, 100, 20);
        text_5.setEditable(false);

//       text_1.setBackground(Color.BLACK);
//        text_1.setBounds(7,24,200,24);
//       add(text_1);
        add(lbId);
        add(text_1);
        add(lbName);
        add(text_2);
        add(lbHeight);
        add(text_3);
        add(lbWeight);
        add(text_4);
        add(lbBmi);
        add(text_5);

        insertButton = new JButton("保存");
        insertButton.setBounds(150, 300, 100, 20);
        add(insertButton);
        SwingBMI.Student st = new SwingBMI.Student();
        insertButton.addActionListener((ActionEvent e) -> {
            st.setIds(text_1.getText());
            st.setNames(text_2.getText());
            st.setHeights(Double.parseDouble(text_3.getText()));
            st.setWeights(Double.parseDouble(text_4.getText()));
            if (text_1.getText() == null || text_1.getText() == "" || frame.swingBMI.isExists(text_1.getText())) {
                JOptionPane.showMessageDialog(null, "用户已经存在，请重新输入", "Alert", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                frame.swingBMI.students.add(st);
                saveFile(frame.swingBMI.students, "test.txt");
                JOptionPane.showMessageDialog(null, "保存成功", "Prompt", JOptionPane.INFORMATION_MESSAGE);
                frame.resultPanel.toupdate();
                return;
            }
        });
    }
}
