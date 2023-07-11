package com.vergilyn.examples.h2.powerjob;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.h2.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * 参考`powerjob: 4.3.2`:
 * <pre>
 *   - tech.powerjob.worker.persistence.ConnectionFactory
 * </pre>
 *
 * @author vergilyn
 * @since 2023-06-12
 *
 */
public class PowerjobWorkerH2Tests {

    private final String H2_PATH = System.getProperty("user.home", "D:") + "/.h2/" + LocalDate.now() + "/";
    private final String DISK_JDBC_URL = String.format("jdbc:h2:file:%spowerjob_worker_db;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false", H2_PATH);
    private final String MEMORY_JDBC_URL = String.format("jdbc:h2:mem:%spowerjob_worker_db;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false", H2_PATH);

    @BeforeEach
    public void beforeEach(){
        System.out.printf("h2-path: %s,\ndisk-path: %s,\nmemory-path:%s\n", H2_PATH, DISK_JDBC_URL, MEMORY_JDBC_URL);
    }

    @SneakyThrows
    @AfterEach
    public void afterEach(){
        // JVM 关闭时删除数据库文件（不一定保证100%能删除，可能存在无法正确删除的情况）
        // FileUtils.forceDeleteOnExit(new File(H2_PATH));
    }

    /**
     * 如果是 {@link StoreStrategy#DISK}，会在对应的{@link #DISK_JDBC_URL}目录创建db文件，
     * 例如 “C:\Users\01\.h2\2023-06-12\powerjob_worker_db.mv.db”。
     *
     * <blockquote>
     *     当你的应用程序使用 H2 数据库进行数据操作时，所有的数据都将存储在这个文件 `*.mv.db` 中。
     *     当你再次运行应用程序并连接到相同的数据库文件时，之前存储的数据仍然可用。
     * </blockquote>
     */
    @SneakyThrows
    @Test
    void testH2StoreStrategy(){
        HikariDataSource dataSource = createHikariDatasource(StoreStrategy.DISK);

        // 此时会在
        initTable(dataSource);
    }

    private void initTable(@Nonnull HikariDataSource dataSource) throws Exception {
        String delTableSQL = "drop table if exists task_info";
        // 感谢 Gitee 用户 @Linfly 反馈的 BUG
        // bigint(20) 与 Java Long 取值范围完全一致
        String createTableSQL = "create table task_info (" +
                "task_id varchar(255), " +
                "instance_id bigint, " +
                "sub_instance_id bigint, " +
                "task_name varchar(255), " +
                "task_content blob, " +
                "address varchar(255), " +
                "status int, " +
                "result text, " +
                "failed_cnt int, " +
                "created_time bigint, " +
                "last_modified_time bigint, " +
                "last_report_time bigint, " +
                "constraint pkey unique (instance_id, task_id)" +
                ")";

        try (Connection conn = dataSource.getConnection();
             Statement stat = conn.createStatement()) {
            stat.execute(delTableSQL);
            stat.execute(createTableSQL);
        }
    }

    private HikariDataSource createHikariDatasource(@Nonnull StoreStrategy strategy){
        HikariConfig hikariConfig = createHikariConfig(strategy);
        return new HikariDataSource(hikariConfig);
    }

    private HikariConfig createHikariConfig(@Nonnull StoreStrategy strategy){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(Driver.class.getName());
        config.setJdbcUrl(strategy == StoreStrategy.DISK ? DISK_JDBC_URL : MEMORY_JDBC_URL);
        config.setAutoCommit(true);
        // 池中最小空闲连接数量
        config.setMinimumIdle(2);
        // 池中最大连接数量
        config.setMaximumPoolSize(32);

        return config;
    }

    private static enum StoreStrategy {
        DISK, MEMORY;
    }
}
