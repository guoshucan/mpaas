package ghost.framework.git.module;
import ghost.framework.beans.annotation.Order;
import ghost.framework.beans.execute.annotation.Init;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:21 2019/11/24
 */
@Order
class GitMain {
    public String remotePath = "http://user@10.1.2.1:8080/project.git";//远程库路径
    public String localPath = "D:\\project\\";//下载已有仓库到本地路径
    public String initPath = "D:\\test\\";//本地路径新建

    @Init
    private void main() {
        System.out.println(this.getClass().getName());
        try {
            //设置远程服务器上的用户名和密码
            UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                    UsernamePasswordCredentialsProvider("username", "password");

            //克隆代码库命令
            CloneCommand cloneCommand = Git.cloneRepository();

            Git git = cloneCommand.setURI(remotePath) //设置远程URI
                    .setBranch("master") //设置clone下来的分支
                    .setDirectory(new File(localPath)) //设置下载存放路径
                    .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                    .call();

            System.out.print(git.tag());
        } catch (Exception e) {

        }
    }
}