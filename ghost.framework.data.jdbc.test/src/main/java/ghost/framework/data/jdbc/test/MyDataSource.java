package ghost.framework.data.jdbc.test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * package: ghost.framework.data.jdbc.plugin.test
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/22:20:38
 */
public class MyDataSource  implements DataSource {

    //模拟就是一个连接池 ，默认放10个
    private static LinkedList<Connection> pool = new LinkedList<Connection>();

    static {
        for (int i = 0; i < 10; i++) {
            Connection connection = DBUtils.getConnection();
            pool.add(connection);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        // TODO Auto-generated method stub

        if (pool != null && pool.size() > 0) {
            System.out.println("=====getConnection=====");
            Connection connection = pool.removeFirst();
            return connection;
        }
        return null;
    }

    /**
     * 连接使用完毕直接放回到连接池
     */
    public void release(Connection connection) {
        try {
            System.out.println("=====release=====");
            pool.addLast(connection);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}