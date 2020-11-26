package ghost.framework.context.event.scan;

import ghost.framework.context.bean.factory.scan.IScanResourceBeanTargetHandle;
import ghost.framework.maven.FileArtifact;
import ghost.framework.util.Assert;

/**
 * package: ghost.framework.core.event.scan
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:13:50
 */
public class ScanResourceEventTargetHandle<O, T> extends ScanEventTargetHandle<O, T> implements IScanResourceBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param artifact 设置扫描资源包信息
     */
    public ScanResourceEventTargetHandle(O owner, T target, FileArtifact artifact) {
        super(owner, target);
        this.artifact = artifact;
        Assert.notNull(owner, "ScanResourceEventTargetHandle is owner null error");
        Assert.notNull(target, "ScanResourceEventTargetHandle is target null error");
        Assert.notNull(artifact, "ScanResourceEventTargetHandle is artifact null error");
    }

    /**
     * 扫描资源包信息
     */
    private FileArtifact artifact;

    /**
     * 获取扫描资源包信息
     * @return
     */
    @Override
    public FileArtifact getArtifact() {
        return artifact;
    }
}