package com.ning.geekbang.github_oauth2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MultiplyLargeNumbers {
    public static void main(String[] args) {
        extracted();
    }

    private static void extracted() {
        long start = System.currentTimeMillis();
        System.out.println("大数相乘");
        String num1 = FileUtils.readStrFromFile("/Users/guotongning/from_member/num1.txt");
        String num2 = FileUtils.readStrFromFile("/Users/guotongning/from_member/num2.txt");
        String multiplyResult = multiply(num1, num2);
        FileUtils.writeStr2File("/Users/guotongning/from_member/result.txt", multiplyResult);
        long end = System.currentTimeMillis();
        System.out.printf("乘数1:[%s]\r\n乘数2:[%s]\r\n结果:[%s]\r\ncost time [%d] ms\r\n", num1, num2, multiplyResult, (end - start));
    }

    public static String multiply(String num1, String num2) {
        Link link1 = new Link(num1);
        Link link2 = new Link(num2);
        Link multiply = multiply(link1, link2);
        return multiply.stringValue();
    }

    public static Link multiply(Link link1, Link link2) {
        if (link1.isEmpty() || link2.isEmpty()) return Link.emptyLink();
        link1.reverse();
        link2.reverse();
        Link max = link1.size() >= link2.size() ? link1 : link2;
        Link min = link1.size() >= link2.size() ? link2 : link1;
        AtomicInteger location = new AtomicInteger();
        String[] resultArr = new String[min.size()];
        min.forEach(value -> {
            StringBuilder stringBuilder = multiply(value, max);
            stringBuilder.reverse();
            for (int i = 0; i < location.get(); i++) {
                stringBuilder.append("0");
            }
            resultArr[location.get()] = stringBuilder.toString();
            location.getAndIncrement();
        });
        Link resultLink = new Link(new Node(0));
        for (String result : resultArr) {
            resultLink = add(resultLink, new Link(result));
        }
        return resultLink;
    }

    public static Link add(Link link1, Link link2) {
        if (link1.isEmpty() || link2.isEmpty()) return Link.emptyLink();
        link1.reverse();
        link2.reverse();
        Link max = link1.size() >= link2.size() ? link1 : link2;
        Link min = link1.size() >= link2.size() ? link2 : link1;
        Node tempT = max.head;
        Node tempL = min.head;
        int carry = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (tempT != null || tempL != null) {
            int curr = 0;
            if (tempT != null && tempL != null) curr = tempT.value + tempL.value + carry;
            if (tempT == null) curr = tempL.value + carry;
            if (tempL == null) curr = tempT.value + carry;
            if (curr >= 10) {
                carry = 1;
                curr = curr % 10;
            } else {
                carry = 0;
            }
            stringBuilder.append(curr);
            tempT = tempT == null ? null : tempT.next;
            tempL = tempL == null ? null : tempL.next;
        }
        if (carry > 0) {
            stringBuilder.append("1");
        }
        return new Link(stringBuilder.reverse().toString());
    }

    public static StringBuilder multiply(int num, Link link2) {
        StringBuilder result = new StringBuilder();
        AtomicInteger carry = new AtomicInteger(0);
        link2.forEach(value -> {
            int curr = num * value + carry.get();
            if (curr >= 10) {
                carry.set(curr / 10);
                curr = curr % 10;
            } else {
                carry.set(0);
            }
            result.append(curr);
        });
        if (carry.get() > 0) {
            result.append(carry.get());
        }
        return result;
    }
}

class Link {
    public Node head;
    private int size;

    public Link() {
    }

    public Link(Node head) {
        this.head = head;
        forEach(value -> size++);
    }

    public Link(String num) {
        if (num.isEmpty()) head = null;
        char[] chars = num.toCharArray();
        head = new Node(chars[0] - '0');
        Node other = head;
        for (int i = 1; i < chars.length; i++) {
            Node temp = new Node(chars[i] - '0');
            other.next = temp;
            other = temp;
        }
        size = num.length();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public static Link emptyLink() {
        return new Link();
    }

    public void reverse() {
        if (head == null) return;
        Node cur = head.next;
        Node pre = head;
        while (cur != null) {
            Node temp = cur.next;
            cur.next = pre;
            head.next = temp;
            pre = cur;
            cur = temp;
        }
        this.head = pre;

    }

    public void forEach(Consumer<Integer> action) {
        Node temp = head;
        if (isEmpty()) return;
        while (temp != null) {
            action.accept(temp.value);
            temp = temp.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Link={");
        forEach(node -> stringBuilder.append(node).append(" -> "));
        if (stringBuilder.length() > 6) stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String stringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}

class Node {
    public int value;
    public Node next;

    public Node(int value) {
        this.value = value;
    }
}