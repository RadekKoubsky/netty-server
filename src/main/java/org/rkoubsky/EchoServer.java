package org.rkoubsky;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class EchoServer {
    private final int port;


    public EchoServer(final int port) {
        this.port = port;
    }

    public static void main(final String[] args) throws Exception {
        /*if (args.length != 1) {
            System.err.printf("Usage: %s <port>\n", EchoServer.class.getSimpleName());
        }
        final int port = Integer.parseInt(args[0]);*/
        new EchoServer(8080).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        final EventLoopGroup group = new NioEventLoopGroup();
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel ch) {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            final ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
