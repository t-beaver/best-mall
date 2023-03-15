package dc.squareup.okhttp3.internal.cache;

import dc.squareup.okio.Buffer;
import dc.squareup.okio.ForwardingSink;
import dc.squareup.okio.Sink;
import java.io.IOException;

class FaultHidingSink extends ForwardingSink {
    private boolean hasErrors;

    FaultHidingSink(Sink sink) {
        super(sink);
    }

    public void close() throws IOException {
        if (!this.hasErrors) {
            try {
                super.close();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    public void flush() throws IOException {
        if (!this.hasErrors) {
            try {
                super.flush();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onException(IOException iOException) {
    }

    public void write(Buffer buffer, long j) throws IOException {
        if (this.hasErrors) {
            buffer.skip(j);
            return;
        }
        try {
            super.write(buffer, j);
        } catch (IOException e) {
            this.hasErrors = true;
            onException(e);
        }
    }
}
