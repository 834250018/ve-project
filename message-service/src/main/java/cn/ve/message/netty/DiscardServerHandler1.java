package cn.ve.message.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ve
 * @date 2022/2/24
 */
public class DiscardServerHandler1 extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ((ByteBuf)msg).release();

        /*
         try {
            // Do something with msg
        } finally {
            ReferenceCountUtil.release(msg);
        }
        * */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
