package cn.ve.user.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.ve.base.constant.SqlFilter;
import cn.ve.base.util.IdWorker;
import cn.ve.base.util.MybatisUtils;
import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.values.ValuesStatement;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

import static org.apache.ibatis.mapping.SqlCommandType.*;

/**
 * @author ve
 * @date 2021/8/20
 */
@Component
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
    @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})})
public class MyBatisInterceptor implements Interceptor {

    private static final String UPDATE_TIME_COL = "update_time";
    private static final String UPDATE_TIME_FIELD = "updateTime";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ID = "id";
    private static final String CREATE_TIME = "createTime";
    private static final String DELETED = "deleted";
    private static final String SQL_COMMAND_TYPE = "sqlCommandType";
    private static final String SQL = "sql";
    private static final String COUNT_ZERO = "count(0)";
    private static final String DELEGATE = "delegate";
    private static final String MAPPED_STATEMENT = "mappedStatement";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (target instanceof MybatisParameterHandler) {
            SqlCommandType sqlCommandType = BeanUtil.getProperty(target, SQL_COMMAND_TYPE);
            if (sqlCommandType != INSERT) {
                return invocation.proceed();
            }
            MybatisParameterHandler mybatisParameterHandler = (MybatisParameterHandler)target;
            Object parameterObject = mybatisParameterHandler.getParameterObject();
            Field[] declaredFields = parameterObject.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (ID.equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(parameterObject);
                    if (o == null) {
                        declaredField.set(parameterObject, IdWorker.nextId());
                    }
                }
                if ("creatorId".equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, System.currentTimeMillis());
                }
                if ("creatorName".equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, String.valueOf(System.currentTimeMillis()));
                }
                if (CREATE_TIME.equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, new Date());
                }
                if (DELETED.equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, 0);
                }
                if ("updaterId".equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, System.currentTimeMillis());
                }
                if ("updaterName".equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, String.valueOf(System.currentTimeMillis()));
                }
                if (UPDATE_TIME_FIELD.equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    declaredField.set(parameterObject, new Date());
                }
            }
            return invocation.proceed();
        } else if (target instanceof RoutingStatementHandler) {
            // 找到查询类型为SELECT的才做拦截业务
            RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler)target;
            Object delegate = BeanUtil.getProperty(target, DELEGATE);
            Object mappedStatement = BeanUtil.getProperty(delegate, MAPPED_STATEMENT);
            String id = ((MappedStatement)mappedStatement).getId();
            SqlFilter sqlFilter = MybatisUtils.getAnnotation(id);
            BoundSql boundSql = routingStatementHandler.getBoundSql();

            SqlCommandType sqlCommandType = ((MappedStatement)mappedStatement).getSqlCommandType();
            // 对sql进行处理,拼接deleted=0
            handlerSql(sqlCommandType, boundSql, sqlFilter);
            return invocation.proceed();
        } else {
            return invocation.proceed();
        }

    }

    private void handlerSql(SqlCommandType sqlCommandType, BoundSql boundSql, SqlFilter sqlFilter)
        throws JSQLParserException {
        if (SELECT == sqlCommandType) {
            CCJSqlParserManager ccjSqlParserManager = new CCJSqlParserManager();
            Select parse = (Select)ccjSqlParserManager.parse(new StringReader(boundSql.getSql()));
            parse.getSelectBody().accept(new DeletedSelectVisitor(sqlFilter));
            BeanUtil.setProperty(boundSql, SQL, parse.toString());
        } else if (UPDATE == sqlCommandType) {
            CCJSqlParserManager ccjSqlParserManager = new CCJSqlParserManager();
            Update parse = (Update)ccjSqlParserManager.parse(new StringReader(boundSql.getSql()));
            parse.getColumns().add(new Column(UPDATE_TIME_COL));
            parse.getExpressions().add(new TimestampValue(DateTime.now().toString(DATE_FORMAT)));
            Long userId = System.currentTimeMillis();
            if (userId != null) {
                parse.getColumns().add(new Column("updater_id"));
                parse.getExpressions().add(new LongValue(userId));
                parse.getColumns().add(new Column("updater_name"));
                parse.getExpressions().add(new StringValue(String.valueOf(System.currentTimeMillis())));
            }
            BeanUtil.setProperty(boundSql, SQL, parse.toString());
        }
    }

    private static class DeletedSelectVisitor implements SelectVisitor {
        private SqlFilter sqlFilter;

        public DeletedSelectVisitor(SqlFilter sqlFilter) {
            this.sqlFilter = sqlFilter;
        }

        @Override
        public void visit(PlainSelect plainSelect) {
            if (sqlFilter != null && sqlFilter.deletedIgnore()) {
                return;
            }
            setDeletedFromItem(plainSelect, plainSelect.getFromItem());
            if (plainSelect.getJoins() == null) {
                return;
            }
            for (Join join : plainSelect.getJoins()) {
                setDeletedFromItem(plainSelect, join.getRightItem());
            }
        }

        private void setDeletedFromItem(PlainSelect plainSelect, FromItem mainFromItem) {
            Alias mainAlias = mainFromItem.getAlias();
            List<String> partItems = BeanUtil.getProperty(mainFromItem, "partItems");
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column(
                (mainAlias != null ? mainAlias.getName() : partItems.get(1) + "." + partItems.get(0)) + "." + DELETED));
            equalsTo.setRightExpression(new LongValue(0));
            if (plainSelect.getSelectItems().size() == 1 && COUNT_ZERO
                .equals(plainSelect.getSelectItems().get(0).toString())) {
                mainFromItem.accept(new MyFromItemVisitor(sqlFilter));
            } else if (plainSelect.getWhere() == null) {
                plainSelect.setWhere(equalsTo);
            } else {
                plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), equalsTo));
            }
        }

        @Override
        public void visit(SetOperationList setOpList) {
            for (SelectBody select : setOpList.getSelects()) {
                select.accept(this);
            }
        }

        @Override
        public void visit(WithItem withItem) {
        }

        @Override
        public void visit(ValuesStatement aThis) {
        }
    }

    private static class MyFromItemVisitor implements FromItemVisitor {
        private SqlFilter sqlFilter;

        public MyFromItemVisitor(SqlFilter sqlFilter) {
            this.sqlFilter = sqlFilter;
        }

        @Override
        public void visit(Table table) {
        }

        @Override
        public void visit(SubSelect subSelect) {
            subSelect.getSelectBody().accept(new DeletedSelectVisitor(sqlFilter));
        }

        @Override
        public void visit(SubJoin subJoin) {
        }

        @Override
        public void visit(LateralSubSelect lateralSubSelect) {
        }

        @Override
        public void visit(ValuesList valuesList) {
        }

        @Override
        public void visit(TableFunction tableFunction) {
        }

        @Override
        public void visit(ParenthesisFromItem parenthesisFromItem) {
        }
    }

}