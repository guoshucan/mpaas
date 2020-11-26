package ghost.framework.data.Jdbc.Template.plugin.test;

import java.sql.*;

/**
 * package: ghost.framework.data.jdbc.plugin.test
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/22:20:38
 */
public class DBUtils {
    private static String driverClass = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://192.168.1.12:3306/test";
    private static String user = "root";
    private static String pwd = "123456";
    static {
//        ResourceBundle resourceBundle = ResourceBundle.getBundle("dbinfo");
//        driverClass = resourceBundle.getString("driverClass");
//        url = resourceBundle.getString("url");
//        user = resourceBundle.getString("user");
//        pwd = resourceBundle.getString("password");
    }

    public static Connection getConnection() {
        Connection connection = null;
        // 1.注册驱动
        try {
            Class.forName(driverClass);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("数据库驱动异常");
        }

        // 2.得到连接
        try {
            connection = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("数据库连接异常");
        }
        return connection;
    }


    public  static void closeAll(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet!=null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (connection!=null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
