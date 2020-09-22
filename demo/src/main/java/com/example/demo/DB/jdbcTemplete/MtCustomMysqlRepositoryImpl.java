//package com.example.demo.DB.jdbcTemplete;
//
//import org.apache.commons.beanutils.PropertyUtils;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.DataAccessResourceFailureException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.lang.reflect.InvocationTargetException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static java.util.stream.Collectors.toList;
//
///**
// * 注意该类不允许加Component注解
// *
// * @author : MrZ
// * @date : 2020-02-25 18:01
// * @update : 更新物料接口多语言BUG 2020-7-15 18:12:00
// */
//public class MtCustomMysqlRepositoryImpl{
//  private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);
//  private static final String VALUE_SQL = "select last_insert_id()";
//  private static final String TABLE_NAME = "mt_sys_sequence";
//  private static final String COLUMN_NAME = "current_value";
//  private static final List<String> CREATE_FIELDS = Arrays.asList("CREATED_BY", "CREATION_DATE");
//  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//  private String suffix = ".1";
//  private Long increment = 1L;
//  private JdbcTemplate jdbcTemplate;
//
//  public MtCustomMysqlRepositoryImpl() {}
//
//  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
//    this.jdbcTemplate = jdbcTemplate;
//  }
//
//  //@Override
//  // @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
//  public String getNextKey(String seqName) throws DataAccessException {
//    if (StringUtils.isEmpty(seqName)) {
//      throw new DataAccessResourceFailureException("sequence name must be set.");
//    }
//
//    int row =
//        this.jdbcTemplate.update(
//            "update "
//                + TABLE_NAME
//                + " set "
//                + COLUMN_NAME
//                + " = last_insert_id("
//                + COLUMN_NAME
//                + " + "
//                + this.increment
//                + ") where name='"
//                + seqName
//                + "'");
//    if (row == 0) {
//      return null;
//    }
//
//    Long nextId =
//        this.jdbcTemplate.query(
//            VALUE_SQL,
//            rs -> {
//              if (!rs.next()) {
//                throw new DataAccessResourceFailureException(
//                    "last_insert_id() failed after executing an update");
//              }
//              return rs.getLong(1);
//            });
//
//    if (seqName.contains("cid")) {
//      return nextId + "";
//    } else {
//      return nextId + suffix;
//    }
//  }
//
//  //@Override
//  // @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
//  public List<String> getNextKeys(String seqName, int count) throws DataAccessException {
//    if (StringUtils.isEmpty(seqName)) {
//      throw new DataAccessResourceFailureException("sequence name must be set.");
//    }
//
//    int row =
//        this.jdbcTemplate.update(
//            "update "
//                + TABLE_NAME
//                + " set "
//                + COLUMN_NAME
//                + " = last_insert_id("
//                + COLUMN_NAME
//                + " + "
//                + this.increment * count
//                + ") where name='"
//                + seqName
//                + "'");
//    if (row == 0) {
//      return null;
//    }
//
//    Long nextId =
//        this.jdbcTemplate.query(
//            VALUE_SQL,
//            rs -> {
//              if (!rs.next()) {
//                throw new DataAccessResourceFailureException(
//                    "last_insert_id() failed after executing an update");
//              }
//              return rs.getLong(1);
//            });
//
//    if (nextId == null) {
//      return null;
//    }
//
//    Long start = nextId - (this.increment * count);
//    List<String> result = new ArrayList<String>(count);
//    for (int i = 0; i < count; i++) {
//      start = start + this.increment;
//      if (seqName.contains("cid")) {
//        result.add(start + "");
//      } else {
//        result.add(start + suffix);
//      }
//    }
//    return result;
//  }
//
//  //@Override
//  public List<String> getDeleteSql(AuditDomain dto) {
//    final StringBuilder sql = new StringBuilder();
//    List<String> sqlList = new ArrayList<String>();
//    Class<?> entityClass = dto.getClass();
//    EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//    Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
//
//    sql.append("DELETE FROM ")
//        .append(entityTable.getName())
//        .append(" ")
//        .append("WHERE")
//        .append(" ");
//
//    StringBuilder pkSql = new StringBuilder();
//    for (EntityColumn field : pkColumns) {
//      try {
//        Object obj = PropertyUtils.getProperty(dto, field.getProperty());
//        if (obj != null) {
//          if (obj instanceof String) {
//            pkSql
//                .append(field.getColumn())
//                .append("=")
//                .append("'")
//                .append(obj.toString())
//                .append("'")
//                .append(" AND ");
//          } else if (obj instanceof Date) {
//            pkSql
//                .append(field.getColumn())
//                .append("=")
//                .append("'")
//                .append(DATE_FORMAT.format(obj))
//                .append("'")
//                .append(" AND ");
//          } else {
//            pkSql.append(field.getColumn()).append("=").append(obj).append(" AND ");
//          }
//        }
//      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        LOGGER.info(e.getMessage());
//      }
//    }
//
//    if (pkSql.length() != 0) {
//      sql.append(pkSql);
//      sql.delete(sql.length() - " AND ".length(), sql.length() - 1);
//      sqlList.add(sql.toString());
//    }
//
//    MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
//    if (multiLanguageTable != null) {
//      String tableName = entityTable.getMultiLanguageTableName();
//      StringBuilder languageSql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
//
//      if (pkSql.length() != 0) {
//        languageSql.append(pkSql);
//        languageSql.delete(languageSql.length() - " AND ".length(), languageSql.length() - 1);
//        sqlList.add(languageSql.toString());
//      }
//    }
//    return sqlList;
//  }
//
//  //@Override
//  public List<String> getUpdateSql(AuditDomain dto) {
//    final StringBuilder sql = new StringBuilder();
//    List<EntityColumn> pkColumns = new ArrayList<EntityColumn>();
//    List<String> sqlList = new ArrayList<String>();
//    Class<?> entityClass = dto.getClass();
//    EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//    Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
//
//    sql.append("UPDATE ").append(entityTable.getName()).append(" ").append("set").append(" ");
//
//    for (EntityColumn column : columnList) {
//      if (column.isId()) {
//        pkColumns.add(column);
//        continue;
//      }
//      if ("last_update_date".equalsIgnoreCase(column.getColumn())) {
//        sql.append(column.getColumn()).append("=").append("CURRENT_TIMESTAMP,");
//        continue;
//      }
//      if ("object_version_number".equalsIgnoreCase(column.getColumn())) {
//        sql.append(column.getColumn()).append("=").append(column.getColumn()).append("+1,");
//        continue;
//      }
//
//      if (column.isUpdatable()) {
//        try {
//          Object value = PropertyUtils.getProperty(dto, column.getProperty());
//          if (value != null) {
//            if (value instanceof String) {
//              value = ((String) value).replace("'", "''");
//              sql.append(column.getColumn())
//                  .append("=")
//                  .append("'")
//                  .append(value.toString())
//                  .append("'")
//                  .append(",");
//            } else if (value instanceof Date) {
//              sql.append(column.getColumn())
//                  .append("=")
//                  .append("'")
//                  .append(DATE_FORMAT.format(value))
//                  .append("'")
//                  .append(",");
//            } else {
//              sql.append(column.getColumn()).append("=").append(value).append(",");
//            }
//          }
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//          LOGGER.info(e.getMessage());
//        }
//      }
//    }
//
//    sql.deleteCharAt(sql.length() - 1);
//    sql.append(" WHERE ");
//
//    if (CollectionUtils.isEmpty(pkColumns)) {
//      return Collections.emptyList();
//    }
//
//    pkColumns.forEach(
//        t -> {
//          try {
//            Object value = PropertyUtils.getProperty(dto, t.getProperty());
//            if (value == null) {
//              sql.append(t.getColumn()).append("=").append("''").append(" AND ");
//            } else {
//              if (value instanceof String) {
//                sql.append(t.getColumn())
//                    .append("=")
//                    .append("'")
//                    .append(value.toString())
//                    .append("'")
//                    .append(" AND ");
//              } else if (value instanceof Date) {
//                sql.append(t.getColumn())
//                    .append("=")
//                    .append("'")
//                    .append(DATE_FORMAT.format(value))
//                    .append("'")
//                    .append(" AND ");
//              } else {
//                sql.append(t.getColumn()).append("=").append(value).append(" AND ");
//              }
//            }
//          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            LOGGER.info(e.getMessage());
//          }
//        });
//
//    sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
//    sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));
//
//    return sqlList;
//  }
//
//  //@Override
//  public List<String> getFullUpdateSql(AuditDomain dto) {
//    final StringBuilder sql = new StringBuilder();
//    List<EntityColumn> pkColumns = new ArrayList<EntityColumn>();
//    List<String> sqlList = new ArrayList<String>();
//    Class<?> entityClass = dto.getClass();
//    EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//    Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
//
//    sql.append("UPDATE ").append(entityTable.getName()).append(" ").append("set").append(" ");
//
//    for (EntityColumn column : columnList) {
//      if (column.isId()) {
//        pkColumns.add(column);
//        continue;
//      }
//      if ("last_update_date".equalsIgnoreCase(column.getColumn())) {
//        sql.append(column.getColumn()).append("=").append("CURRENT_TIMESTAMP,");
//        continue;
//      }
//      if ("object_version_number".equalsIgnoreCase(column.getColumn())) {
//        sql.append(column.getColumn()).append("=").append(column.getColumn()).append("+1,");
//        continue;
//      }
//
//      if (column.isUpdatable()) {
//        try {
//          Object value = PropertyUtils.getProperty(dto, column.getProperty());
//          if (value != null) {
//            if (value instanceof String) {
//              value = ((String) value).replace("'", "''");
//              sql.append(column.getColumn())
//                  .append("=")
//                  .append("'")
//                  .append(value.toString())
//                  .append("'")
//                  .append(",");
//            } else if (value instanceof Date) {
//              sql.append(column.getColumn())
//                  .append("=")
//                  .append("'")
//                  .append(DATE_FORMAT.format(value))
//                  .append("'")
//                  .append(",");
//            } else {
//              sql.append(column.getColumn()).append("=").append(value).append(",");
//            }
//          } else {
//            if (!"java.lang.String".equals(column.getJavaType().getName())
//                && !CREATE_FIELDS.contains(column.getColumn().toUpperCase())) {
//              sql.append(column.getColumn()).append("=").append("null").append(",");
//            }
//          }
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//          LOGGER.info(e.getMessage());
//        }
//      }
//    }
//
//    sql.deleteCharAt(sql.length() - 1);
//    sql.append(" WHERE ");
//
//    if (CollectionUtils.isEmpty(pkColumns)) {
//      return Collections.emptyList();
//    }
//
//    pkColumns.forEach(
//        t -> {
//          try {
//            Object value = PropertyUtils.getProperty(dto, t.getProperty());
//            if (value == null) {
//              sql.append(t.getColumn()).append("=").append("''").append(" AND ");
//            } else {
//              if (value instanceof String) {
//                sql.append(t.getColumn())
//                    .append("=")
//                    .append("'")
//                    .append(value.toString())
//                    .append("'")
//                    .append(" AND ");
//              } else if (value instanceof Date) {
//                sql.append(t.getColumn())
//                    .append("=")
//                    .append("'")
//                    .append(DATE_FORMAT.format(value))
//                    .append("'")
//                    .append(" AND ");
//              } else {
//                sql.append(t.getColumn()).append("=").append(value).append(" AND ");
//              }
//            }
//          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            LOGGER.info(e.getMessage());
//          }
//        });
//
//    sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
//    sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));
//
//    return sqlList;
//  }
//
//  //@Override
//  public List<String> getInsertSql(AuditDomain dto) {
//    final StringBuilder sql = new StringBuilder();
//    final StringBuilder columnSql = new StringBuilder();
//    final StringBuilder valueSql = new StringBuilder();
//
//    List<String> sqlList = new ArrayList<String>();
//    Class<?> entityClass = dto.getClass();
//    EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//    Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
//
//    // Table Name
//    sql.append("INSERT INTO ");
//    sql.append(entityTable.getName());
//    sql.append(" ");
//    sql.append("(");
//
//    // Table Column&Value
//    columnList.forEach(
//        t -> {
//          if (t.isInsertable()) {
//            try {
//              Object obj = PropertyUtils.getProperty(dto, t.getProperty());
//              if (obj != null) {
//                columnSql.append(t.getColumn()).append(",");
//                if (obj instanceof String) {
//                  obj = ((String) obj).replace("'", "''");
//                  valueSql.append("'").append(obj.toString()).append("'").append(",");
//                } else if (obj instanceof Date) {
//                  valueSql.append("'").append(DATE_FORMAT.format(obj)).append("'").append(",");
//                } else {
//                  valueSql.append(obj).append(",");
//                }
//              }
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//              LOGGER.info(e.getMessage());
//            }
//          }
//        });
//
//    columnSql.deleteCharAt(columnSql.length() - 1);
//    valueSql.deleteCharAt(valueSql.length() - 1);
//    sql.append(columnSql).append(") ").append("VALUES(").append(valueSql).append(")");
//    sqlList.add(sql.toString());
//
//    MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
//    if (multiLanguageTable != null) {
//      List<String> keys = new ArrayList<String>();
//      List<Object> objs = new ArrayList<Object>();
//
//      String tableName = entityTable.getMultiLanguageTableName();
//
//      StringBuilder languageSql = new StringBuilder("INSERT INTO " + tableName + "(");
//      for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
//        String columnName = field.getColumn();
//        keys.add(columnName);
//        try {
//          objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//          LOGGER.info(e.getMessage());
//        }
//      }
//      keys.add("LANG");
//      // 占位符
//      objs.add(null);
//
//      Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
//      List<EntityColumn> multiFields =
//          allFields.stream().filter(EntityColumn::isMultiLanguage).collect(toList());
//      for (EntityColumn field : multiFields) {
//        keys.add(field.getColumn());
//
//        Map<String, Map<String, String>> _tls = dto.get_tls();
//        if (_tls == null) {
//          // if multi language value not exists in __tls, then use value on current field
//          try {
//            objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
//          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            LOGGER.info(e.getMessage());
//          }
//          continue;
//        } else {
//          Map<String, String> tls = _tls.get(field.getColumn());
//          if (tls == null) {
//            // if multi language value not exists in __tls, then use value on current field
//            try {
//              objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//              LOGGER.info(e.getMessage());
//            }
//            continue;
//          }
//        }
//
//        // 占位符
//        objs.add(null);
//      }
//
//      languageSql.append(StringUtils.join(keys, ","));
//      languageSql.append(") VALUES (");
//
//      List<Language> languages = LanguageHelper.languages();
//      for (Language language : languages) {
//        objs.set(objs.size() - multiFields.size() - 1, language.getCode());
//        for (int i = 0; i < multiFields.size(); i++) {
//          int idx = objs.size() - multiFields.size() + i;
//          Map<String, Map<String, String>> _tls = dto.get_tls();
//          if (null != _tls) {
//            Map<String, String> tls = _tls.get(multiFields.get(i).getProperty());
//            if (tls != null) {
//              objs.set(idx, tls.get(language.getCode()));
//            }
//            // 当tls为null时,不设置值(使用field的值,旧模式)
//          }
//        }
//
//        StringBuilder tmpSql = new StringBuilder(languageSql);
//        for (Object obj : objs) {
//          if (obj == null) {
//            tmpSql.append("''").append(",");
//          } else {
//            obj = ((String) obj).replace("'", "''");
//            tmpSql.append("'").append(obj).append("'").append(",");
//          }
//        }
//        tmpSql.deleteCharAt(tmpSql.length() - 1);
//
//        tmpSql.append(")");
//        sqlList.add(tmpSql.toString());
//      }
//    }
//    return sqlList;
//  }
//
//  //@Override
//  public List<String> getReplaceSql(List<AuditDomain> list) {
//    final StringBuilder sql = new StringBuilder();
//    final StringBuilder columnSql = new StringBuilder();
//    final StringBuilder valueSql = new StringBuilder();
//
//    AuditDomain dto = list.get(0);
//    List<String> sqlList = new ArrayList<String>();
//    Class<?> entityClass = dto.getClass();
//    EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//    Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
//
//    // Table Name
//    sql.append("REPLACE INTO ");
//    sql.append(entityTable.getName());
//    sql.append("(");
//
//    // Table Column
//    columnList.forEach(
//        t -> {
//          if (t.isInsertable()) {
//            columnSql.append(t.getColumn()).append(",");
//          }
//        });
//    columnSql.deleteCharAt(columnSql.length() - 1);
//    sql.append(columnSql).append(") ").append("VALUES");
//
//    // Value
//    for (AuditDomain auditDomain : list) {
//      valueSql.append("(");
//      for (EntityColumn t : columnList) {
//        if (t.isInsertable()) {
//          try {
//            Object obj = PropertyUtils.getProperty(auditDomain, t.getProperty());
//            if (obj != null) {
//              if (obj instanceof String) {
//                obj = ((String) obj).replace("'", "''");
//                valueSql.append("'").append(obj.toString()).append("'").append(",");
//              } else if (obj instanceof Date) {
//                valueSql.append("'").append(DATE_FORMAT.format(obj)).append("'").append(",");
//              } else {
//                valueSql.append(obj).append(",");
//              }
//            } else {
//              if ("java.lang.String".equalsIgnoreCase(t.getJavaType().getName())) {
//                valueSql.append("''").append(",");
//              } else {
//                valueSql.append("null").append(",");
//              }
//            }
//          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            LOGGER.info(e.getMessage());
//          }
//        }
//      }
//      valueSql.deleteCharAt(valueSql.length() - 1);
//      valueSql.append("),");
//    }
//    valueSql.deleteCharAt(valueSql.length() - 1);
//    sql.append(valueSql);
//    sqlList.add(sql.toString());
//
//    MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
//    if (multiLanguageTable != null) {
//      List<String> keys = new ArrayList<String>();
//      List<Object> objs = new ArrayList<Object>();
//
//      String tableName = entityTable.getMultiLanguageTableName();
//
//      StringBuilder languageSql = new StringBuilder("REPLACE  INTO " + tableName + "(");
//      for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
//        String columnName = field.getColumn();
//        keys.add(columnName);
//        try {
//          objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//          LOGGER.info(e.getMessage());
//        }
//      }
//      keys.add("LANG");
//      // 占位符
//      objs.add(null);
//
//      Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
//      List<EntityColumn> multiFields =
//          allFields.stream().filter(EntityColumn::isMultiLanguage).collect(toList());
//      for (EntityColumn field : multiFields) {
//        keys.add(field.getColumn());
//        Map<String, Map<String, String>> _tls = dto.get_tls();
//        if (_tls == null) {
//          // if multi language value not exists in __tls, then use value on current field
//          try {
//            objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
//          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            LOGGER.info(e.getMessage());
//          }
//          continue;
//        } else {
//          Map<String, String> tls = _tls.get(field.getColumn());
//          if (tls == null) {
//            // if multi language value not exists in __tls, then use value on current field
//            try {
//              objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//              LOGGER.info(e.getMessage());
//            }
//            continue;
//          }
//        }
//
//        // 占位符
//        objs.add(null);
//      }
//
//      languageSql.append(StringUtils.join(keys, ","));
//      languageSql.append(") VALUES ");
//      StringBuilder tmpSql = new StringBuilder();
//      List<Language> languages = LanguageHelper.languages();
//      for (Language language : languages) {
//        objs.set(objs.size() - multiFields.size() - 1, language.getCode());
//        for (AuditDomain auditDomain : list) {
//          tmpSql.append("(");
//          for (int i = 0; i < multiFields.size(); i++) {
//            int idx = objs.size() - multiFields.size() + i;
//            String property = multiFields.get(i).getProperty();
//            Map<String, Map<String, String>> _tls = auditDomain.get_tls();
//            if (null != _tls) {
//              Map<String, String> tls = _tls.get(multiFields.get(i).getProperty());
//              if (tls != null) {
//                try {
//                  objs.set(idx, PropertyUtils.getProperty(auditDomain, property));
//                } catch (IllegalAccessException
//                    | InvocationTargetException
//                    | NoSuchMethodException e) {
//                  LOGGER.info(e.getMessage());
//                }
//              }
//            } else {
//              try {
//                objs.set(idx, PropertyUtils.getProperty(auditDomain, property));
//              } catch (IllegalAccessException
//                  | InvocationTargetException
//                  | NoSuchMethodException e) {
//                LOGGER.info(e.getMessage());
//              }
//            }
//          }
//
//          // 更换kid
//          for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
//            try {
//              objs.set(0, PropertyUtils.getProperty(auditDomain, field.getProperty()));
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//              LOGGER.info(e.getMessage());
//            }
//          }
//          for (Object obj : objs) {
//            if (obj == null) {
//              tmpSql.append("''").append(",");
//            } else {
//              obj = ((String) obj).replace("'", "''");
//              tmpSql.append("'").append(obj).append("'").append(",");
//            }
//          }
//          tmpSql.deleteCharAt(tmpSql.length() - 1);
//          tmpSql.append("),");
//        }
//      }
//      tmpSql.deleteCharAt(tmpSql.length() - 1);
//      sqlList.add(languageSql.append(tmpSql).toString());
//    }
//    return sqlList;
//  }
//
//  //@Override
//  public String getDateSerializerSql(String input, Boolean isColumn) {
//    if (StringUtils.isEmpty(input) || isColumn == null) {
//      return "";
//    }
//    if (isColumn) {
//      return " DATE_FORMAT(" + input + ",'%Y-%m-%d %H:%i:%S') ";
//    } else {
//      return " DATE_FORMAT('" + input + "','%Y-%m-%d %H:%i:%S') ";
//    }
//  }
//
//  //@Override
//  public String getDateDeserializerSql(String columnName) {
//    if (StringUtils.isEmpty(columnName)) {
//      return "";
//    }
//    return " DATE_FORMAT(" + columnName + ",'%Y-%m-%d %H:%i:%S') ";
//  }
//
//  /**
//   * 私有方法，更新时更新多语言表
//   *
//   * @param dto
//   * @param updateFields
//   * @return
//   */
//  private List<String> getUpdateMultiLanguage(AuditDomain dto, List<String> updateFields) {
//    Class<?> entityClass = dto.getClass();
//    MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
//    Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
//    Set<EntityColumn> multiFields =
//        allFields.stream().filter(EntityColumn::isMultiLanguage).collect(Collectors.toSet());
//    if (multiLanguageTable == null || multiFields.size() == 0) {
//      return Collections.emptyList();
//    }
//
//    List<String> sqlList = new ArrayList<String>();
//    EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
//    String tableName = entityTable.getMultiLanguageTableName();
//
//    final StringBuilder firstSql = new StringBuilder("UPDATE " + tableName + " SET ");
//    final StringBuilder setSql = new StringBuilder();
//    final StringBuilder keySql = new StringBuilder();
//
//    // 获取主键拼接信息
//    for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
//      try {
//        Object value = PropertyUtils.getProperty(dto, field.getProperty());
//        if (value != null) {
//          if (value instanceof String) {
//            keySql
//                .append(field.getColumn())
//                .append("=")
//                .append("'")
//                .append(value.toString())
//                .append("'")
//                .append(" AND ");
//          } else if (value instanceof Date) {
//            keySql
//                .append(field.getColumn())
//                .append("=")
//                .append("'")
//                .append(DATE_FORMAT.format(value))
//                .append("'")
//                .append(" AND ");
//          } else {
//            keySql.append(field.getColumn()).append("=").append(value).append(" AND ");
//          }
//        }
//      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        LOGGER.info(e.getMessage());
//      }
//    }
//
//    if (keySql.length() == 0) {
//      return Collections.emptyList();
//    }
//
//    // 没有特殊备注TLS信息时，默认只更新当前语言环境的数据
//    if (null == dto.get_tls() || dto.get_tls().isEmpty()) {
//      for (EntityColumn column : multiFields) {
//        if (CollectionUtils.isNotEmpty(updateFields)
//            && updateFields.stream().noneMatch(t -> t.equalsIgnoreCase(column.getProperty()))) {
//          continue;
//        }
//
//        try {
//          Object value = PropertyUtils.getProperty(dto, column.getProperty());
//          if (value != null) {
//            value = ((String) value).replace("'", "''");
//            setSql
//                .append(column.getColumn())
//                .append("=")
//                .append("'")
//                .append(value.toString())
//                .append("'")
//                .append(",");
//          }
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//          LOGGER.info(e.getMessage());
//        }
//      }
//
//      if (setSql.length() == 0) {
//        return Collections.emptyList();
//      }
//      // 删除末尾逗号
//      setSql.deleteCharAt(setSql.length() - 1);
//
//      setSql.append(" WHERE ");
//      setSql.append("LANG='").append(MtLanguageHelper.language()).append("' AND ");
//      // 拼接主键信息
//      sqlList.add(
//          String.valueOf(firstSql)
//              + setSql
//              + keySql.substring(0, keySql.length() - " AND ".length()));
//    }
//    // 特殊备注TLS信息时，根据TLS信息进行多语言环境的数据修改
//    else {
//      List<MtMultiLanguageVO> infos = new ArrayList<MtMultiLanguageVO>();
//      for (Map.Entry<String, Map<String, String>> columns : dto.get_tls().entrySet()) {
//        for (Map.Entry<String, String> tls : columns.getValue().entrySet()) {
//          // 获取需要修改的语言环境
//          infos.add(
//              new MtMultiLanguageVO(
//                  FieldNameUtils.camel2Underline(columns.getKey(), true),
//                  tls.getKey(),
//                  tls.getValue()));
//        }
//      }
//      List<String> langs =
//          infos.stream().map(MtMultiLanguageVO::getLang).distinct().collect(toList());
//      // 拼接不同语言环境的数据
//      for (String lang : langs) {
//        // 获取不同语言环境的数据
//        List<MtMultiLanguageVO> langInfo =
//            infos.stream().filter(t -> lang.equals(t.getLang())).collect(toList());
//        // 拼接修改的值
//        setSql.delete(0, setSql.length());
//        for (MtMultiLanguageVO ever : langInfo) {
//          String value = ever.getValues().replace("'", "''");
//          setSql
//              .append(ever.getColoumName())
//              .append("=")
//              .append("'")
//              .append(value)
//              .append("'")
//              .append(",");
//        }
//        // 删除末尾逗号
//        setSql.deleteCharAt(setSql.length() - 1);
//
//        setSql.append(" WHERE ");
//        setSql.append("LANG='").append(lang).append("' AND ");
//        // 拼接主键信息
//        sqlList.add(
//            String.valueOf(firstSql)
//                + setSql
//                + keySql.substring(0, keySql.length() - " AND ".length()));
//      }
//    }
//    return sqlList;
//  }
//}
