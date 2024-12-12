package com.vergilyn.examples.dsl.sql;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.Date;
import java.util.List;

public class SQLQueryFactoryTest {

    private final String _tableName = "tb_person";

    /**
     * int, 0-false, 1-true
     */
    private final String _field_is_deleted = "is_deleted";
    /**
     * int
     */
    private final String _field_age = "age";
    /**
     * datetime
     */
    private final String _field_create_time = "create_time";
    /**
     * String
     */
    private final String _field_tag = "tag";

    private final List<String> _sources = List.of("id", "name", "age", "tag", "is_deleted", "create_time");
    private final String _expectedSQL = "SELECT id, name, age, tag, is_deleted, create_time "
                                        + " FROM " + _tableName
                                        + " WHERE is_deleted = ? "
                                        + " AND age > ? "
                                        + " AND create_time >= ? AND create_time <= ? "
                                        + " AND tag = ?";

    private SQLQueryFactory _sqlQueryFactory;
    private SQLQuery<Tuple> _query;

    @BeforeEach
    public void beforeEach() {
        _sqlQueryFactory = new SQLQueryFactory(OracleTemplates.DEFAULT, null);
        _query = null;
    }

    @AfterEach
    public void afterEach(TestInfo info) {
        String displayName = info.getDisplayName();

        if (_query != null) {
            System.out.printf("[%s] SQL >>>> %s\n", displayName, _query.getSQL().getSQL());
            System.out.println();
            System.out.printf("[%s] Bindings >>>> %s\n", displayName, _query.getSQL().getNullFriendlyBindings());

        }

    }

    private Expression<String>[] convertFields() {
        return _sources.stream()
                       .map(Expressions::asString)
                       .toArray(StringExpression[]::new);
    }

    @Test
    void test_simple() {
        // 创建表达式对象
        NumberPath<Integer> expIsDeleted = Expressions.numberPath(Integer.class, _field_is_deleted);
        NumberPath<Integer> expAge = Expressions.numberPath(Integer.class, _field_age);
        DateTimePath<Date> expCreateTime = Expressions.dateTimePath(Date.class, _field_create_time);
        StringPath expTag = Expressions.stringPath(_field_tag);

        StringExpression table = Expressions.asString(_tableName);
        Expression<String>[] fields = convertFields();

        // 构建查询，使用参数
        _query = _sqlQueryFactory.select(fields)
                                 .from(table)
                                 .where(expIsDeleted.eq(1),
                                        expAge.eq(30),
                                        expCreateTime.goe(new Date()),
                                        expCreateTime.loe(new Date()),
                                        expTag.eq("test")).offset(1).limit(20);


    }

    /**
     * @see com.querydsl.core.types.ExpressionUtils
     */
    @Test
    void test_dynamic() {
        NumberPath<Integer> expIsDeleted = Expressions.numberPath(Integer.class, _field_is_deleted);
        NumberPath<Integer> expAge = Expressions.numberPath(Integer.class, _field_age);
        DateTimePath<Date> expCreateTime = Expressions.dateTimePath(Date.class, _field_create_time);
        StringPath expTag = Expressions.stringPath(_field_tag);

        Expression<String>[] fields = convertFields();
        StringExpression table = Expressions.asString(_tableName);

        _query = SQLExpressions.select(fields).from(table).where(expIsDeleted.eq(1),
                                                                 expAge.eq(30),
                                                                 expCreateTime.goe(new Date()),
                                                                 expCreateTime.loe(new Date()),
                                                                 expTag.eq("test")).offset(1).limit(10);


    }

    /**
     * 如果是 `xxxPath`，则最终生成的SQL 不会使用 `?` 占位
     */
    @Test
    void test_dem() {
        StringPath table = Expressions.stringPath(_tableName);

        NumberPath<Integer> expIsDeleted = Expressions.numberPath(Integer.class, _field_is_deleted);

        Expression[] fields = new Expression[]{
                Expressions.numberPath(Integer.class, "id"),
                Expressions.stringPath("tag"),
                Expressions.dateTimePath(Date.class, "create_time"),
                Expressions.asString("name")
        };

        SQLQuery<Object> sqlQuery = new SQLQuery<>(MySQLTemplates.DEFAULT);
        sqlQuery = new SQLQuery<>(OracleTemplates.DEFAULT);

        _query = sqlQuery.select(fields)
                               .from(table)
                               .where(
                                       Expressions.booleanOperation(Ops.EQ, Expressions.path(Integer.class, "is_deleted"), ConstantImpl.create(1)),
                                       Expressions.booleanOperation(Ops.LOE, Expressions.path(Integer.class,"age"), ConstantImpl.create(30)),
                                       // TODO 2024-06-03 类型有什么用？
                                       Expressions.booleanOperation(Ops.GOE, Expressions.path(Date.class, "create_time"), ConstantImpl.create("2024-06-03 00:00:00")),
                                       Expressions.booleanOperation(Ops.LOE, Expressions.path(String.class, "create_time"), ConstantImpl.create("2024-06-03 23:59:59")),
                                       Expressions.booleanOperation(Ops.EQ, Expressions.path(String.class, "tag"), ConstantImpl.create("red"))
                               ).offset(1).limit(10);


    }

}
