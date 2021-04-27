package com.ning.geekbang.github_oauth2;

import java.io.*;
import java.util.Random;

public class FileUtils {

    public static void main(String[] args) {
        StringBuilder num1 = new StringBuilder();
        StringBuilder num2 = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int j = random.nextInt(10);
            num1.append(j == 0 ? 1 : j);
            int k = random.nextInt(10);
            num2.append(k == 0 ? 1 : k);
        }
        String pathNum1 = "/Users/guotongning/from_member/num1.txt";
        String pathNum2 = "/Users/guotongning/from_member/num2.txt";
        writeStr2File(pathNum1, num1.toString());
        writeStr2File(pathNum2, num2.toString());
        String num1FromDist = readStrFromFile("/Users/guotongning/from_member/num1.txt");
        System.out.println(num1);
        System.out.println(num1FromDist);
    }

    public static void writeStr2File(String filePath, String content) {
        File file = new File(filePath);
        try (PrintStream printStream = new PrintStream(new FileOutputStream(file))) {
            printStream.println(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readStrFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line = br.readLine();
            sb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}