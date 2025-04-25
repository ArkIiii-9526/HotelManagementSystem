package com.system.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class) // 使用Spring的测试运行器
@ContextConfiguration(classes = SpringConfig.class) // 指定配置类
public class SpringConfigTest {

    @Autowired
    private DataSource dataSource; // 自动注入数据源

    @Test
    public void testDataSourceConnection() throws SQLException {
        // 获取数据库连接并验证
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull("数据库连接不应为null", connection);
            assertTrue("连接应有效", connection.isValid(5));
        }
    }
}