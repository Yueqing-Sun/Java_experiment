package edu.hit1160300901.experiment3;

/**
 * Created by 孙月晴 on 2017/7/17.
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

//import java.io.IOException;
//import java.io.FileWriter;
//import java.io.FileReader;
//import java.io.BufferedReader;
public class OOBMI {
    ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        OOBMI stu = new OOBMI();
        Scanner in = new Scanner(System.in);
        int ch, n;
//        stu.inputStudents();
//        stu.genStudents(3);
//        stu.printStatics();
        stu.sortStudents(stu.new HeightComparator()); // NameComparator());
        do {
            Menu();
            ch = in.nextInt();
            in.nextLine();
            switch (ch) {
                case 0:
                    break;
                case 1:
                    stu.inputStudents();
                    break;
                case 2:
                    System.out.println("输入要生成的学生数量：");
                    n = in.nextInt();
                    in.nextLine();
                    stu.genStudents(n);
                    break;
                case 3:
                    stu.printStatics();
                    break;
                case 4:
                    stu.sortStudents(stu.new IdComparator());
                    stu.printStatics();
                    break;
                case 5:
                    stu.sortStudents(stu.new NameComparator());
                    stu.printStatics();
                    break;
                case 6:
                    stu.sortStudents(stu.new HeightComparator());
                    stu.printStatics();
                    break;
                case 7:
                    stu.sortStudents(stu.new WeightComparator());
                    stu.printStatics();
                    break;
                case 8:
                    stu.sortStudents(stu.new BmiComparator());
                    stu.printStatics();
                    break;
                case 9:
                    saveFile(stu.students, "test.txt");
                    break;
                case 10:
                    stu.students.addAll(readFile("test.txt"));
                    break;
                case 11:
                    stu.findStudent();
                    break;
                default:
                    System.out.print("Input error!");
            }
        } while (ch != 0);
    }

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
    private boolean isExists(String id) {
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
    public double bmiAverage() {
        int size = students.size();
        //System.out.println(size);
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum = sum + students.get(i).getBmis();
        }
        double ave = sum / size;
        return ave;
    }

    // 统计bmi的中值
    public double bmiMedian() {
        int size = students.size();
        double[] bmis = new double[size];
        for (int i = 0; i < size; i++) {
            bmis[i] = students.get(i).getBmis();
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
    public void bmiMode() {
        int size = students.size();
        int[] count = new int[size];
        for (int i = 0; i < size; i++)         //Count each unique element
            count[i] = 1;
        int j = 0;
        for (int i = 0; i < size; ) {
            if (i >= (size - 2))
                break;
            if (students.get(i).getBmis() != students.get(i + 1).getBmis())
                i++;
            else {
                j = i + 1;
                while (students.get(i).getBmis() == students.get(j).getBmis()) {
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
        if (max == 1) {
            System.out.println("modes{None or All}");
            return;
        }
        String s = "";
        boolean flag = true;
        for (int i = 0; i < size; i++) {
            if (count[i] == max) {
                if (flag) {
                    s = "" + students.get(i).getBmis();
                    flag = false;
                } else {
                    s = s + "," + students.get(i).getBmis();
                }
            }
        }
        System.out.println("modes={" + s + "}");
    }

    // 统计bmi的方差
    public double bmiVariance() {
        int size = students.size();
        double ave = bmiAverage();
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum = sum + (students.get(i).getBmis() - ave) * (students.get(i).getBmis() - ave);
        }
        double s = sum / size;
        return s;
    }

    //打印所有学生基本信息，以及统计结果信息
    public void printStatics() {
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("平均数：" + new Student().get2Double(bmiAverage()));
        System.out.println("中数：" + new Student().get2Double(bmiMedian()));
        bmiMode();
        System.out.println("方差：" + new Student().get2Double(bmiVariance()));
    }

    //  增加五个comparator子类
    public void sortStudents(Comparator<Student> c) {
        Collections.sort(students, c);
    }
    //        Collections.sort(students,(Student a,Student b)->a.getNames().compareTo(b.getNames()));
    // 学号排序
    private class IdComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return st1.getIds().compareTo(st2.getIds());
        }
    }

    // 姓名排序
    private class NameComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return st1.getNames().compareTo(st2.getNames());
        }
    }

    // 身高排序
    private class HeightComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return Double.compare(st1.getHeights(), st2.getHeights());
        }
    }

    // 体重排序
    private class WeightComparator implements Comparator<Student> {
        @Override
        public int compare(Student st1, Student st2) {
            return Double.compare(st1.getWeights(), st2.getWeights());
        }
    }

    // BMI 排序
    private class BmiComparator implements Comparator<Student> {
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
            return "学号：" + ids + "\t" + "姓名：" + names + "\t" + "身高：" + heights
                    + "米" + "\t" + "  体重：" + weights + "公斤" + "\t" + "  BMI：" + String.format("%3.2f", bmis);
        }

        public Double get2Double(double to) {
            // System.out.println("what in:"+to);
            BigDecimal bd = new BigDecimal(to);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
        }

    }

}