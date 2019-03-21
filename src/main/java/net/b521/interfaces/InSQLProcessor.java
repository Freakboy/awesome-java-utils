package net.b521.interfaces;

/**
 * @author Allen
 * @Date: 2019/03/21 17:51
 * @Description: SQL执行处理器接口, 用于处理带IN子句的SQL语句中IN中参数过长的问题
 * @Version 1.0
 **/
public interface InSQLProcessor {

    /**
     * 执行SQL的方法.
     *
     * @param sql  SQL语句
     * @param args 语句中的参数
     */
    public void executeSQL(String sql, Object[] args);

}
