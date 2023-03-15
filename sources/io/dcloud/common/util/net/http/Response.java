package io.dcloud.common.util.net.http;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Response implements Runnable {
    private static final byte CR = 13;
    private static final byte[] CRLF = {CR, LF};
    private static final byte LF = 10;
    final int BUFFER_SIZE = 10240;
    AbsMgr mNetMgr = null;
    Socket mSocket = null;
    String mUrl = null;

    public Response(Socket socket, AbsMgr absMgr) {
        this.mSocket = socket;
        this.mNetMgr = absMgr;
        new Thread(this).start();
    }

    private void addResponseHead(long j, OutputStream outputStream) throws IOException {
        writeHeader(outputStream, "HTTP/1.1 200 OK");
        writeHeader(outputStream, "Content-Type: " + PdrUtil.getMimeType(this.mUrl));
        writeHeader(outputStream, "Access-Control-Allow-Origin: *");
        writeHeader(outputStream, "Access-Control-Allow-Headers: *");
        writeHeader(outputStream, "Content-Length: " + j);
        outputStream.write(CRLF);
        outputStream.flush();
    }

    private void write(OutputStream outputStream, String str) throws IOException {
        outputStream.write(str.getBytes());
    }

    private void writeRequest(OutputStream outputStream, String str) throws IOException {
        outputStream.write("GET /index.html HTTP/1.1".getBytes());
        byte[] bArr = CRLF;
        outputStream.write(bArr);
        outputStream.write(("Host: " + str).getBytes());
        outputStream.write(bArr);
        outputStream.write(bArr);
        outputStream.flush();
    }

    public void run() {
        InputStream inputStream;
        InputStream inputStream2;
        InputStream inputStream3;
        InputStream inputStream4;
        InputStream inputStream5;
        OutputStream outputStream;
        InputStream inputStream6;
        InputStream inputStream7;
        OutputStream outputStream2 = null;
        try {
            InputStream inputStream8 = this.mSocket.getInputStream();
            try {
                Request request = new Request(inputStream8);
                request.parse();
                String data = request.getData();
                if (data.startsWith(AbsoluteConst.SOCKET_NATIVE_COMMAND)) {
                    this.mNetMgr.processEvent(IMgr.MgrType.AppMgr, 7, data);
                    inputStream4 = null;
                } else if (data.startsWith(AbsoluteConst.SOCKET_CONNECTION)) {
                    String substring = data.substring(6);
                    OutputStream outputStream3 = this.mSocket.getOutputStream();
                    try {
                        String str = PdrUtil.isEquals(substring, AbsoluteConst.SOCKET_CONN_REQUEST_ROOT_PATH) ? DeviceInfo.sDeviceRootDir : "";
                        Logger.d("miniserver", substring, str);
                        outputStream3.write(str.getBytes());
                        inputStream4 = null;
                        outputStream2 = outputStream3;
                    } catch (IOException e) {
                        inputStream5 = inputStream8;
                        inputStream3 = null;
                        outputStream = outputStream3;
                        e = e;
                        inputStream = inputStream5;
                        try {
                            e.printStackTrace();
                            try {
                                IOUtil.close(inputStream);
                                IOUtil.close(inputStream2);
                                IOUtil.close(outputStream2);
                                this.mSocket.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                return;
                            }
                        } catch (Throwable th) {
                            th = th;
                            try {
                                IOUtil.close(inputStream);
                                IOUtil.close(inputStream2);
                                IOUtil.close(outputStream2);
                                this.mSocket.close();
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        inputStream6 = inputStream8;
                        inputStream2 = null;
                        outputStream2 = outputStream3;
                        th = th2;
                        inputStream = inputStream6;
                        IOUtil.close(inputStream);
                        IOUtil.close(inputStream2);
                        IOUtil.close(outputStream2);
                        this.mSocket.close();
                        throw th;
                    }
                } else {
                    String uri = request.getUri();
                    this.mUrl = uri;
                    if (uri == null) {
                        try {
                            IOUtil.close(inputStream8);
                            IOUtil.close((InputStream) null);
                            IOUtil.close((OutputStream) null);
                            this.mSocket.close();
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    } else {
                        OutputStream outputStream4 = this.mSocket.getOutputStream();
                        try {
                            byte[] bArr = new byte[10240];
                            if (this.mUrl.startsWith(AbsoluteConst.MINI_SERVER_BASE_RES)) {
                                inputStream7 = PlatformUtil.getResInputStream("res/" + this.mUrl.substring(5));
                            } else {
                                inputStream7 = (InputStream) this.mNetMgr.processEvent(IMgr.MgrType.AppMgr, 2, this.mUrl);
                            }
                            if (inputStream7 != null) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                while (true) {
                                    int read = inputStream7.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    byteArrayOutputStream.write(bArr, 0, read);
                                }
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                addResponseHead((long) byteArray.length, outputStream4);
                                outputStream4.write(byteArray);
                            } else {
                                Logger.i("miniserver", "error url=" + this.mUrl);
                                outputStream4.write("HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: 23\r\n\r\n<h1>File Not Found</h1>".getBytes());
                            }
                            OutputStream outputStream5 = outputStream4;
                            inputStream4 = inputStream7;
                            outputStream2 = outputStream5;
                        } catch (IOException e5) {
                            e = e5;
                            inputStream5 = inputStream8;
                            inputStream3 = null;
                            outputStream = outputStream4;
                            inputStream = inputStream5;
                            e.printStackTrace();
                            IOUtil.close(inputStream);
                            IOUtil.close(inputStream2);
                            IOUtil.close(outputStream2);
                            this.mSocket.close();
                        } catch (Throwable th3) {
                            th = th3;
                            inputStream6 = inputStream8;
                            inputStream2 = null;
                            outputStream2 = outputStream4;
                            inputStream = inputStream6;
                            IOUtil.close(inputStream);
                            IOUtil.close(inputStream2);
                            IOUtil.close(outputStream2);
                            this.mSocket.close();
                            throw th;
                        }
                    }
                }
                try {
                    IOUtil.close(inputStream8);
                    IOUtil.close(inputStream4);
                    IOUtil.close(outputStream2);
                    this.mSocket.close();
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
            } catch (IOException e7) {
                e = e7;
                inputStream = inputStream8;
                inputStream3 = null;
                e.printStackTrace();
                IOUtil.close(inputStream);
                IOUtil.close(inputStream2);
                IOUtil.close(outputStream2);
                this.mSocket.close();
            } catch (Throwable th4) {
                th = th4;
                inputStream = inputStream8;
                inputStream2 = null;
                IOUtil.close(inputStream);
                IOUtil.close(inputStream2);
                IOUtil.close(outputStream2);
                this.mSocket.close();
                throw th;
            }
        } catch (IOException e8) {
            inputStream3 = null;
            e = e8;
            inputStream = null;
            e.printStackTrace();
            IOUtil.close(inputStream);
            IOUtil.close(inputStream2);
            IOUtil.close(outputStream2);
            this.mSocket.close();
        } catch (Throwable th5) {
            inputStream2 = null;
            th = th5;
            inputStream = null;
            IOUtil.close(inputStream);
            IOUtil.close(inputStream2);
            IOUtil.close(outputStream2);
            this.mSocket.close();
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public void writeHeader(OutputStream outputStream, String str) throws IOException {
        write(outputStream, str);
        outputStream.write(CRLF);
    }
}
