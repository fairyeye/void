//package com.example.demo.DB.jdbcTemplete;
//
//import java.lang.reflect.InvocationTargetException;
//
//import jdk.nashorn.internal.runtime.logging.Logger;
//import org.apache.commons.beanutils.PropertyUtils;
//import org.slf4j.LoggerFactory;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import static java.util.stream.Collectors.toList;
//
///**
// * @author huapeng.zhang
// * @version 1.0
// * @date 2020/9/22 10:46
// */
//public class JDBCDemo {
//
//    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JDBCDemo.class);
//
//
//    public List<String> getInsertSql(Object dto) {
//        final StringBuilder sql = new StringBuilder();
//        final StringBuilder columnSql = new StringBuilder();
//        final StringBuilder valueSql = new StringBuilder();
//
//        List<String> sqlList = new ArrayList<String>();
//        Class<?> entityClass = dto.getClass();
//        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
//
//        // Table Name
//        sql.append("INSERT INTO ");
//        sql.append(entityTable.getName());
//        sql.append(" ");
//        sql.append("(");
//
//        // Table Column&Value
//        columnList.forEach(
//                t -> {
//                    if (t.isInsertable()) {
//                        try {
//                            Object obj = PropertyUtils.getProperty(dto, t.getProperty());
//                            if (obj != null) {
//                                columnSql.append(t.getColumn()).append(",");
//                                if (obj instanceof String) {
//                                    obj = ((String) obj).replace("'", "''");
//                                    valueSql.append("'").append(obj.toString()).append("'").append(",");
//                                } else if (obj instanceof Date) {
//                                    valueSql.append("'").append(DATE_FORMAT.format(obj)).append("'").append(",");
//                                } else {
//                                    valueSql.append(obj).append(",");
//                                }
//                            }
//                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                            LOGGER.info(e.getMessage());
//                        }
//                    }
//                });
//
//        columnSql.deleteCharAt(columnSql.length() - 1);
//        valueSql.deleteCharAt(valueSql.length() - 1);
//        sql.append(columnSql).append(") ").append("VALUES(").append(valueSql).append(")");
//        sqlList.add(sql.toString());
//
//        return sqlList;
//    }
//}
