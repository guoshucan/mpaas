package ghost.framework.context.bean.factory.scan;

import ghost.framework.maven.FileArtifact;

/**
 * package: ghost.framework.context.bean.factory.scan
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描资源事件目标处理接口
 * @Date: 2020/2/23:13:33
 */
public interface IScanResourceBeanTargetHandle<O, T> extends IScanBeanTargetHandle<O, T> {
    /**
     * 获取扫描资源包信息
     * @return
     */
    FileArtifact getArtifact();
}