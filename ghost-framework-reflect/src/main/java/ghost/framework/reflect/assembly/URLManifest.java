package ghost.framework.reflect.assembly;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:包程序集
 * @Date: 1:40 2019-06-15
 */
public class URLManifest extends JarManifest {
    private URL url;

    @Override
    public int hashCode() {
        return super.hashCode() + this.url.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o) && ((URLManifest) o).url.equals(this.url)) {
            return true;
        }
        return false;
    }

    public URLManifest(InputStream is) throws IOException {
        super(is);
    }

    public URLManifest(Manifest manifest, URL url) {
        super(manifest);
        this.url = url;
    }

    public URLManifest(Manifest manifest) {
        super(manifest);
    }

    public URLManifest() {
        super();
    }
}
