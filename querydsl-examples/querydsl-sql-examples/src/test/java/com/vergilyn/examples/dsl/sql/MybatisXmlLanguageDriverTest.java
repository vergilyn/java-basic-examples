package com.vergilyn.examples.dsl.sql;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.*;

/**
 *
 * @author dingmaohai
 * @version v1.0
 * @since 2024/06/29 15:33
 *
 * @see org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
 * @see com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver
 * @see <a href="https://mybatis.org/mybatis-3/zh_CN/dynamic-sql.html">mybatis, 动态SQL</a>
 */
public class MybatisXmlLanguageDriverTest {

    private final Configuration _mybatisCfg = new Configuration();

    /**
     *
     * <p> {@link BoundSql#getSql()} 未格式化，可能存在多余的换行之类的。
     * <p> {@link BoundSql#getParameterObject()} 返回的是 Object，并且不是 {@link java.util.Collection} 类型。 而是 {@link java.util.ImmutableCollections.MapN}
     *
     * <p> 貌似可以直接缓存 {@link SqlSource} 对象，避免重复解析{@code script}。
 }
 }
     */
    @ParameterizedTest
    @CsvSource({
            "type-01,",
            "type-02,title-xx"
    })
    void test_dynamicSQL(String type, String title) {

        // language=xml
        final String script = """
                                 <script>
                                     SELECT * FROM tb_name
                                     WHERE type = #{p_type}
                                     <if test="p_title != null">
                                       AND title like #{p_title}
                                     </if>
                                 </script>
                                 """;

        Map<String, String> parameterMap = Maps.newHashMap();
        parameterMap.put("p_type", type);
        parameterMap.put("p_title", title);

        // XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        LanguageDriver languageDriver = _mybatisCfg.getLanguageDriver(XMLLanguageDriver.class);

        // XXX 2024-06-29 待了解为什么需要`_mybatisCfg`及其用途
        SqlSource sqlSource = languageDriver.createSqlSource(_mybatisCfg, script, Map.class);
        BoundSql boundSql = sqlSource.getBoundSql(parameterMap);

        System.out.printf("[SQL] >>>>\n%s\n\n", boundSql.getSql());
        System.out.printf("[ParameterObject] >>>>\n%s\n\n", boundSql.getParameterObject());
        System.out.printf("[ParameterMappings] >>>>\n%s\n\n", boundSql.getParameterMappings());

        Assertions.assertThat(boundSql.getSql()).isEqualToIgnoringWhitespace("SELECT * FROM tb_name WHERE type = ? " + (title == null ? "" : "AND title like ?"));
    }

    /**
     * 因为mybatis 存在Mapper缓存机制，所以可以考虑缓存Mapper，减少解析Mapper的开销。 参考：{@link #test_xmlMapperBuilder_cache()}
     *
     * @see com.baomidou.mybatisplus.core.MybatisParameterHandler#setParameters(PreparedStatement)
     * @see <a href="https://github.com/mybatis/mybatis-3/blob/mybatis-3.5.4/src/test/java/org/apache/ibatis/builder/XmlMapperBuilderTest.java">XmlMapperBuilderTest.java</a>
     */
    @ParameterizedTest
    @CsvSource({
            "10086,",
            "10086,title-xx"
    })
    void test_xmlMapperBuilder(Integer authorId, String title) {
        Configuration configuration = new Configuration();

        String resource = "org/apache/ibatis/builder/AuthorMapper.xml";
        // language=xml
        String mapperXml = """
                          <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                          <mapper namespace="org.apache.ibatis.builder.AuthorMapper">
                            <select id="selectAuthor" parameterType="int" resultType="java.util.Map">
                              SELECT * FROM author 
                              WHERE author_id = #{author_id}
                              <if test="title != null">
                                    AND book_title like #{title}
                              </if>
                              AND ids IN 
                              <foreach collection="ids_list" item="item" index="index" open="(" separator="," close=")">
                                 #{item}
                              </foreach>
                            </select>
                          </mapper>
                          """;

        InputStream inputStream = IOUtils.toInputStream(mapperXml, "UTF-8");

        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse();

        MockHashMap parameterMap = new MockHashMap();
        // parameterMap.put("author_id", authorId);
        // parameterMap.put("title", title);

        MappedStatement mappedStatement = configuration.getMappedStatement("org.apache.ibatis.builder.AuthorMapper.selectAuthor");

        BoundSql boundSql = mappedStatement.getBoundSql(parameterMap);

        // 将参数部分转换成 List<Object>
        List<Object> parameters = convertParameters(configuration, boundSql, parameterMap);

        System.out.printf("[SQL] >>>>\n%s\n\n", removeBreakingWhitespace(boundSql.getSql()));
        System.out.printf("[SQL_PARAM] >>>>\n%s\n\n", JSON.toJSONString(parameters));
        System.out.printf("[SQL_PARAM] >>>>\n%s\n\n", JSON.toJSONString(parameterMap.keySet));
        Assertions.assertThat(boundSql.getSql())
                  .isEqualToIgnoringWhitespace("SELECT * FROM author WHERE author_id = ?" + (title == null ? "" : "AND book_title like ?"));

        Assertions.assertThat(parameters)
                  .contains(authorId, Index.atIndex(0))
                  .satisfies(o -> {
                      if (title != null) {
                          Assertions.assertThat(o.get(1)).isEqualTo(title);
                      }
                  });

    }

    /**
     * @see com.baomidou.mybatisplus.core.MybatisParameterHandler#setParameters(PreparedStatement)
     */
    List<Object> convertParameters(Configuration configuration, BoundSql boundSql, Map<String, Object> parameterObject) {

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        List<Object> parameters = Lists.newArrayList();
        for (ParameterMapping parameterMapping : parameterMappings) {

            if (parameterMapping.getMode() == ParameterMode.OUT) {
                continue;
            }

            Object value;
            String propertyName = parameterMapping.getProperty();

            if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (parameterObject == null) {
                value = null;
            } else if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                value = parameterObject;
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                value = metaObject.getValue(propertyName);
            }

            parameters.add(value);
        }

        return parameters;
    }

    /**
     *
     * @see org.apache.ibatis.logging.jdbc.BaseJdbcLogger#removeBreakingWhitespace
     */
    protected String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
            builder.append(whitespaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * <p> Q1. {@link Configuration#getMappedStatementNames()} 存在2个值：{@code namespace.mdss_ap_service_sql.10086.10086} 和 {@code 10086}。
     * <br/> 如果不同的namespace下存在相同的 id，会怎么样？
     */
    @Test
    void test_xmlMapperBuilder_cache() {
        Configuration configuration = new Configuration();

        // 同时是 namespace 和 resource
        String mapperNamespace = "namespace.mdss_ap_service_sql.10086";
        String mapperId = mapperNamespace + ".10086";

        List<Map<String, String>> parameterList = Lists.newArrayList();
        parameterList.add(Map.of("author_id", "10086"));
        parameterList.add(Map.of("author_id", "10086", "title", "title_xxx"));

        for (Map<String, String> parameterMap : parameterList) {

            MappedStatement mappedStatement;
            // configuration.getMappedStatementNames()
            // 0 = "namespace.mdss_ap_service_sql.10086.10086"
            // 1 = "10086"
            if (configuration.getMappedStatementNames().contains(mapperId)) {
                mappedStatement = configuration.getMappedStatement(mapperId);
            } else {
                // language=xml
                String mapperXml = """
                          <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                          <mapper namespace="namespace.mdss_ap_service_sql.10086">
                            <select id="10086" parameterType="java.util.Map" resultType="java.util.Map">
                              SELECT * FROM author 
                              WHERE author_id = #{author_id}
                              <if test="title != null">
                                    AND book_title like #{title}
                              </if>
                            </select>
                          </mapper>
                          """;

                InputStream inputStream = IOUtils.toInputStream(mapperXml, "UTF-8");

                XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, mapperNamespace, configuration.getSqlFragments());
                builder.parse();

                mappedStatement = configuration.getMappedStatement(mapperId);
            }

            BoundSql boundSql = mappedStatement.getBoundSql(parameterMap);

            System.out.printf("[SQL] >>>>\n%s\n\n", boundSql.getSql());
            Assertions.assertThat(boundSql.getSql())
                      .isEqualToIgnoringWhitespace("SELECT * FROM author WHERE author_id = ?" + (!parameterMap.containsKey("title") ? "" : "AND book_title like ?"));

        }

    }

    @Test
    void test122() {
        String sql = """
                SELECT * FROM `aops_resource` WHERE 1=1
                                      <if test="name != null">
                                      AND name like #{name}
                                      </if>
                                      And categ_one_id = #{categOneId}
                                      <if test="ids !=null">
                                                AND resource_id in
                                      <foreach collection="ids" open="(" close=")" separator="," item="id" index="i">
                                          #{id}
                                      </foreach>    
                                      </if>
                                       AND categ_two_id IN
                                      <foreach collection="list" open="(" close=")" separator="," item="id" index="i">
                                          #{id}
                                      </foreach>
                                      AND name_space> #{nameSpace}
                                      order by resource_id desc
                """;

        String s = removeBreakingWhitespace(sql);
        System.out.println(s);
    }

    private static class MockHashMap extends HashMap<String, Object> {

        private final Set<String> keySet = new HashSet<>() ;

        @Override
        public Object get(Object key) {
            keySet.add(key.toString());
            return key.toString().endsWith("_list") ? Lists.newArrayList("1", "2", "3") : "1";
        }

        public Set<String> getKeySet() {
            return keySet;
        }
    }
}
