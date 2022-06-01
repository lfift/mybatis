package com.ift.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 * 阈值插件
 *
 * @author liufei
 */
@Intercepts(
        value = {
                @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
        }
)
public class ThresholdPlugin implements Interceptor {

    private int threshold;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = invocation.proceed();
        long end = System.currentTimeMillis();
        if (end - start > threshold) {
            Object[] args = invocation.getArgs();
            Statement stat = (Statement) args[0];
            MetaObject metaObject = SystemMetaObject.forObject(stat);
            PreparedStatementLogger statementLogger = (PreparedStatementLogger) metaObject.getValue("h");
            PreparedStatement statement = statementLogger.getPreparedStatement();
            System.out.println("本次执行超过阈值, sql: " + statement.toString());
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
       this.threshold = Integer.parseInt(properties.getProperty("threshold", String.valueOf(10)));
    }
}
