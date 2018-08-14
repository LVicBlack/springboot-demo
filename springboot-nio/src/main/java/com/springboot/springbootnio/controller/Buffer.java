package com.springboot.springbootnio.controller;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Buffer {
    // 解决NIO乱码
    static Charset charset = Charset.forName("UTF-8");//Java.nio.charset.Charset处理了字符转换问题。它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
    static CharsetDecoder decoder = charset.newDecoder();

    public static void main(String[] args) throws IOException {
//        baseMethod();
        transferFrom();
    }

    /**
     * Buffer的基本用法
     * <p>
     * 使用Buffer读写数据一般遵循以下四个步骤：
     * 1.写入数据到Buffer
     * 2.调用flip()方法
     * 3.从Buffer中读取数据
     * 4.调用clear()方法或者compact()方法
     */

    static void baseMethod() throws IOException {

        // 随机流 不属于IO流，支持对文件的读取和写入随机访问。
        // mode-rw-可读可写
        RandomAccessFile aFile = new RandomAccessFile("springboot-nio/src/main/resources/data/nio-data.txt", "rw");
        // 从文件中读写数据。
        FileChannel inChannel = aFile.getChannel();

        //create buffer with capacity of 48 bytes
        // 创建了一个容量为capacity字节的ByteBuffer对象
        ByteBuffer buf = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = CharBuffer.allocate(1024);

        // 从该通道读取到给定缓冲区的字节序列。
        // 从该通道的当前文件位置开始读取字节，然后以实际读取的字节数更新文件位置。 否则，此方法的行为与ReadableByteChannel界面中的规定完全相同。
        // 返回读取的字节数
        int bytesRead = inChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {

            // 翻转这个缓冲区。
            buf.flip();  //make buffer ready for read
            decoder.decode(buf, charBuffer, false);
            charBuffer.flip();

            // 当前位置和极限之间是否存在任何元素。
            while (charBuffer.hasRemaining()) {
                System.out.print(charBuffer.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            charBuffer.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }

    static void transferFrom() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("springboot-nio/src/main/resources/data/fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("springboot-nio/src/main/resources/data/toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = CharBuffer.allocate(1024);

        // 从该通道读取到给定缓冲区的字节序列。
        // 从该通道的当前文件位置开始读取字节，然后以实际读取的字节数更新文件位置。 否则，此方法的行为与ReadableByteChannel界面中的规定完全相同。
        // 返回读取的字节数
        int bytesRead = toChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {

            // 翻转这个缓冲区。
            buf.flip();  //make buffer ready for read
            decoder.decode(buf, charBuffer, false);
            charBuffer.flip();

            // 当前位置和极限之间是否存在任何元素。
            while (charBuffer.hasRemaining()) {
                System.out.print(charBuffer.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            charBuffer.clear(); //make buffer ready for writing
            bytesRead = toChannel.read(buf);
        }
        toChannel.close();
    }

}
