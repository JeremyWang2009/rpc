package com.zfzj.netty.http.server;

import com.zfzj.netty.http.pojo.User;
import com.zfzj.netty.http.protocol.JSONSerializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;

import java.util.Date;

/**
 * @author jeremy.wang
 * @date 2019-11-06
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final String FAVICON_ICO = "/favicon.ico";

    private static final String GET = "get";

    private static final String POST = "post";

    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");

    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");

    private static final AsciiString CONNECTION = AsciiString.cached("Connection");

    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        User user = new User();
        user.setDate(new Date());
        user.setUserName("netty server");

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();
            if (uri.equalsIgnoreCase(FAVICON_ICO)) {}

            HttpMethod method = request.method();

            if (method.equals(HttpMethod.GET)) {
                user.setMethod(GET);
            } else if (method.equals(HttpMethod.POST)) {
                user.setMethod(POST);
            }

            JSONSerializer jsonSerializer = new JSONSerializer();
            byte[] content = jsonSerializer.serialize(user);

            FullHttpResponse response =
                    new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            Unpooled.wrappedBuffer(content));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
