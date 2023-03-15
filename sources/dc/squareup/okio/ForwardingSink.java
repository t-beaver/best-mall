package dc.squareup.okio;

import com.taobao.weex.el.parse.Operators;
import java.io.IOException;

public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink sink) {
        if (sink != null) {
            this.delegate = sink;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    public void close() throws IOException {
        this.delegate.close();
    }

    public final Sink delegate() {
        return this.delegate;
    }

    public void flush() throws IOException {
        this.delegate.flush();
    }

    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public String toString() {
        return getClass().getSimpleName() + Operators.BRACKET_START_STR + this.delegate.toString() + Operators.BRACKET_END_STR;
    }

    public void write(Buffer buffer, long j) throws IOException {
        this.delegate.write(buffer, j);
    }
}
