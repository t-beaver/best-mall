package io.dcloud.feature.audio.recorder;

public abstract class AbsRecorder {
    public abstract void pause();

    public abstract void release();

    public abstract void resume();

    public abstract void start();

    public abstract void stop();
}
