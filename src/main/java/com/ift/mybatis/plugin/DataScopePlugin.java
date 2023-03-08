package com.ift.mybatis.plugin;

import com.ift.mybatis.annotation.DataScope;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据权限插件
 *
 * @author liufei
 * @date 2023/3/8 10:14
 */
@Intercepts(
        value = {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        }
)
public class DataScopePlugin implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(DataScopePlugin.class);

    private DataScope findDataScope(MappedStatement mappedStatement) {
        if (mappedStatement == null) {
            return null;
        }
        final String id = mappedStatement.getId();
        String clazzName = id.substring(0, id.lastIndexOf("."));
        final String methodName = id.substring(id.lastIndexOf(".") + 1);
        try {
            final Class<?> clazz = Class.forName(clazzName);
            for (Method method : clazz.getMethods()) {
                if (methodName.equals(method.getName())) {
                    return method.getAnnotation(DataScope.class);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final DataScope dataScope = this.findDataScope((MappedStatement) invocation.getArgs()[0]);
        if (dataScope == null) {
            return invocation.proceed();
        }
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatements = (MappedStatement) args[0];
        Map<String, Object> paramMap = new MapperMethod.ParamMap<>();
        if (args[1] != null) {
            if (args[1] instanceof MapperMethod.ParamMap) {
                paramMap.putAll((Map) args[1]);
            } else if (BeanUtils.isSimpleValueType(args[1].getClass())) {
                String mapperId = mappedStatements.getId();
                Class<?> paramType = args[1].getClass();
                String clazzName = mapperId.substring(0, mapperId.lastIndexOf("."));
                String methodName = mapperId.substring(mapperId.lastIndexOf(".") + 1);
                Class<?> target = Class.forName(clazzName);
                Method method = target.getMethod(methodName, paramType);
                Parameter[] params = method.getParameters();
                String paramName = params[0].getName();
                paramMap.put(paramName, args[1]);
            } else {
                List<Field> fields = new ArrayList<>();
                this.getAllFields(fields, args[1].getClass());
                for (Field field : fields) {
                    Object fieldValue = null;
                    String fieldName = field.getName();
                    try {
                        fieldValue = (new PropertyDescriptor(fieldName, args[1].getClass())).getReadMethod().invoke(args[1]);
                    } catch (Exception var9) {
                        logger.error(var9.getMessage(), var9);
                    }
                    paramMap.putIfAbsent(field.getName(), fieldValue);
                }
            }
        }
        paramMap.put("dsSql", " and id = #{id}");
        args[1] = paramMap;
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    private void getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            this.getAllFields(fields, type.getSuperclass());
        }

    }
}
