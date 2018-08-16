package com.springboot.springbootnio.controller;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {

    @Autowired
    Buffer buffer;

    public static void main(String[] args) throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("springboot-nio/src/main/resources/data/fileChannel.txt", "rw");
        FileChannel channel = fromFile.getChannel();
        fileChannel(channel);
        channel.close();

    }

    static void fileChannel(FileChannel channel) throws IOException {
        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(64);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();

        while(buf.hasRemaining()) {
            channel.write(buf);
        }
    }
}
