package io.dcloud.sdk.core.entry;

public class SplashConfig {
    private int a;
    private int b;

    public static final class Builder {
        /* access modifiers changed from: private */
        public int a;
        /* access modifiers changed from: private */
        public int b;

        public SplashConfig build() {
            return new SplashConfig(this);
        }

        public Builder height(int i) {
            this.b = i;
            return this;
        }

        public Builder width(int i) {
            this.a = i;
            return this;
        }
    }

    public int getHeight() {
        return this.b;
    }

    public int getWidth() {
        return this.a;
    }

    private SplashConfig(Builder builder) {
        this.a = builder.a;
        this.b = builder.b;
    }
}
