package com.mycompany.app;

import com.mycompany.app.codec.TastyEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class TastyEncoderTest {
        
    @Test
    public void testEncoded() {   
        //创建一个能容纳int的ByteBuf   
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);   
        }   
        //创建EmbeddedChannel对象   
        EmbeddedChannel channel = new EmbeddedChannel(new TastyEncoder());
        //将buf数据写入出站EmbeddedChannel   
        Assert.assertTrue(channel.writeOutbound(buf));
        //标示EmbeddedChannel完成   
        Assert.assertTrue(channel.finish());   
        //读取出站数据   
       // ByteBuf output = (ByteBuf) channel.readOutbound();
        for (int i = 1; i < 10; i++) {
            Assert.assertEquals(i, channel.readOutbound());
        }   

        Assert.assertNull(channel.readOutbound());   
    }   
            
}   
