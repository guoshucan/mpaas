# ghost-framework-mpaas-project

#https://blog.csdn.net/tomato__/article/details/32697679
连接器配置概览
连接器用于接收网络连接，配置一个连接器需要配置：
 1）连接器的网络参数（例如：端口）；
 2）连接器使用的服务（例如：executors，schedulers）；
 3）为接收连接而初始化和配置协议的连接工厂。
Jetty主要使用的连接器类型为ServerConnector。
标准Jetty发布使用下面的Jetty XML文件创建和配置连接器：
 1）jetty-http.xml
 初始化一个ServerConnector，用于接收HTTP连接（可以被升级到WebSocket连接）。
 2）jetty-https.xml
 初始化一个ServerConnector，用于接收SSL/TLS连接。
 3）example-jetty-spdy.xml
 初始化一个ServerConnector，用于接收SSL连接，连接使用HTTP或者SPDY通信。

构造一个ServerConnector
一个ServerConnector使用的服务在构造时设置，后面不能改变，服务大部分情况下被设置到默认的null或0，这样一个合理的默认值将被采用，因此，大部分情况下仅需要设置Server和连接工厂。在Jetty XML中（即jetty-http.xml中），你能配置：

<New class="org.eclipse.jetty.server.ServerConnector">
  <Arg name="server"><Ref refid="Server" /></Arg>
  <Arg name="factories">
    <Array type="org.eclipse.jetty.server.ConnectionFactory">
      <!-- insert one or more factories here -->
    </Array>
  </Arg>
  <!-- set connector fields here -->
</New>
你能在Javadoc中看到构造ServerConnector的其它参数，但对于几乎所有的部署，默认值就足够了。

网络设置
配置连接器的网络设置需要在它启动之前，调用setters设置。例如，你能在Jetty XML中设置端口：

<New class="org.eclipse.jetty.server.ServerConnector">
  <Arg name="server"><Ref refid="Server" /></Arg>
  <Arg name="factories"><!-- insert one or more factories here --></Arg>
 
  <Set name="port">8080</Set>
</New>
Jetty XML中的值也可以被参数化，以至于他们可以通过属性文件或者命令行传入。下面是使用属性文件的例子：

<New class="org.eclipse.jetty.server.ServerConnector">
  <Arg name="server"><Ref refid="Server" /></Arg>
  <Arg name="factories"><!-- insert one or more factories here --></Arg>
 
  <Set name="port"><Property name="jetty.port" default="8080"/></Set>
</New>
ServerConnector的网络设置包括：
 1）host：连接器绑定的网络接口，可以是IP地址或主机名。如果是null或0.0.0.0，绑定到所有接口；
 2）port：连接器端口，0则表示使用随机端口（通过getLocalPort()选择可用的端口）；
 3）idleTimeout：连接器在空闲状态持续该时间后（单位毫秒）关闭；
 4）defaultProtocol：用于选择ConnectionFactory实例的默认协议名；
 5）stopTimeout：温和的停止一个连接器前等待的时间（毫秒）；
 6）acceptQueueSize：等待处理的连接队列大小，最好保持默认值，除非你有必要的原因去修改它；
 7）reuseAddress：允许Server socket被重绑定，即使在TIME_WAIT状态。通常保持默认值true；
 8）soLingerTime：若值>=0，则设置socket SO_LINGER值，单位毫秒。Jetty尝试温和的关闭所有TCP/IP连接，因此该值不应该被修改，保持默认值-1；

HTTP配置
HttpConfiguration为HTTPChannel提供配置，你能创建1对1的HTTP连接，或者1对n的多路复用SPDY连接。因此一个HTTPConfiguration可以用于HTTP和SPDY连接工厂。为了避免重复配置，标准Jetty发布在jetty.xml中创建一个公用的HttpConfiguration实例，是一个Ref元素，可以在 jetty-http.xml， jetty-https.xml和example-jetty-spdy.xml中使用它。
一个通常的HttpConfiguration配置如下：

<New id="httpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
  <Set name="secureScheme">https</Set>
  <Set name="securePort"><Property name="jetty.tls.port" default="8443" /></Set>
  <Set name="outputBufferSize">32768</Set>
  <Set name="requestHeaderSize">8192</Set>
  <Set name="responseHeaderSize">8192</Set>
 
  <Call name="addCustomizer">
    <Arg><New class="org.eclipse.jetty.server.ForwardedRequestCustomizer"/></Arg>
  </Call>
</New>
这个例子增加了一个ForwardedRequestCustomizer处理X-Forward-For和相关的协议头。jetty-https.xml中可以通过ID使用该实例：

<Call name="addConnector">
  <Arg>
    <New class="org.eclipse.jetty.server.ServerConnector">
      <Arg name="server"><Ref refid="Server" /></Arg>
      <Arg name="factories">
        <Array type="org.eclipse.jetty.server.ConnectionFactory">
          <Item>
            <New class="org.eclipse.jetty.server.HttpConnectionFactory">
              <Arg name="config"><Ref refid="httpConfig" /></Arg>
            </New>
          </Item>
        </Array>
      </Arg>
      <!-- ... -->
    </New>
  </Arg>
</Call>
为SSL（在jetty-https.xml和jetty-spdy.xml中），"httpConfig"实例能被用于作为创建SSL特定配置的基础：

<New id="tlsHttpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
  <Arg><Ref refid="httpConfig"/></Arg>
  <Call name="addCustomizer">
    <Arg><New class="org.eclipse.jetty.server.SecureRequestCustomizer"/></Arg>
  </Call>
</New>
这里使用SecureRequestCustomizer，增加SSL Session IDs和认证信息作为请求属性。

SSL上下文配置
SSL/TLS连接器用于HTTPS和SPDY，要求认证信息用于建立安全连接。在下面的“配置SSL”节会做详细论述。

配置连接工厂
在Jetty发布中，最简单的连接工厂的例子是jetty-http.xml：

<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
 
<!-- ============================================================= -->
<!-- Configure the Jetty Server instance with an ID "Server"       -->
<!-- by adding a HTTP connector.                                   -->
<!-- This configuration must be used in conjunction with jetty.xml -->
<!-- ============================================================= -->
<Configure id="Server" class="org.eclipse.jetty.server.Server">
 
  <!-- =========================================================== -->
  <!-- Add a HTTP Connector.                                       -->
  <!-- Configure an o.e.j.server.ServerConnector with a single     -->
  <!-- HttpConnectionFactory instance using the common httpConfig  -->
  <!-- instance defined in jetty.xml                               -->
  <!--                                                             -->
  <!-- Consult the javadoc of o.e.j.server.ServerConnector and     -->
  <!-- o.e.j.server.HttpConnectionFactory for all configuration    -->
  <!-- that may be set here.                                       -->
  <!-- =========================================================== -->
  <Call name="addConnector">
    <Arg>
      <New class="org.eclipse.jetty.server.ServerConnector">
        <Arg name="server"><Ref refid="Server" /></Arg>
        <Arg name="factories">
          <Array type="org.eclipse.jetty.server.ConnectionFactory">
            <Item>
              <New class="org.eclipse.jetty.server.HttpConnectionFactory">
                <Arg name="config"><Ref refid="httpConfig" /></Arg>
              </New>
            </Item>
          </Array>
        </Arg>
        <Set name="host"><Property name="jetty.host" /></Set>
        <Set name="port"><Property name="jetty.port" default="80" /></Set>
        <Set name="idleTimeout"><Property name="http.timeout" default="30000"/></Set>
        <Set name="soLingerTime"><Property name="http.soLingerTime" default="-1"/></Set>
      </New>
    </Arg>
  </Call>
 
</Configure>
这里仅有一个ConnectionFactory被注入，当一个新的连接被接收时，HttpConnectionFactory将创建一个HttpConnection。
一个更复杂的例子是jetty-spdy.xml，配置了多个连接工厂：

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
 
<Configure id="Server" class="org.eclipse.jetty.server.Server">
 
    <New id="sslContextFactory" class="org.eclipse.jetty.util.ssl.SslContextFactory">
        <Set name="keyStorePath">src/main/resources/keystore.jks</Set>
        <Set name="keyStorePassword">storepwd</Set>
        <Set name="trustStorePath">src/main/resources/truststore.jks</Set>
        <Set name="trustStorePassword">storepwd</Set>
        <Set name="protocol">TLSv1</Set>
    </New>
 
    <New id="tlsHttpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
        <Arg>
            <New id="httpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
                <Set name="secureScheme">https</Set>
                <Set name="securePort">
                    <Property name="jetty.tls.port" default="8443"/>
                </Set>
                <Set name="outputBufferSize">32768</Set>
                <Set name="requestHeaderSize">8192</Set>
                <Set name="responseHeaderSize">8192</Set>
 
                <!-- Uncomment to enable handling of X-Forwarded- style headers
                <Call name="addCustomizer">
                    <Arg><New class="org.eclipse.jetty.server.ForwardedRequestCustomizer"/></Arg>
                </Call>
                -->
            </New>
        </Arg>
        <Call name="addCustomizer">
            <Arg>
                <New class="org.eclipse.jetty.server.SecureRequestCustomizer"/>
            </Arg>
        </Call>
    </New>
 
    <New id="pushStrategy" class="org.eclipse.jetty.spdy.server.http.ReferrerPushStrategy">
        <!-- Uncomment to blacklist browsers for this push strategy. If one of the blacklisted Strings occurs in the
             user-agent header sent by the client, push will be disabled for this browser. This is case insensitive" -->
        <!--
        <Set name="UserAgentBlacklist">
            <Array type="String">
                <Item>.*(?i)firefox/14.*</Item>
                <Item>.*(?i)firefox/15.*</Item>
                <Item>.*(?i)firefox/16.*</Item>
            </Array>
        </Set>
        -->
 
        <!-- Uncomment to override default file extensions to push -->
        <!--
        <Set name="PushRegexps">
            <Array type="String">
               <Item>.*\.css</Item>
               <Item>.*\.js</Item>
               <Item>.*\.png</Item>
               <Item>.*\.jpg</Item>
               <Item>.*\.gif</Item>
           </Array>
        </Set>
        -->
        <Set name="referrerPushPeriod">5000</Set>
        <Set name="maxAssociatedResources">32</Set>
    </New>
 
    <Call id="sslConnector" name="addConnector">
        <Arg>
            <New class="org.eclipse.jetty.server.ServerConnector">
                <Arg name="server"><Ref refid="Server"/></Arg>
                <Arg name="factories">
                    <Array type="org.eclipse.jetty.server.ConnectionFactory">
 
                        <!-- SSL Connection factory with NPN as next protocol -->
                        <Item>
                            <New class="org.eclipse.jetty.server.SslConnectionFactory">
                                <Arg name="next">npn</Arg>
                                <Arg name="sslContextFactory">
                                    <Ref refid="sslContextFactory"/>
                                </Arg>
                            </New>
                        </Item>
 
                        <!-- NPN Connection factory with HTTP as default protocol -->
                        <Item>
                            <New class="org.eclipse.jetty.spdy.server.NPNServerConnectionFactory">
                                <Arg name="protocols">
                                    <Array type="String">
                                        <Item>spdy/3</Item>
                                        <Item>spdy/2</Item>
                                        <Item>http/1.1</Item>
                                    </Array>
                                </Arg>
                                <Set name="defaultProtocol">http/1.1</Set>
                            </New>
                        </Item>
 
                        <!-- SPDY/3 Connection factory -->
                        <Item>
                            <New class="org.eclipse.jetty.spdy.server.http.HTTPSPDYServerConnectionFactory">
                                <Arg name="version" type="int">3</Arg>
                                <Arg name="config">
                                    <Ref refid="tlsHttpConfig"/>
                                </Arg>
                                <Arg name="pushStrategy">
                                    <Ref refid="pushStrategy"/>
                                </Arg>
                            </New>
                        </Item>
 
                        <!-- SPDY/2 Connection factory -->
                        <Item>
                            <New class="org.eclipse.jetty.spdy.server.http.HTTPSPDYServerConnectionFactory">
                                <Arg name="version" type="int">2</Arg>
                                <Arg name="config">
                                    <Ref refid="tlsHttpConfig"/>
                                </Arg>
                            </New>
                        </Item>
 
                        <!-- HTTP Connection factory -->
                        <Item>
                            <New class="org.eclipse.jetty.server.HttpConnectionFactory">
                                <Arg name="config">
                                    <Ref refid="tlsHttpConfig"/>
                                </Arg>
                            </New>
                        </Item>
                    </Array>
                </Arg>
 
                <Set name="port">8443</Set>
            </New>
        </Arg>
    </Call>
 
</Configure>
在这个例子中有5个连接工厂被创建，通过他们的协议名连接在一起：
 1）"SSL-npn"
 被第一个连接工厂标识的默认协议，这里配置的连接工厂是SslConnectionFactory，使用"npn"作为下一个协议。因此接收端点与SslConnection实例相关联，SslConnection实例与"npn"连接工厂创建的NextProtoNegoServerConnection实例构成处理链。
 2）"npn"
 这里是NPNServerConnectionFactory，链接到SslConnectionFactory。NPN连接与客户端协商协议的类型，然后根据协议名称找到对应工厂（下面3个中的一个）并创建一个连接来替换NPN连接。如果NPN不被支持，defaultProtocol被配置为“http/1.1”。
 3）"spdy/3"
 如果SPDY版本是3，则NPN连接使用该工厂。
 4）"spdy/2"
 如果SPDY版本是2，则NPN连接使用该工厂。
 5）"http/1.1"
 如果HTTP版本是1.1，或者如果NPN不被支持，NPN连接用该工厂。注意HTTP/1.1也能处理HTTP/1.0。

配置SSL
配置SSL是一个复杂的过程，关系到keys、certificates、protocols和formats，对这些有一个基本的了解将是很有帮助的，下面的连接提供一些资料供参考：
 1）Certificates
  SSL Certificates HOWTO（http://en.tldp.org/HOWTO/SSL%20Certificates%20HOWTO）
  Mindprod Java Glossary: Certificates（http://mindprod.com/jgloss/certificate.html）
 2）Keytool
  Keytool for Unix（http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/keytool.html）
  Keytool for Windows（http://docs.oracle.com/javase/7/docs/technotes/tools/windows/keytool.html）
 3）Other tools
  IBM Keyman（https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=6fb00498-f6ea-4f65-bf0c-adc5bd0c5fcc）
 4）OpenSSL
  OpenSSL HOWTO（http://www.openssl.org/docs/HOWTO/）
  OpenSSL FAQ（http://www.openssl.org/support/faq.html）

OpenSSL vs. Keytool
为了测试，与JDK绑定的keytool工具提供了最简单的方式产生你需要的key和cetificate。
你也能使用OpenSSL工具产生key和certificate，或者转换那些你用于Apache或者其他servers的。由于Apache和其他servers都是用OpenSSL工具套装产生和操作keys和certificates，你也许已经有了一些通过OpenSSL创建的keys和certificates，或者你也许也会更喜欢OpenSSL产生的格式。
如果你想和Jetty或者一个web server（例如Apache，不是用Java实现）使用同样的cetificate，你也许更喜欢用OpenSSL产生你的私有key和certificate。

为Jetty配置SSL
为了在Jetty配置SSL，需要执行下面的任务：
 1）产生key值对和certificates；
 2）请求一个受信任的certificate；
 3）加载keys和certificates；
 4）配置SslContextFactory。
下面将一一介绍。

产生Key值对和certificate
最简单的产生keys和certificates的方式是使用JDK自带的keytool，他产生的keys和certificates直接进入keystore。
如果你已经有了keys和certificates，你可以加载它们进入JSSE keystore（看下面“加载keys和certificates”），或者替换已经到期的certificate。
下面的例子将仅产生基本的keys和certificates，你应该阅读工具的详细手册，如果你想指定：
 1）key的大小；
 2）certificate到期时间；
 3）备用的安全提供商。

使用JDK的keytool产生keys和certificates
下面是使用keystore产生keys和certificates的例子：

$ keytool -keystore keystore -alias jetty -genkey -keyalg RSA
命令行将提示你输入信息，需要注意的是对"first and last name"提示的回答必须提供server的有效的主机名，下面是一个例子：

$ keytool -keystore keystore -alias jetty -genkey -keyalg RSA
 Enter keystore password:  password
 What is your first and last name?
   [Unknown]:  jetty.eclipse.org
 What is the name of your organizational unit?
   [Unknown]:  Jetty
 What is the name of your organization?
   [Unknown]:  Mort Bay Consulting Pty. Ltd.
 What is the name of your City or Locality?
   [Unknown]:
 What is the name of your State or Province?
   [Unknown]:
 What is the two-letter country code for this unit?
   [Unknown]:
 Is CN=jetty.eclipse.org, OU=Jetty, O=Mort Bay Consulting Pty. Ltd.,
 L=Unknown, ST=Unknown, C=Unknown correct?
   [no]:  yes

 Enter key password for <jetty>
         (RETURN if same as keystore password):
 $
使用OpenSSL产生Keys和Certificates
下面的命令在文件jetty.key中产生一个key值对：

$ openssl genrsa -des3 -out jetty.key
下面的命令为key产生一个certificate，存入文件jetty.crt：

$ openssl req -new -x509 -key jetty.key -out jetty.crt
下面是一个完整的例子，注意“Common Name”要求提供server的完整的主机名：

$ openssl genrsa -des3 -out jetty.key
 Generating RSA private key, 512 bit long modulus
 ...........................++++++++++++
 ..++++++++++++
 e is 65537 (0x10001)
 Enter pass phrase for jetty.key:
 Verifying - Enter pass phrase for jetty.key:

$ openssl req -new -x509 -key jetty.key -out jetty.crt
 Enter pass phrase for jetty.key:
 You are about to be asked to enter information to be incorporated
 into your certificate request.
 What you are about to enter is what is called a Distinguished Name or a DN.
 There are quite a few fields but you can leave some blank
 For some fields there is a default value,
 If you enter '.', the field is left blank.
 -----
 Country Name (2 letter code) [AU]:.
 State or Province Name (full name) [Some-State]:.
 Locality Name (eg, city) []:.
 Organization Name (eg, company) [Internet Widgets Pty Ltd]:Mort Bay Consulting Pty. Ltd.
 Organizational Unit Name (eg, section) []:Jetty
 Common Name (eg, YOUR name) []:jetty.eclipse.org
 Email Address []:

$
其它方式获取keys和certificates
你也可以通过其它方式获取keys和certificates。

请求一个可信的certificate
使用keytool和OpenSSL产生的keys和certificates已经足够运行一个SSL连接器。然而浏览器将不信任你产生的certificate，它将提示用户certificate不可信。
为了获取一个大部分浏览器都信任的certificate，你需要请求一个众所周知的certificate authority（CA）为你的key/certificate签名。这样的CA包括：AddTrust，Entrust，GeoTrust，RSA Data Security，Thawte，VISA，ValiCert，Verisign，和beTRUSTed, 等等。每个CA都有它自己的指导手册（查看JSSE或者OpenSSL相关章节），但是所有的CA都要求产生一个certificate signing request（CSR）。

用keytool产生CSR
下面的命令为一个已经在keystore中的key/cert产生jetty.csr文件：

$ keytool -certreq -alias jetty -keystore keystore -file jetty.csr
用OpenSSL产生CSR
下面的命令为jetty.key中的key产生jetty.csr文件：

$ openssl req -new -key jetty.key -out jetty.csr
注意这个命令仅用jetty.key中的key，没有jetty.crt中的certificate。你需要输入certificate的细节。

加载keys和certificates
一旦CA给了你一个certificate，或者如果你没有使用keytool产生你自己的certificate，你需要加载它到JSSE keystore。

使用keytool加载certificates
你能使用keytool加载一个PEM格式的certificate到keystore，PEM格式是一个证书的文本编码，它通过OpenSSL产生，被一些CA返回给你。下面PEM文件的一个例子：

jetty.crt
-----BEGIN CERTIFICATE-----
MIICSDCCAfKgAwIBAgIBADANBgkqhkiG9w0BAQQFADBUMSYwJAYDVQQKEx1Nb3J0
IEJheSBDb25zdWx0aW5nIFB0eS4gTHRkLjEOMAwGA1UECxMFSmV0dHkxGjAYBgNV
BAMTEWpldHR5Lm1vcnRiYXkub3JnMB4XDTAzMDQwNjEzMTk1MFoXDTAzMDUwNjEz
MTk1MFowVDEmMCQGA1UEChMdTW9ydCBCYXkgQ29uc3VsdGluZyBQdHkuIEx0ZC4x
DjAMBgNVBAsTBUpldHR5MRowGAYDVQQDExFqZXR0eS5tb3J0YmF5Lm9yZzBcMA0G
CSqGSIb3DQEBAQUAA0sAMEgCQQC5V4oZeVdhdhHqa9L2/ZnKySPWUqqy81riNfAJ
7uALW0kEv/LtlG34dOOcVVt/PK8/bU4dlolnJx1SpiMZbKsFAgMBAAGjga4wgasw
HQYDVR0OBBYEFFV1gbB1XRvUx1UofmifQJS/MCYwMHwGA1UdIwR1MHOAFFV1gbB1
XRvUx1UofmifQJS/MCYwoVikVjBUMSYwJAYDVQQKEx1Nb3J0IEJheSBDb25zdWx0
aW5nIFB0eS4gTHRkLjEOMAwGA1UECxMFSmV0dHkxGjAYBgNVBAMTEWpldHR5Lm1v
cnRiYXkub3JnggEAMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEEBQADQQA6NkaV
OtXzP4ayzBcgK/qSCmF44jdcARmrXhiXUcXzjxsLjSJeYPJojhUdC2LQKy+p4ki8
Rcz6oCRvCGCe5kDB
-----END CERTIFICATE-----
下面的命令加载一个PEM编码的证书（jetty.crt）到JSSE keystore：

$ keytool -keystore keystore -import -alias jetty -file jetty.crt -trustcacerts
如果你从CA收到的证书不是kertool理解的格式，你能使用openssl命令转换格式：

$ openssl x509 -in jetty.der -inform DER -outform PEM -out jetty.crt
通过PKCS12加载keys和certificates
如果你的key和证书在不同的文件中，你需要合并它们到PKCS12格式，然后加载到keystore。证书可以是你自己产生的，或者从CA得到的。
下面的OpenSSL命令用于合并jetty.key中的key和jetty.crt中的证书到jetty.pkcs12文件：

$ openssl pkcs12 -inkey jetty.key -in jetty.crt -export -out jetty.pkcs12
如果你有一个证书链，使用下面的方式生成PKCS12：

$ cat example.crt intermediate.crt [intermediate2.crt] ... rootCA.crt > cert-chain.txt
$ openssl pkcs12 -export -inkey example.key -in cert-chain.txt -out example.pkcs12
证书的命令必须是从server到rootCA，按照RFC2246中第7.4.2描述。
OpenSSL请求一个导出的密码，下一步的工作需要一个非空的密码。接下来使用keytool加载PKCS12文件进入JSSE keystore：

$ keytool -importkeystore -srckeystore jetty.pkcs12 -srcstoretype PKCS12 -destkeystore keystore
配置SslContextFactory
为了让SSL证书配置到Jetty，需要注入SslContextFactory对象的一个实例到连接器的SslConnectionFactory，在Jetty发布中SslConnectionFactory在jetty-https.xml和jetty-spdy.xml中配置。由于SPDY也能处理HTTPS，因此你通常需要配置两个配置文件中的一个，但也可以使用命令行或者编辑start.ini文件。
下面的配置创建一个SslContextFactory的实例，ID为"sslContextFactory"：

<New id="sslContextFactory" class="org.eclipse.jetty.util.ssl.SslContextFactory">
  <Set name="KeyStorePath"><Property name="jetty.home" default="." />/etc/keystore</Set>
  <Set name="KeyStorePassword">OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4</Set>
  <Set name="KeyManagerPassword">OBF:1u2u1wml1z7s1z7a1wnl1u2g</Set>
  <Set name="TrustStorePath"><Property name="jetty.home" default="." />/etc/keystore</Set>
  <Set name="TrustStorePassword">OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4</Set>
</New>
这里使用了随jetty发布的keystore。为了使用你自己的keystore，你需要更新至少两个信息：
 1）KeyStorePath：你能用你自己的keystore替换这里提供的keystore，或者改变配置指向新的文件。注意由于keystore是非常重要的安全信息，因此keystore文件所在的文件夹应该有严格的许可限制。
 2）KeyStorePassword：keystore的密码可以用普通文本保存，也可以做一些保护，它能被扰乱，通过使用Password class。
Truststore通常设置为相同的keystore，在验证客户端证书时使用。
keyManagerPassword作为password参数传递到KeyManagerFactory.init(...)，如果没有keymanagerpassword，那么用keystorepassword代替，如果没有trustmanager，那么keystore被用于truststore，keystorepassword被用于TrustStorePassword。
不需要创建一个新的SslContextFactory实例，因为在jetty-ssl.xml中已经存在。在jetty-ssl.xml中编辑路径和密码信息，并确保你增加了下面的行在start.ini中，在jetty.dump.stop=之后添加：

etc/jetty-ssl.xml
etc/jetty-https.xml
你也可以选择在jetty-https.xml中配置https端口：

<Set name="port">8443</Set>

或者通过命令行设置，例如：

$ java -jar start.jar https.port=8443
Disabling/Enabling指定的密码组
例如为了避免BEAST攻击，需要配置一组特定的密码组。这能通过SslContext.setIncludeCipherSuites(java.lang.String...)或者通过SslContext.setExcludeCipherSuites(java.lang.String...)实现。
setIncludeCipherSuites和setExcludeCipherSuites都能使用在JDK中使用的密码组名，也可以使用正则表达式。
下面是一些例子怎么配置RC4密码组，他们都能保护server免于BEAST攻击。
包括一套优先的密码组：

<Set name="IncludeCipherSuites">
  <Array type="String">
      <Item>TLS_RSA_WITH_RC4_128_MD5</Item>
      <Item>TLS_RSA_WITH_RC4_128_SHA</Item>
      <Item>TLS_ECDHE_RSA_WITH_RC4_128_SHA</Item>
  </Array>
</Set>
通过使用正则表达式包括所有RC4密码组：

<Set name="IncludeCipherSuites">
  <Array type="String">
      <Item>.*RC4.*</Item>
  </Array>
</Set>
排除所有非RC4密码组：

<Set name="ExcludeCipherSuites">
  <Array type="String">
    <Item>SSL_RSA_WITH_DES_CBC_SHA</Item>
    <Item>SSL_DHE_RSA_WITH_DES_CBC_SHA</Item>
    <Item>SSL_DHE_DSS_WITH_DES_CBC_SHA</Item>
    <Item>SSL_RSA_EXPORT_WITH_RC4_40_MD5</Item>
    <Item>SSL_RSA_EXPORT_WITH_DES40_CBC_SHA</Item>
    <Item>SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA</Item>
    <Item>SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA</Item>
    <Item>TLS_DHE_RSA_WITH_AES_128_CBC_SHA256</Item>
    <Item>TLS_RSA_WITH_AES_128_CBC_SHA</Item>
    <Item>TLS_DHE_RSA_WITH_AES_128_CBC_SHA</Item>
    <Item>TLS_RSA_WITH_AES_128_CBC_SHA256</Item>
    <Item>TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA</Item>
    <Item>TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256</Item>
    <Item>TLS_RSA_WITH_3DES_EDE_CBC_SHA</Item>
    <Item>TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA</Item>
    <Item>TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA</Item>
    <Item>RSA_WITH_3DES_EDE_CBC_SHA</Item>
  </Array>
</Set>
注意你应该优先选择使用正则表达式的IncludeCipherSuites，这样更加有利于扩展。

配置SSL连接器和端口
上面创建的SslContextFactory实例被注入到SslConnectionFactory实例，当接收网络连接的时候将被依次注入ServerConnector实例。例如下面jetty-https.xml中的例子：

<Call id="sslConnector" name="addConnector">
  <Arg>
    <New class="org.eclipse.jetty.server.ServerConnector">
      <Arg name="server"><Ref refid="Server" /></Arg>
        <Arg name="factories">
          <Array type="org.eclipse.jetty.server.ConnectionFactory">
            <Item>
              <New class="org.eclipse.jetty.server.SslConnectionFactory">
                <Arg name="next">http/1.1</Arg>
                <Arg name="sslContextFactory"><Ref refid="sslContextFactory"/></Arg>
              </New>
            </Item>
            <Item>
              <New class="org.eclipse.jetty.server.HttpConnectionFactory">
                <Arg name="config"><Ref refid="tlsHttpConfig"/></Arg>
              </New>
            </Item>
          </Array>
        </Arg>
        <Set name="host"><Property name="jetty.host" /></Set>
        <Set name="port"><Property name="jetty.tls.port" default="8443" /></Set>
        <Set name="idleTimeout">30000</Set>
      </New>
  </Arg>
</Call>
注意SSL连接器端口被直接设置在ServerConnector实例。

更新证书
如果你正在更新你的配置使用一个新的证书（可能由于旧的证书已经过期），你仅需要按照“加载keys和certificates”节描述的过程加载新的证书即可。如果你使用PKCS12方法导入原始的key和certificate，应该使用别名“1”而不是“jetty”，因为那是PKCS12处理进入keystore的别名。
————————————————
版权声明：本文为CSDN博主「tomato__」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/tomato__/article/details/32697679