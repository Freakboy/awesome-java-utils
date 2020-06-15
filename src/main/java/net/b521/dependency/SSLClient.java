package net.b521.dependency;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Allen
 * Date: 2019/03/13 11:58
 *
 * @Description: 用于进行Https请求的HttpClient
 */
public class SSLClient extends DefaultHttpClient {

    public SSLClient() throws Exception {

        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        SSLContext ctx = SSLContext.getInstance("TLS");

        // 证书信任管理器（用于https请求）
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        ctx.init(null, new TrustManager[]{tm}, null);
        //允许所有证书
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }


}