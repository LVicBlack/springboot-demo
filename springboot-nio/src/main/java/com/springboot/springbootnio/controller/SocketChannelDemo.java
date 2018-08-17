package com.springboot.springbootnio.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        socketInit();
    }

    static void socketInit() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",8088));
        socketWrite(socketChannel);
        read(socketChannel);
    }

    static void socketWrite(SocketChannel socketChannel) throws IOException {
        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(128);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();

        while(buf.hasRemaining()) {
            socketChannel.write(buf);
        }
    }

    public static void read(SocketChannel inChannel) throws IOException {
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
            Buffer.decoder.decode(buf, charBuffer, false);
            charBuffer.flip();

            // 当前位置和极限之间是否存在任何元素。
            while (charBuffer.hasRemaining()) {
                System.out.print(charBuffer.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            charBuffer.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
    }
}
