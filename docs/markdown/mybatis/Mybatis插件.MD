# mybatis 自定义分页插件

```java
public interface Paginable<T> {

    /** 总记录数 */
    int getTotalCount();

    /** 总页数 */
    int getTotalPage();

    /** 每页记录数 */
    int getPageSize();

    /** 当前页号 */
    int getPageNo();

    /** 是否第一页 */
    boolean isFirstPage();

    /** 是否最后一页 */
    boolean isLastPage();

    /** 返回下页的页号 */
    int getNextPage();

    /** 返回上页的页号 */
    int getPrePage();

    /** 取得当前页显示的项的起始序号 */
    int getBeginIndex();

    /** 取得当前页显示的末项序号 */
    int getEndIndex();
    /** 获取开始页*/
    int getBeginPage();
    /** 获取结束页*/
    int getEndPage();
}

```

```java
package com.blog4java.plugin.pager;

public class Page<T>  implements Paginable<T> {

    public static final int DEFAULT_PAGE_SIZE = 10; // 默认每页记录数

    public static final int PAGE_COUNT = 10;

    private int pageNo = 1; // 页码

    private int pageSize = DEFAULT_PAGE_SIZE; // 每页记录数

    private int totalCount = 0; // 总记录数

    private int totalPage = 0; // 总页数

    private long timestamp = 0; // 查询时间戳

    private boolean full = false; // 是否全量更新 //false 不更新totalcount

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        this.setTotalPage(totalPage);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isFirstPage() {
        return pageNo <= 1;
    }

    public boolean isLastPage() {
        return pageNo >= totalPage;
    }

    public int getNextPage() {
        return isLastPage() ? pageNo : (pageNo + 1);
    }

    public int getPrePage() {
        return isFirstPage() ? pageNo : (pageNo - 1);
    }

    public int getBeginIndex() {
        if (pageNo > 0) {
            return (pageSize * (pageNo - 1));
        } else {
            return 0;
        }
    }

    public int getEndIndex() {
        if (pageNo > 0) {
            return Math.min(pageSize * pageNo, totalCount);
        } else {
            return 0;
        }
    }

    public int getBeginPage() {
        if (pageNo > 0) {
            return (PAGE_COUNT * ((pageNo - 1) / PAGE_COUNT)) + 1;
        } else {
            return 0;
        }
    }

    public int getEndPage() {
        if (pageNo > 0) {
            return Math.min(PAGE_COUNT * ((pageNo - 1) / PAGE_COUNT + 1), getTotalPage());
        } else {
            return 0;
        }
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

```
```java

public abstract class ReflectionUtils {

    /**
     * 利用反射获取指定对象的指定属性
     *
     * @param target 目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    public static Object getFieldValue(Object target, String fieldName) {
        Object result = null;
        Field field = ReflectionUtils.getField(target, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param target
     *            目标对象
     * @param fieldName
     *            目标属性
     * @return 目标字段
     */
    private static Field getField(Object target, String fieldName) {
        Field field = null;
        for (Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // ignore
            }
        }
        return field;
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param target 目标对象
     * @param fieldName 目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object target, String fieldName, String fieldValue) {
        Field field = ReflectionUtils.getField(target, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(target, fieldValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```


```java


@Intercepts({
        @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})
})
public class PageInterceptor implements Interceptor {

    private String databaseType;

    public Object intercept(Invocation invocation) throws Throwable {
        // 获取拦截的目标对象
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(handler, "delegate");
        BoundSql boundSql = delegate.getBoundSql();
        // 获取参数对象，当参数对象为Page的子类时执行分页操作
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject instanceof Page<?>) {
            Page<?> page = (Page<?>) parameterObject;
            MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, "mappedStatement");
            Connection connection = (Connection) invocation.getArgs()[0];
            String sql = boundSql.getSql();
            if (page.isFull()) {
                // 获取记录总数
                this.setTotalCount(page, mappedStatement, connection);
            }
            page.setTimestamp(System.currentTimeMillis());
            // 获取分页SQL
            String pageSql = this.getPageSql(page, sql);
            // 将原始SQL语句替换成分页语句
            ReflectionUtils.setFieldValue(boundSql, "sql", pageSql);
        }
        return invocation.proceed();
    }

    /**
     * 拦截器对应的封装原始对象的方法
     */
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置注册拦截器时设定的属性
     */
    public void setProperties(Properties properties) {
        this.databaseType = properties.getProperty("databaseType");
    }

    /**
     * 根据page对象获取对应的分页查询Sql语句，
     * 这里只做了三种数据库类型，Mysql、Oracle、HSQLDB
     * 其它的数据库都没有进行分页
     *
     * @param page 分页对象
     * @param sql  原始sql语句
     * @return
     */
    private String getPageSql(Page<?> page, String sql) {
        StringBuffer sqlBuffer = new StringBuffer(sql);
        if ("mysql".equalsIgnoreCase(databaseType)) {
            return getMysqlPageSql(page, sqlBuffer);
        } else if ("oracle".equalsIgnoreCase(databaseType)) {
            return getOraclePageSql(page, sqlBuffer);
        } else if ("hsqldb".equalsIgnoreCase(databaseType)) {
            return getHSQLDBPageSql(page, sqlBuffer);
        }
        return sqlBuffer.toString();
    }

    /**
     * 获取Mysql数据库的分页查询语句
     *
     * @param page      分页对象
     * @param sqlBuffer 包含原sql语句的StringBuffer对象
     * @return Mysql数据库分页语句
     */
    private String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
        int offset = (page.getPageNo() - 1) * page.getPageSize();
        sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
        return sqlBuffer.toString();
    }

    /**
     * 获取Oracle数据库的分页查询语句
     *
     * @param page      分页对象
     * @param sqlBuffer 包含原sql语句的StringBuffer对象
     * @return Oracle数据库的分页查询语句
     */
    private String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
        int offset = (page.getPageNo() - 1) * page.getPageSize() + 1;
        sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ")
                .append(offset + page.getPageSize());
        sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
        return sqlBuffer.toString();
    }


    /**
     * 获取HSQLDB数据库的分页查询语句
     *
     * @param page      分页对象
     * @param sqlBuffer 包含原sql语句的StringBuffer对象
     * @return Oracle数据库的分页查询语句
     */
    private String getHSQLDBPageSql(Page<?> page, StringBuffer sqlBuffer) {
        int offset = (page.getPageNo() - 1) * page.getPageSize() + 1;
        String sql = "select limit " + offset + " " + page.getPageSize() + " * from (" + sqlBuffer.toString() + " )";
        return sql;
    }

    /**
     * 给当前的参数对象page设置总记录数
     *
     * @param page            Mapper映射语句对应的参数对象
     * @param mappedStatement Mapper映射语句
     * @param connection      当前的数据库连接
     */
    private void setTotalCount(Page<?> page, MappedStatement mappedStatement, Connection connection) {
        BoundSql boundSql = mappedStatement.getBoundSql(page);
        String sql = boundSql.getSql();
        // 获取总记录数
        String countSql = this.getCountSql(sql);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(countSql);
            parameterHandler.setParameters(pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalCount = rs.getInt(1);
                page.setTotalCount(totalCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(rs);
            IOUtils.closeQuietly(pstmt);
        }
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     *
     * @param sql
     * @return
     */
    private String getCountSql(String sql) {
        return "select count(1) " + sql.substring(sql.toLowerCase().indexOf("from"));
    }
}

```

在mybatis-config.xm配置文件中注册

```java
<plugins>
    <plugin interceptor="com.zdy.plugin.pager.PageInterceptor">
    <property name="databaseType" value="mysql"/>
    </plugin>
</plugins>

```

编写UserQuery 继承 Page类

```java
public class UserQuery extends Page<UserEntity> {
}

```

```java
public interface UserMapper {
    @Select("select * from user")
    List<UserEntity> getUserPageable(UserQuery query);
}
```

```java

    @Test
    public void testPageInterceptor() throws IOException{
          // 获取配置文件输入流
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 通过SqlSessionFactoryBuilder的build()方法创建SqlSessionFactory实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 调用openSession()方法创建SqlSession实例
        sqlSession = sqlSessionFactory.openSession();
        // 获取UserMapper代理对象
        userMapper = sqlSession.getMapper(UserMapper.class);
        UserQuery query = new UserQuery();
        query.setPageSize(5);
        query.setFull(true);
        List<UserEntity> users = userMapper.getUserPageable(query);
        System.out.println("总数据量：" + query.getTotalCount() + ",总页数："
                + query.getTotalPage()+ "，当前查询数据：" + JSON.toJSONString(users));
    }
```

## 自定义统计慢SQL 插件

```java
package com.blog4java.plugin.slowsql;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class SlowSqlInterceptor implements Interceptor {

    private Integer limitSecond;

    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        long beginTimeMillis = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        try {
            return invocation.proceed();
        } finally {
            long endTimeMillis = System.currentTimeMillis();
            long costTimeMillis = endTimeMillis - beginTimeMillis;
            if (costTimeMillis > limitSecond * 1000) {
                BoundSql boundSql = statementHandler.getBoundSql();
                // 调用getFormatedSql（）方法对参数占位符进行替换
                String sql = getFormatedSql(boundSql);
                System.out.println("SQL语句【" + sql + "】，执行耗时：" + costTimeMillis + "ms");
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String limitSecond = (String) properties.get("limitSecond");
        this.limitSecond = Integer.parseInt(limitSecond);
    }

    private String getFormatedSql(BoundSql boundSql) {
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        sql = beautifySql(sql);
        if (parameterObject == null || parameterMappings == null || parameterMappings.isEmpty()) {
            return sql;
        }
        String sqlWithoutReplacePlaceholder = sql;
        try {
            if (parameterMappings != null) {
                Class<?> parameterObjectClass = parameterObject.getClass();
                if (isStrictMap(parameterObjectClass)) {
                    DefaultSqlSession.StrictMap<Collection<?>> strictMap = (DefaultSqlSession.StrictMap<Collection<?>>) parameterObject;
                    if (isList(strictMap.get("list").getClass())) {
                        sql = handleListParameter(sql, strictMap.get("list"));
                    }
                } else if (isMap(parameterObjectClass)) {
                    Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                    sql = handleMapParameter(sql, paramMap, parameterMappings);
                } else {
                    sql = handleCommonParameter(sql, parameterMappings, parameterObjectClass, parameterObject);
                }
            }
        } catch (Exception e) {
            return sqlWithoutReplacePlaceholder;
        }
        return sql;

    }

    private String beautifySql(String sql) {
        sql = sql.replace("\n", "")
                .replace("\t", "")
                .replace("  ", " ")
                .replace("( ", "(")
                .replace(" )", ")")
                .replace(" ,", ",");
        return sql;

    }

    private String handleListParameter(String sql, Collection<?> col) {
        if (col != null && col.size() != 0) {
            for (Object obj : col) {
                String value = null;
                Class<?> objClass = obj.getClass();
                if (isPrimitiveOrPrimitiveWrapper(objClass)) {
                    value = obj.toString();
                } else if (objClass.isAssignableFrom(String.class)) {
                    value = "\"" + obj.toString() + "\"";
                }
                sql = sql.replaceFirst("\\?", value);
            }
        }
        return sql;

    }

    private String handleMapParameter(String sql, Map<?, ?> paramMap, List<ParameterMapping> parameterMappingList) {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            Object propertyName = parameterMapping.getProperty();
            Object propertyValue = paramMap.get(propertyName);
            if (propertyValue != null) {
                if (propertyValue.getClass().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
                sql = sql.replaceFirst("\\?", propertyValue.toString());
            }
        }
        return sql;

    }


    private String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList,
                                         Class<?> parameterObjectClass,
                                         Object parameterObject) throws Exception {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            String propertyValue = null;
            if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
                propertyValue = parameterObject.toString();
            } else {
                String propertyName = parameterMapping.getProperty();
                Field field = parameterObjectClass.getDeclaredField(propertyName);
                field.setAccessible(true);
                propertyValue = String.valueOf(field.get(parameterObject));
                if (parameterMapping.getJavaType().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
            }
            sql = sql.replaceFirst("\\?", propertyValue);
        }
        return sql;

    }

    private boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
        return parameterObjectClass.isPrimitive() ||
                (parameterObjectClass.isAssignableFrom(Byte.class)
                        || parameterObjectClass.isAssignableFrom(Short.class)
                        || parameterObjectClass.isAssignableFrom(Integer.class)
                        || parameterObjectClass.isAssignableFrom(Long.class)
                        || parameterObjectClass.isAssignableFrom(Double.class)
                        || parameterObjectClass.isAssignableFrom(Float.class)
                        || parameterObjectClass.isAssignableFrom(Character.class)
                        || parameterObjectClass.isAssignableFrom(Boolean.class));
    }

    private boolean isStrictMap(Class<?> parameterObjectClass) {
        return parameterObjectClass.isAssignableFrom(DefaultSqlSession.StrictMap.class);

    }


    private boolean isList(Class<?> clazz) {
        Class<?>[] interfaceClasses = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(List.class)) {
                return true;
            }
        }
        return false;

    }


    private boolean isMap(Class<?> parameterObjectClass) {
        Class<?>[] interfaceClasses = parameterObjectClass.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(Map.class)) {
                return true;
            }
        }
        return false;

    }

}

```

注册

```java
	<plugin interceptor="com.blog4java.plugin.slowsql.SlowSqlInterceptor">
			<property name="limitSecond" value="0"/>
		</plugin>
```

