package com.mycompany.app.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;


public class NettyEchoNioClient2 {
    final ByteBuf hiBuf = Unpooled.copiedBuffer("Hi! what is happening, boss?\r\n", Charset.forName("UTF-8"));

    public void connect(int port, String host) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
                            .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO),
                                    new EchoClientHandler(15));
                        }
                    });

            for(int i=0;i<3000;i++) {
                Channel f = b.connect().channel();

                f.closeFuture();
            }
            b.connect().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
    class EchoClientHandler extends ChannelInboundHandlerAdapter {


        private final ByteBuf firstMessage;

        /**
         * Creates a client-side handler.
         */
        public EchoClientHandler(int firstMessageSize) {
            if (firstMessageSize <= 0) {
                throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
            }
            firstMessage = Unpooled.buffer(firstMessageSize);
            for (int i = 0; i < firstMessage.capacity(); i++) {
                firstMessage.writeByte((byte) i);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.writeAndFlush(hiBuf.copy());
            System.out.print("active");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ctx.writeAndFlush(msg+"\n\r");
            System.out.println("read "+msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
            System.out.println("readok");
            ctx.channel().close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // Close the connection when an exception is raised.

            ctx.close();
        }

    }


    public static void main(String[] args) throws Exception {
        new NettyEchoNioClient2().connect(6888, "127.0.0.1");
    }
}

