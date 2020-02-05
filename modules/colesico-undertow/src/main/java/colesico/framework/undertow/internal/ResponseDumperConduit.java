package colesico.framework.undertow.internal;

import io.undertow.UndertowMessages;
import io.undertow.conduits.StoredResponseStreamSinkConduit;
import io.undertow.server.ConduitWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import io.undertow.util.ConduitFactory;
import org.xnio.IoUtils;
import org.xnio.channels.StreamSourceChannel;
import org.xnio.conduits.AbstractStreamSinkConduit;
import org.xnio.conduits.ConduitWritableByteChannel;
import org.xnio.conduits.StreamSinkConduit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Stuart Douglas
 * @author Vladlen Larionov
 */
public class ResponseDumperConduit extends AbstractStreamSinkConduit<StreamSinkConduit> {

    public static final AttachmentKey<byte[]> RESPONSE = AttachmentKey.create(byte[].class);
    private ByteArrayOutputStream outputStream;
    private final HttpServerExchange exchange;

    public ResponseDumperConduit(StreamSinkConduit next, HttpServerExchange exchange) {
        super(next);
        this.exchange = exchange;
        long length = exchange.getResponseContentLength();
        if (length <= 0L) {
            this.outputStream = new ByteArrayOutputStream();
        } else {
            if (length > 2147483647L) {
                throw UndertowMessages.MESSAGES.responseTooLargeToBuffer(length);
            }

            this.outputStream = new ByteArrayOutputStream((int) length);
        }
    }

    public long transferFrom(StreamSourceChannel source, long count, ByteBuffer throughBuffer) throws IOException {
        return IoUtils.transfer(source, count, throughBuffer, new ConduitWritableByteChannel(this));
    }

    public long transferFrom(FileChannel src, long position, long count) throws IOException {
        return src.transferTo(position, count, new ConduitWritableByteChannel(this));
    }

    public int write(ByteBuffer src) throws IOException {
        int start = src.position();
        int ret = super.write(src);

        for (int i = start; i < start + ret; ++i) {
            this.outputStream.write(src.get(i));
        }

        return ret;
    }

    public long write(ByteBuffer[] srcs, int offs, int len) throws IOException {
        int[] starts = new int[len];

        for (int i = 0; i < len; ++i) {
            starts[i] = srcs[i + offs].position();
        }

        long ret = super.write(srcs, offs, len);
        long rem = ret;

        for (int i = 0; i < len; ++i) {
            ByteBuffer buf = srcs[i + offs];

            for (int pos = starts[i]; rem > 0L && pos < buf.position(); --rem) {
                this.outputStream.write(buf.get(pos));
                ++pos;
            }
        }

        return ret;
    }

    public int writeFinal(ByteBuffer src) throws IOException {
        int start = src.position();
        int ret = super.writeFinal(src);

        for (int i = start; i < start + ret; ++i) {
            this.outputStream.write(src.get(i));
        }

        if (!src.hasRemaining()) {
            this.exchange.putAttachment(RESPONSE, this.outputStream.toByteArray());
            this.outputStream = null;
        }

        return ret;
    }

    public long writeFinal(ByteBuffer[] srcs, int offs, int len) throws IOException {
        int[] starts = new int[len];
        long toWrite = 0L;

        for (int i = 0; i < len; ++i) {
            starts[i] = srcs[i + offs].position();
            toWrite += (long) srcs[i + offs].remaining();
        }

        long ret = super.write(srcs, offs, len);
        long rem = ret;

        for (int i = 0; i < len; ++i) {
            ByteBuffer buf = srcs[i + offs];

            for (int pos = starts[i]; rem > 0L && pos < buf.position(); --rem) {
                this.outputStream.write(buf.get(pos));
                ++pos;
            }
        }

        if (toWrite == ret) {
            this.exchange.putAttachment(RESPONSE, this.outputStream.toByteArray());
            this.outputStream = null;
        }

        return ret;
    }

    public void terminateWrites() throws IOException {
        if (outputStream != null) {
            this.exchange.putAttachment(RESPONSE, this.outputStream.toByteArray());
            this.outputStream = null;
        }
        super.terminateWrites();
    }

    public static class ResponseDumperHandler implements HttpHandler {
        private final HttpHandler next;

        public ResponseDumperHandler(HttpHandler next) {
            this.next = next;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            exchange.addResponseWrapper(new ConduitWrapper<StreamSinkConduit>() {
                @Override
                public StreamSinkConduit wrap(ConduitFactory<StreamSinkConduit> factory, HttpServerExchange exchange) {
                    return new StoredResponseStreamSinkConduit(factory.create(), exchange);
                }
            });
            next.handleRequest(exchange);
        }
    }

}
