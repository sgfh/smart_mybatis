package com.smart.mybatis.database;

import com.smart.mybatis.annotation.Column;
import com.smart.mybatis.annotation.GeneratedValue;
import com.smart.mybatis.annotation.Id;
import com.smart.mybatis.annotation.Table;
import com.smart.mybatis.pojo.TableField;
import com.smart.mybatis.util.DataSourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 自动创建数据库表
 */
public class DatabaseManager {
    protected static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    public DatabaseManager() {

    }

    private static final String CLASS_SUFFIX = ".class";
    private static final String CLASS_FILE_PREFIX = File.separator + "classes" + File.separator;
    private static final String PACKAGE_SEPARATOR = ".";

    public void init(String url, String[] packageNames, String username, String password) {
        Date begin = new Date();
        for (String packageName:packageNames) {
            logger.info("当前同步包:"+packageName);
            //获取包下所有class类，获取到后，扫描注解，完成数据库表生成
            List<String> classList = getClazzName(packageName, false);
            logger.info("classList=========="+classList.size());
            for (String className : classList) {
                String valueName;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    Object object = Class.forName(className).newInstance();
                    if (object.getClass().getAnnotation(Table.class) == null)
                        continue;
                    if (!object.getClass().getAnnotation(Table.class).isCreate())
                        continue;

                    String tableName = object.getClass().getAnnotation(Table.class).value();
                    int tableCount = object.getClass().getAnnotation(Table.class).tableCount();
                    String rule = object.getClass().getAnnotation(Table.class).rule();
                    //保存已存在的table名称
                    Map<String, String> tableMap = new HashMap<>();
                    for (int c = 1; c <= tableCount; c++) {
                        String localTableName = tableName;
                        if (tableCount > 1)
                            localTableName = tableName + rule + c;
                        if (isTableExist(url, username, password, "SELECT * FROM information_schema.TABLES WHERE TABLE_SCHEMA=(SELECT DATABASE()) AND `table_name` ='" + localTableName + "'")) {
                            String excuteAddSql = "";
                            String excuteModifySql = "";
                            //表已经存在
                            List<TableField> map = findTableFields(url, username, password, "SELECT column_name,column_type,column_default FROM information_schema.columns WHERE `table_name` ='" + localTableName + "'" + " AND TABLE_SCHEMA='" + (url.split("//")[1].split("/")[1].split("\\?")[0]).toLowerCase() + "'");
                            //遍历属性，如果在结果集中不存在，则需要添加字段
                            Field[] superFields = object.getClass().getSuperclass().getDeclaredFields();
                            excuteAddSql += addFiledSql(superFields, map);
                            excuteModifySql += addModifyFiledSql(superFields, map);
                            Field[] fields = object.getClass().getDeclaredFields();
                            excuteAddSql += addFiledSql(fields, map);
                            excuteModifySql += addModifyFiledSql(fields, map);
                            if (excuteAddSql.length() != 0) {
                                excuteAddSql = excuteAddSql.substring(0, excuteAddSql.length() - 1);
                                excuteAddSql = "ALTER TABLE " + localTableName + excuteAddSql;
                                logger.info("localTableName========" + localTableName);
                                executeSql(excuteAddSql, url, username, password);
                            }
                            if (excuteModifySql.length() != 0) {
                                excuteModifySql = "ALTER TABLE " + localTableName + excuteModifySql.substring(0, excuteModifySql.length() - 1);
                                executeSql(excuteModifySql, url, username, password);
                            }
                            tableMap.put(localTableName, localTableName);
                        }
                    }
                    //保存索引集合
                    List<String> indexList = new ArrayList<>();
                    //父类，此时扫描出父类的注解
                    Field[] superFields = object.getClass().getSuperclass().getDeclaredFields();
                    for (Field superField : superFields) {
                        if (superField.getAnnotation(GeneratedValue.class) != null)
                            stringBuilder.append(superField.getAnnotation(Id.class).value()).append(" ").append(superField.getAnnotation(Id.class).columnDefinition()).append(" ").append("AUTO_INCREMENT PRIMARY KEY,");

                        if (superField.getAnnotation(Column.class) != null)
                            stringBuilder.append(superField.getAnnotation(Column.class).value()).append(" ").append(superField.getAnnotation(Column.class).columnDefinition()).append(isNull(superField.getAnnotation(Column.class).isNull())).append(",");

                        if (superField.getAnnotation(Column.class) != null && superField.getAnnotation(Column.class).index())
                            indexList.add(superField.getAnnotation(Column.class).value());
                    }
                    Field[] fields = object.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        Column column = field.getAnnotation(Column.class);
                        if (column != null)
                            stringBuilder.append(column.value()).append(" ").append(column.columnDefinition()).append(isNull(column.isNull())).append(",");
                        if (column != null && column.index())
                            indexList.add(column.value());
                    }
                    valueName = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
                    if (tableCount == 1) {
                        if (tableMap.get(tableName) != null && !"".equals(tableName))
                            continue;
                        String executeSql = "CREATE TABLE " + tableName + "(" + valueName + ")";
                        //表不存在，直接生成
                        executeSql(executeSql, url, username, password);
                        //新建索引
                        logger.info("indexList.size()======" + indexList.size());
                        if (indexList.size() > 0) {
                            for (String s : indexList)
                                executeSql("ALTER TABLE " + tableName + " ADD index " + s + "(" + s + "); ", url, username, password);
                        }
                    } else {
                        for (int count = 0; count < tableCount; count++) {
                            String lTableName = (tableName + rule + (count + 1));
                            if (tableMap.get(lTableName) != null && !"".equals(lTableName))
                                continue;
                            String executeSql = "CREATE TABLE " + lTableName + "(" + valueName + ")";
                            //表不存在，直接生成
                            executeSql(executeSql, url, username, password);
                            if (indexList.size() > 0) {
                                for (String s : indexList)
                                    executeSql("ALTER TABLE " + tableName + " ADD index " + s + "(" + s + "); ", url, username, password);
                            }
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Date end = new Date();
        logger.info("====>>init smart table time:" + (end.getTime() - begin.getTime()) + "ms");
    }

    /**
     * 添加索引
     */
    private void addIndex(boolean isCheck, String url, String tableName, String column, String username, String password) {
        if (!isCheck) {
            String executeSql = "ALTER TABLE '" + tableName + "' ADD INDEX index_name('" + column + "'); ";
            //表不存在，直接生成
            executeSql(executeSql, url, username, password);
        }
    }

    /**
     * 判断字段null
     */
    private String isNull(boolean isNull) {
        return " " + (isNull ? "NULL" : "NOT NULL");
    }

    /**
     * 组装属性sql
     */
    private String addFiledSql(Field[] fields, List<TableField> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                String keyColumn = field.getAnnotation(Column.class).value();
                if (!isSameFieldName(map, keyColumn))
                    stringBuilder.append(" ADD ").append(keyColumn).append(" ").append(field.getAnnotation(Column.class).columnDefinition()).append(isNull(field.getAnnotation(Column.class).isNull())).append(",");

            }

        }
        return stringBuilder.toString();
    }

    /**
     * 获取相同字段不同类型
     */
    private String addModifyFiledSql(Field[] fields, List<TableField> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                String keyColumn = field.getAnnotation(Column.class).columnDefinition() + (field.getAnnotation(Column.class).isNull() ? "NULL" : "NOT NULL");
                if (isSameFieldName(map, field.getAnnotation(Column.class).value()) && !isSameFieldType(map, keyColumn))
                    stringBuilder.append(" MODIFY ").append(field.getAnnotation(Column.class).value()).append(" ").append(field.getAnnotation(Column.class).columnDefinition()).append(isNull(field.getAnnotation(Column.class).isNull())).append(",");
            }

        }
        return stringBuilder.toString();
    }

    /**
     * 判断属性名称是否一致
     */
    private boolean isSameFieldName(List<TableField> map, String keyColumn) {
        for (TableField tableField : map) {
            if (tableField.getColumnName().equals(keyColumn))
                return true;

        }
        return false;
    }

    /**
     * 判断属性类型是否一致
     */
    private boolean isSameFieldType(List<TableField> map, String keyColumn) {
        for (TableField tableField : map) {
            if (keyColumn.contains(tableField.getColumnType().toUpperCase()))
                return true;

        }
        return false;
    }

    /**
     * 查询表中否存在
     */
    private boolean isTableExist(String url, String username, String password, String executeSql) {
        boolean flag = false;
        try {
            Connection con = DataSourceUtil.getConnection(url, username, password);
            Statement sm = con.createStatement();   //创建对象
            logger.info("====>>" + executeSql);
            sm.execute(executeSql);

            ResultSet resultSet = sm.getResultSet();
            if (resultSet.next())
                flag = true;
            sm.close();
            DataSourceUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询表属性
     */
    private List<TableField> findTableFields(String url, String username, String password, String executeSql) {
        List<TableField> map = null;
        try {
            Connection con = DataSourceUtil.getConnection(url, username, password);
            Statement sm = con.createStatement();   //创建对象
            logger.info("====>>" + executeSql);
            sm.execute(executeSql);

            ResultSet resultSet = sm.getResultSet();
            map = convertList(resultSet);
            sm.close();
            DataSourceUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 将数据集合存放到list集合中
     */
    private static List<TableField> convertList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        List<TableField> tableFieldList = new ArrayList<>();
        while (rs.next()) {

            for (int i = 1; i <= columnCount; i++) {
                TableField tableField = new TableField();
                tableField.setColumnName(rs.getString("COLUMN_NAME"));
                tableField.setColumnType(rs.getString("COLUMN_TYPE"));
                tableField.setColumnDefault("COLUMN_DEFAULT");
                tableField.setIsNullable("IS_NULLABLE");
                tableFieldList.add(tableField);
            }
        }
        return tableFieldList;
    }


    /**
     * 执行sql语句
     */
    private void executeSql(String executeSql, String url, String username, String password) {
        try {
            logger.info("====>>" + executeSql);
            Connection con = getConnection(url, username, password);
            Statement sm = con.createStatement();   //创建对象
            sm.execute(executeSql);
            sm.close();
            con.close();
            //   return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    /**
     * 获取数据库链接
     */
    private static Connection getConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    /**
     * 26
     * 查找包下的所有类的名字
     * 27
     *
     * @param packageName          28
     * @param showChildPackageFlag 是否需要显示子包内容
     *                             29
     * @return List集合，内容为类的全名
     * 30
     */
    private List<String> getClazzName(String packageName, boolean showChildPackageFlag) {
        List<String> result = new ArrayList<>();
        String suffixPath = packageName.replaceAll("\\.", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        logger.info("suffixPath====="+suffixPath);
        try {
            Enumeration<URL> urls = loader.getResources(suffixPath);

            while (urls.hasMoreElements()) {
                System.out.println("urls=========================");
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String path = url.getPath();
                        result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));
                    } else if ("jar".equals(protocol)) {
                        JarFile jarFile = null;
                        try {
                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jarFile != null) {
                            result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 反射获取包名称
     */
    private static void doClassName(String path, List<String> result) {
        // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
        if (path.endsWith(CLASS_SUFFIX)) {
            path = path.replace(CLASS_SUFFIX, "");
            // 从"/classes/"后面开始截取
            String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                    .replace(File.separator, PACKAGE_SEPARATOR);
            if (!clazzName.contains("$")) {
                result.add(clazzName);
            }
        }
    }

    /**
     * 66
     * 递归获取所有class文件的名字
     * 67
     *
     * @param file 68
     * @param flag 是否需要迭代遍历
     *             69
     * @return List
     * 70
     */
    private static List<String> getAllClassNameByFile(File file, boolean flag) {
        List<String> result = new ArrayList<>();
        if (!file.exists()) {
            return result;
        }
        if (file.isFile()) {
            String path = file.getPath();
            // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
            doClassName(path, result);

            return result;

        } else {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    if (flag) {
                        result.addAll(getAllClassNameByFile(f, flag));
                    } else {
                        if (f.isFile()) {
                            String path = f.getPath();
                            doClassName(path, result);
                        }

                    }
                }
            }
            return result;
        }
    }


    /**
     * 递归获取jar所有class文件的名字
     *
     * @param jarFile
     * @param packageName 包名
     * @param flag        是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {
        List<String> result = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if (flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if (name.startsWith(packageName) && !name.contains("$"))
                        result.add(name);

                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if (packageName.equals(name.substring(0, name.lastIndexOf("."))) && !name.contains("$"))
                        result.add(name);

                }
            }
        }
        return result;
    }
}
