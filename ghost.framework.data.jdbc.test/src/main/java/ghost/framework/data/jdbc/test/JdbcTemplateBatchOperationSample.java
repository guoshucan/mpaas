package ghost.framework.data.jdbc.test;

import ghost.framework.data.jdbc.core.BatchPreparedStatementSetter;
import ghost.framework.data.jdbc.template.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * package: ghost.framework.data.jdbc.plugin.test
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/22:20:32
 */
public class JdbcTemplateBatchOperationSample {
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

    public static void main(String[] args) {
        MyDataSource  ds = new MyDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        //准备环境
        jdbcTemplate.execute("drop table if exists user  ");
        jdbcTemplate.execute("create table user(id int auto_increment primary key, name varchar(40))");
        //这里面我们需要批量插入多个用户，所以我们这里先定义了待插入用户的列表，
        // 这个列表可以方便实现BatchPreparedStatementSetter接口，比如:获取
        // 批操作的大小 <==> uses.size()即可
        List<String> users = Arrays.asList("Alice", "Bob");
        jdbcTemplate.batchUpdate("insert into user(name) values(?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, users.get(i));
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });

        Long count = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        System.out.println("count is :" + count);

        //清空环境
        jdbcTemplate.execute("drop table user");
    }
}
