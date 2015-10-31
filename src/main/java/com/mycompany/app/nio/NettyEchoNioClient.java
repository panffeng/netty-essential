package com.mycompany.app.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NettyEchoNioClient {
    static final Pattern pattern = Pattern.compile(".*\\W*counting sheep, (\\d+) little lambs.");

    public void connect(int port, String host) throws Exception {
        final ByteBuf hiBuf = Unpooled.copiedBuffer("counting sheep? \n\r", Charset.forName("UTF-8"));
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
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO)).addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()) {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(hiBuf);
                                    super.channelActive(ctx);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf byteBuf = (ByteBuf) msg;
                                    String received = byteBuf.toString(CharsetUtil.UTF_8);
                                    if (received.equals("\u0003")) {
                                        ctx.close();
                                    }
                                    super.channelRead(ctx, msg);
                                }
                            }).addLast(new StringDecoder(CharsetUtil.UTF_8)).addLast(new StringEncoder(CharsetUtil.UTF_8)).addLast(new SimpleChannelInboundHandler<String>() {
                                // int count = 0;

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                    String message = null;
                                    String messageLog = "client received for the time " + Thread.currentThread() + "\tmsg: " + msg + "\n\r";
                                    System.out.println(messageLog);
                                    Matcher matcher = pattern.matcher(msg);
                                    if (matcher.find()) {
                                        String countVal = matcher.group(1);
                                        Integer count = Integer.valueOf(countVal) + 1;
                                        if (1000 == count) {
                                            ctx.close();
                                        }
                                        message = "counting sheep, " + count + " little lambs.\n\r";

                                    } else if (StringUtils.isNotBlank(msg) && !msg.contains("sheep")) {
                                        message = "counting sheep, " + 222 + " little lambs.\n\r";
                                    }
                                    if (StringUtils.isNotBlank(message)) {
                                        ctx.writeAndFlush(message);
                                    }

//                                    ctx.close();
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx,
                                                            Throwable cause) {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    });

            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyEchoNioClient().connect(6888, "127.0.0.1");
    }
}

