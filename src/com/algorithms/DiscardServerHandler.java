package com.algorithms;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Lee on 2017/12/13.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("DiscardServerHandler channelActive...");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("DiscardServerHandler channelRegistered...");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("DiscardServerHandler channelUnregistered...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)

        System.out.println("DiscardServerHandler channelRead...");

            ByteBuf result = (ByteBuf) msg;
            byte[] result1 = new byte[result.readableBytes()];
            // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
            result.readBytes(result1);
            String resultStr = new String(result1);
            // 接收并打印客户端的信息
            System.out.println("Client said:" + resultStr);
            // 释放资源，这行很关键
            result.release();

            // 向客户端发送消息
            String response = "I am ok!";
            // 在当前场景下，发送的数据必须转换成ByteBuf数组
            ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
            encoded.writeBytes(response.getBytes());
            ctx.write(encoded);
            ctx.flush();

//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg); // (2)
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        System.out.println("DiscardServerHandler exceptionCaught...");
        cause.printStackTrace();
        ctx.close();
    }
}
