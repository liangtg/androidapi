package com.example;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class MyClass {
    public static void main(String[] args) throws IOException {
        ArrayList<Item> data = new ArrayList<>();
        BufferedSource readBuffer = Okio.buffer(Okio.source(new File("test.html")));
        int code = readBuffer.readUtf8CodePoint();
        int index = 0;
        boolean next = false;
        boolean read = false;
        StringBuffer sb = new StringBuffer();
        ByteArrayOutputStream bao = new ByteArrayOutputStream(64);
        BufferedSink writer = Okio.buffer(Okio.sink(bao));
        int last = 0;
        int id = 0, page = 0;
        Item item;
        Stack<Integer> stack = new Stack<>();
        while (true) {
            if (code == '<') {
                read = false;
                last++;
                if (last == 3) {
                    next = true;
                }
            } else if (code == '>') {
                read = true;
                last++;
                if (last == 4) {
                    writer.flush();
                    String trim = bao.toString().trim();
                    if (trim.length() > 0) {
                        StringBuilder tmp = new StringBuilder();
                        for (int i = 0; i < index - 1; i++) {
                            tmp.append(" ");
                        }
                        System.out.println(String.format("%d%s%s", index, tmp.toString(), trim));
                    }
                    bao.reset();
                    if (stack.size() > 0) {
                        last = stack.pop();
                        index--;
                    }
                }
            } else if (next) {
                next = false;
                if (code == '/') {
                } else {
                    index++;
                    stack.push(--last);
                    last = 1;
                }
            } else if (read) {
                writer.writeUtf8CodePoint(code);
            }
            try {
                code = readBuffer.readUtf8CodePoint();
            } catch (IOException e) {
                break;
            }
        }

        readBuffer.close();
    }

    private static class Item {
        int id, page;
        String content;
    }

}
