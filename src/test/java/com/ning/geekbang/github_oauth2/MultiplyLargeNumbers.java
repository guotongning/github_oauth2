package com.ning.geekbang.github_oauth2;

import java.util.function.Consumer;

public class MultiplyLargeNumbers {
    public static void main(String[] args) {
        test();
        extracted();
    }

    private static void test() {
        String str = "1";
        Link node = new Link(str);
        String result = node.stringValue();

        System.out.println(node.toString());
        System.out.println(result);
        System.out.println(str.equals(result));
    }

    private static void extracted() {
        long start = System.currentTimeMillis();
        System.out.println("大数相乘");
        String num2 = "86757434325654327243678264389532867463289756289745689735";
        String num1 = "1542431556485677895623874562938745692873568973";
        String multiplyResult = multiply(num1, num2);
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
        //倒序，从各位开始算。
        link1.reverse();
        link2.reverse();

        return new Link(new Node(0));
    }

}

class Link {
    public Node head;

    public Link() {
    }

    public Link(Node head) {
        this.head = head;
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