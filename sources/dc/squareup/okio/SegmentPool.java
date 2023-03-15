package dc.squareup.okio;

import android.support.v4.media.session.PlaybackStateCompat;

final class SegmentPool {
    static final long MAX_SIZE = 65536;
    static long byteCount;
    static Segment next;

    private SegmentPool() {
    }

    static void recycle(Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        } else if (!segment.shared) {
            synchronized (SegmentPool.class) {
                long j = byteCount + PlaybackStateCompat.ACTION_PLAY_FROM_URI;
                if (j <= 65536) {
                    byteCount = j;
                    segment.next = next;
                    segment.limit = 0;
                    segment.pos = 0;
                    next = segment;
                }
            }
        }
    }

    static Segment take() {
        synchronized (SegmentPool.class) {
            Segment segment = next;
            if (segment == null) {
                return new Segment();
            }
            next = segment.next;
            segment.next = null;
            byteCount -= PlaybackStateCompat.ACTION_PLAY_FROM_URI;
            return segment;
        }
    }
}
