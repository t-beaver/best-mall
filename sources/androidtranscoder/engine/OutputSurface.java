package androidtranscoder.engine;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.view.Surface;

class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "OutputSurface";
    private static final boolean VERBOSE = false;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private boolean mFrameAvailable;
    private Object mFrameSyncObject = new Object();
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private TextureRender mTextureRender;

    public OutputSurface(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException();
        }
        eglSetup(i, i2);
        makeCurrent();
        setup();
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError != 12288) {
            throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
        }
    }

    private void eglSetup(int i, int i2) {
        EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        this.mEGLDisplay = eglGetDisplay;
        if (eglGetDisplay != EGL14.EGL_NO_DISPLAY) {
            int[] iArr = new int[2];
            if (EGL14.eglInitialize(eglGetDisplay, iArr, 0, iArr, 1)) {
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                if (EGL14.eglChooseConfig(this.mEGLDisplay, new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12339, 1, 12344}, 0, eGLConfigArr, 0, 1, new int[1], 0)) {
                    this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, eGLConfigArr[0], EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
                    checkEglError("eglCreateContext");
                    if (this.mEGLContext != null) {
                        this.mEGLSurface = EGL14.eglCreatePbufferSurface(this.mEGLDisplay, eGLConfigArr[0], new int[]{12375, i, 12374, i2, 12344}, 0);
                        checkEglError("eglCreatePbufferSurface");
                        if (this.mEGLSurface == null) {
                            throw new RuntimeException("surface was null");
                        }
                        return;
                    }
                    throw new RuntimeException("null context");
                }
                throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
            }
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        throw new RuntimeException("unable to get EGL14 display");
    }

    private void setup() {
        TextureRender textureRender = new TextureRender();
        this.mTextureRender = textureRender;
        textureRender.surfaceCreated();
        SurfaceTexture surfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture = surfaceTexture;
        surfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    public void awaitNewImage() {
        synchronized (this.mFrameSyncObject) {
            while (!this.mFrameAvailable) {
                try {
                    this.mFrameSyncObject.wait(10000);
                    if (!this.mFrameAvailable) {
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.mFrameAvailable = false;
        }
        this.mTextureRender.checkGlError("before updateTexImage");
        this.mSurfaceTexture.updateTexImage();
    }

    public void changeFragmentShader(String str) {
        this.mTextureRender.changeFragmentShader(str);
    }

    public boolean checkForNewImage(int i) {
        synchronized (this.mFrameSyncObject) {
            do {
                if (!this.mFrameAvailable) {
                    try {
                        this.mFrameSyncObject.wait((long) i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.mFrameAvailable = false;
                    this.mTextureRender.checkGlError("before updateTexImage");
                    this.mSurfaceTexture.updateTexImage();
                    return true;
                }
            } while (this.mFrameAvailable);
            return false;
        }
    }

    public void drawImage() {
        this.mTextureRender.drawFrame(this.mSurfaceTexture);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void makeCurrent() {
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        EGLSurface eGLSurface = this.mEGLSurface;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.mFrameSyncObject) {
            if (!this.mFrameAvailable) {
                this.mFrameAvailable = true;
                this.mFrameSyncObject.notifyAll();
            } else {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
        }
    }

    public void release() {
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(eGLDisplay, this.mEGLSurface);
            EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.mEGLDisplay);
        }
        this.mSurface.release();
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public OutputSurface() {
        setup();
    }
}
