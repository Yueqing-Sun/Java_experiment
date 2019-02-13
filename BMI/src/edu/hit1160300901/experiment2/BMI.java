/**
 * Created by 孙月晴 on 2017/7/11.
 */
package edu.hit1160300901.experiment2;
import java.util.Scanner;

public class BMI {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int i, ch;
//        System.out.print("输入学生人数：");
//        int N = in.nextInt();
//        in.nextLine();
//        String[] ids = new String[N];
//        String[] names = new String[N];
//        float[] heights = new float[N];
//        float[] weights = new float[N];
//        float[] bmis = new float[N];
//        for (i = 0; i < N; i++) {
//            System.out.print("输入学号：");
//            ids[i] = in.nextLine();
//            System.out.print("输入姓名：");
//            names[i] = in.nextLine();
//            System.out.print("输入身高(cm)：");
//            heights[i] = in.nextFloat();
//            in.nextLine();
//            System.out.print("输入体重(斤)：");
//            weights[i] = in.nextFloat();
//            in.nextLine();
//            bmis[i] = calcuBmi(heights[i], weights[i]);
//            System.out.println("学号：" + ids[i] + "，姓名：" + names[i] + "，身高：" + heights[i]
//                    + "厘米，体重：" + weights[i] + "斤，BMI：" + String.format("%3.2f", bmis[i]));
//        }

        int N = 4;
        String[] ids = {"001", "005", "007", "009"};
        String[] names = {"bbb", "ccc", "aaa", "eee"};
        float[] heights = {163, 162, 168, 165};
        float[] weights = {100, 105, 103, 99};
        float[] bmis = {20, 21, 19, 19.5f};

        int[] sortedIndex = new int[N];
        while (true) {
            Menu();
            ch = in.nextInt();
            in.nextLine();
            switch (ch) {
                case 1:
                    sortedIndex = sortByids(ids);
                    break;
                case 2:
                    sortedIndex = sortBynames(names);
                    break;
                case 3:
                    sortedIndex = sortByHeights(heights);
                    break;
                case 4:
                    sortedIndex = sortByWeights(weights);
                    break;
                case 5:
                    sortedIndex = sortByBmi(bmis);
                    break;
                default:
                    System.out.print("Input error!");
            }
            printStudents(ids, names, heights, weights, bmis, sortedIndex, N);
        }
    }

    public static float calcuBmi(float height, float weight) {
        float bmi = weight / (height * height) * 5000;
        return bmi;
    }

    public static String checkHealth(float bmi) {
        if (bmi <= 18.5)
            return "UnderWeight";
        else if (bmi > 18.5 && bmi <= 23)
            return "Normal Range";
        else if (bmi > 23 && bmi <= 25)
            return "Overweight-At Risk";
        else if (bmi > 25 && bmi <= 30)
            return "Overweight-Moderately Obese";
        else
            return "Overweight-Severely Obese";
    }

    public static int[]  sortBy12(String[] arr) {
        String tmp;
        int tmp2;
        int[] sortedIndex = new int[arr.length];
        String[] Ano=new String[arr.length];
        for(int i=0;i<arr.length;i++)
        {
            Ano[i]=arr[i];
            sortedIndex[i]=i;
        }
        for(int j=1; j < arr.length; j++){
            for (int i = 0; i < arr.length-j; i++) {
                if ((Ano[i].compareTo(Ano[i+1]) > 0)) {
                    tmp = Ano[i];
                    Ano[i] = Ano[i + 1];
                    Ano[i + 1] = tmp;

                    tmp2=sortedIndex[i];
                    sortedIndex[i]=sortedIndex[i+1];
                    sortedIndex[i+1]=tmp2;
                }
            }
        }
        return sortedIndex;
    }

    public static int[] sortByids(String[] ids) {
        return sortBy12(ids);
    }

    public static int[] sortBynames(String[] ids) {
        return sortBy12(ids);
    }


    public static int[]  sortBy345(float[] arr) {
        float tmp;
        int tmp2;
        int[] sortedIndex = new int[arr.length];
        float[] Ano=new float[arr.length];
        for(int i=0;i<arr.length;i++)
        {
            Ano[i]=arr[i];
            sortedIndex[i]=i;
        }
        for(int j=1; j < arr.length; j++){
            for (int i = 0; i < arr.length-j; i++) {
                if (Ano[i] > Ano[i + 1]) {
                    tmp = Ano[i];
                    Ano[i] = Ano[i + 1];
                    Ano[i + 1] = tmp;
                    tmp2=sortedIndex[i];
                    sortedIndex[i]=sortedIndex[i+1];
                    sortedIndex[i+1]=tmp2;
                }
            }
        }
        return sortedIndex;
    }
    public static int[] sortByHeights(float[] num) {
        return sortBy345(num);
    }
    public static int[] sortByWeights(float[] num) {
        return sortBy345(num);
    }
    public static int[] sortByBmi(float[] num) {
        return sortBy345(num);
    }

    public static void printStudents(String[] ids, String[] names, float[] heights,
                                     float[] weights, float[] bmis, int sortedIndex[], int N) {
        System.out.println("排序前：");
        for (int j = 0; j < N; j++) {
            System.out.println("学号：" + ids[j] + "\t"+"姓名：" + names[j] +"\t"+ "身高：" + heights[j]
                    + "厘米"+"\t"+"  体重：" + weights[j] + "斤"+"\t"+"  BMI：" + String.format("%3.2f", bmis[j]));
        }
        int i;
        System.out.println("排序后：");
        for (int j = 0; j < N; j++) {
            i = sortedIndex[j];
            System.out.println("学号：" + ids[i] + "\t"+"姓名：" + names[i] +"\t"+ "身高：" + heights[i]
                    + "厘米"+"\t"+"  体重：" + weights[i] + "斤"+"\t"+"  BMI：" + String.format("%3.2f", bmis[i]));
        }
    }

    public static void Menu() {
        System.out.println("Management for BMI");
        System.out.println("1.Sort by ids:");
        System.out.println("2.Sort by names:");
        System.out.println("3.Sort by heights");
        System.out.println("4.Sort by weights");
        System.out.println("5.Sort by bmis");
        System.out.print("Please input your choice:");
    }


}

