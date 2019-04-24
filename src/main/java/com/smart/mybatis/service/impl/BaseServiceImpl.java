package com.smart.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart.mybatis.annotation.*;
import com.smart.mybatis.database.TableConstants;
import com.smart.mybatis.mapper.BaseMapper;
import com.smart.mybatis.page.PageBean;
import com.smart.mybatis.page.Pageable;
import com.smart.mybatis.pojo.GroupBy;
import com.smart.mybatis.pojo.Like;
import com.smart.mybatis.pojo.OrderBy;
import com.smart.mybatis.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.*;
import java.util.*;

import static com.smart.mybatis.database.TableConstants.UPDATE;
@Transactional
@Service
public class BaseServiceImpl<T> implements BaseService<T> {


    @Autowired
    private BaseMapper baseMapper;

    @Override
    public int insert(T entity) {
        Map<String, Object> param = transformObj(entity, TableConstants.INSERT, null, null, null);
        if (null == param)
            return 0;
        int num = baseMapper.insert(param);
        if (num > 0) {
            Long keyId = (Long) param.get("id");
            addKeyId(entity, keyId);
        }
        return num;
    }

    @Override
    public int update(T entity) {
        return baseMapper.update(transformObj(entity, UPDATE, null, null, null));
    }

    @Override
    public int delete(T entity) {
        return baseMapper.delete(transformObj(entity, TableConstants.COMMON, null, null, null));
    }

    @Override
    public Object findById(Long id, Class<T> cls) {
        T entity = null;
        try {
            entity = newTclass(cls);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Field[] fields = cls.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null)
                setFieldValueByFieldName(field.getName(), entity, id);
        }
        if (null == entity)
            return null;
        return doR(entity, baseMapper.findById(transformObj(entity, TableConstants.COMMON, null, null, null)));
    }

    @Override
    public List<T> list(T entity, Class<T> cls) {
        //return map2List(entity, baseMapper.list(transformObj(entity, "common")), cls);
        return map2LinkList(baseMapper.findLinkListT(transformObj(entity, TableConstants.LINK, null, null, null)), cls);
    }

    @Override
    public Object find(T entity) {
        //return doR(entity, baseMapper.find(transformObj(entity, "common")));
        return linkTMap(entity, baseMapper.findLinkT(transformObj(entity, TableConstants.LINK, null, null, null)));
    }

    @Override
    public Object findLinkT(T entity) {
        return linkTMap(entity, baseMapper.findLinkT(transformObj(entity, TableConstants.LINK, null, null, null)));
    }

    @Override
    public List<T> findLinkListT(T entity, Class<T> cls) {
        return map2LinkList(baseMapper.findLinkListT(transformObj(entity, TableConstants.LINK, null, null, null)), cls);
    }

    @Override
    public PageInfo<T> page(Pageable pageable, T entity, Class<T> cls) {
        Integer pageNo = pageable.getPageNumber();
        Integer pageSize = pageable.getPageSize();
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? PageBean.DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNo, pageSize);
        List<T> list = list(entity, cls);
        PageInfo<T> page = new PageInfo<>(list);
        page.setList(list);
        return page;
    }

    @Override
    public PageInfo<T> page(Pageable pageable, T entity, Class<T> cls, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes) {
        Integer pageNo = pageable.getPageNumber();
        Integer pageSize = pageable.getPageSize();
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? PageBean.DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNo, pageSize);
        List<T> list = findOrderLinkT(entity, cls, orderList, groupByList, likes);
        PageInfo<T> page = new PageInfo<>(list);
        page.setList(list);
        return page;
    }

    @Override
    public List<T> findOrderLinkT(T entity, Class<T> cls, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes) {
        return map2LinkList(baseMapper.findOrderLinkT(transformObj(entity, TableConstants.LINK, orderList, groupByList, likes)), cls);
    }

    @Override
    public Integer count(T entity, Class<T> cls) {
        Integer num = baseMapper.count(transformObj(entity, TableConstants.LINK, null, null, null));
        return num == null ? 0 : num;
    }

    private static <T> T newTclass(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    /**
     * 整合集合map-多表关联
     */
    private List<T> map2LinkList(List<Map<String, Object>> mapList, Class<T> cls) {
        List<T> list = new ArrayList<>();
        for (Map<String, Object> params : mapList) {
            try {
                T t = newTclass(cls);
                list.add(linkTMap(t, params));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return list;
    }


    /**
     * 查询get方法获取到的属性值
     */
    private Object getFieldValue(Object object, Field field) {
        try {
            Method m = object.getClass().getMethod(
                    "get" + getMethodName(field.getName()));
            return m.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注入主键--insert时候使用
     */
    private void addKeyId(T cls, Long id) {
        // 拿到该类
        Class<?> clz = cls.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            //判断是否存在标签
            if (field.getAnnotation(Id.class) == null)
                continue;
            setFieldValueByFieldName(field.getName(), cls, id);
        }
    }

    /**
     * 扫描类属性，注入查询值
     */
    private Map<String, Object> transformObj(Object t, String type, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes) {
        //获取表名
        if (null == t.getClass().getAnnotation(Table.class))
            return null;

        Map<String, Object> re = new LinkedHashMap<>();
        re.put(TableConstants.TABLE_NAME, t.getClass().getAnnotation(Table.class).value());
        // 拿到该类
        Class<?> clz = t.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        //获取父类id属性
        Field[] superFields = clz.getSuperclass().getDeclaredFields();
        for (Field field : superFields) {
            if (null != field.getAnnotation(Id.class) && getFieldValue(t, field) != null) {
                re.put(TableConstants.KEY_ID, field.getAnnotation(Id.class).value());
                re.put(TableConstants.KEY_VALUE, getFieldValue(t, field));
                continue;
            }

            if (null != field.getAnnotation(Sql.class) && getFieldValue(t, field) != null)
                re.put("SQL", getFieldValue(t, field));

        }

        if (TableConstants.INSERT.equals(type)) {
            List<Object> keys = new ArrayList<>();//存放列名
            List<Object> values = new ArrayList<>();//存放列值
            for (Field field : superFields) {
                if (null != field.getAnnotation(Column.class) && getFieldValue(t, field) != null) {
                    keys.add(field.getAnnotation(Column.class).value());
                    values.add(getFieldValue(t, field));
                }
            }
            for (Field field : fields) {
                //判断是否存在标签
                if (field.getAnnotation(Column.class) != null) {
                    //列不为空
                    keys.add(field.getAnnotation(Column.class).value());
                    values.add(getFieldValue(t, field));
                }
            }

            re.put(TableConstants.COLUMNS, keys);
            re.put(TableConstants.VALUES, values);
        } else if (UPDATE.equals(type)) {
            List<Map<String, Object>> d = new ArrayList<>();
            Field[] superF = clz.getSuperclass().getDeclaredFields();
            for (Field field : superF) {
                if (null != field.getAnnotation(Column.class) && getFieldValue(t, field) != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(TableConstants.COLUMN, field.getAnnotation(Column.class).value());
                    map.put(TableConstants.COL_VALUE, getFieldValue(t, field));
                    d.add(map);
                }
            }
            for (Field field : fields) {
                //判断是否存在标签
                if (field.getAnnotation(Column.class) != null && getFieldValue(t, field) != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(TableConstants.COLUMN, field.getAnnotation(Column.class).value());
                    map.put(TableConstants.COL_VALUE, getFieldValue(t, field));
                    d.add(map);
                }

            }
            re.put(TableConstants.DATA, d);
        } else if (TableConstants.COMMON.equals(type)) {
            List<Map<String, Object>> data = new ArrayList<>();
            for (Field field : fields) {
                Map<String, Object> map = new HashMap<>();
                if (null != field.getAnnotation(Column.class) && getFieldValue(t, field) != null) {
                    map.put(TableConstants.COLUMN, field.getAnnotation(Column.class).value());
                    map.put(TableConstants.COL_VALUE, getFieldValue(t, field));
                    data.add(map);
                }
            }
            Map<String, Object> idMap = new HashMap<>();
            if (re.get(TableConstants.KEY_VALUE) != null) {
                idMap.put(TableConstants.COLUMN, re.get(TableConstants.KEY_ID));
                idMap.put(TableConstants.COL_VALUE, re.get(TableConstants.KEY_VALUE));
                data.add(idMap);
            }
            re.put(TableConstants.DATA, data);
            //如果查询条件中存在id字段，将id字段放在第一个，这么做是为了让sql首先调用主键索引
            for (int index = 0; index < data.size(); index++) {
                Map<String, Object> map = data.get(index);
                if (null == map)
                    continue;
                if (map.get(TableConstants.COLUMN) != null && map.get(TableConstants.COLUMN).equals("id") && data.size() > 1)
                    Collections.swap(data, 0, index);

            }

        } else if (TableConstants.LINK.equals(type)) {
            String tableName = t.getClass().getAnnotation(Table.class).value();
            List<Map<String, Object>> data = new ArrayList<>();
            List<Map<String, Object>> queryData = new ArrayList<>();

            //保存扫描出来的field
            re.put(TableConstants.MAIN_TABLE, tableName);
            StringBuilder stringBuilder = new StringBuilder();
            // String fileds = "";
            stringBuilder.append(addTableFields(clz, t.getClass().getAnnotation(Table.class).value()));
            //获取父类中查询参数
            for (Field field : superFields) {
                addParamVal(field, t, tableName, queryData);
            }
            for (Field field : fields) {
                if (null != field.getAnnotation(OneToOne.class)) {
                    //保存table集合map
                    Map<String, Object> tableMap = new LinkedHashMap<>();
                    Class<?> cls = field.getType();
                    String table = cls.getAnnotation(Table.class).value();
                    tableMap.put(TableConstants.TABLE_NAME, table);
                    //on字段约束条件
                    tableMap.put("ON_FIELD", field.getAnnotation(OneToOne.class).value());
                    data.add(tableMap);
                    stringBuilder.append(",").append(addTableFields(cls, table));
                } else
                    addParamVal(field, t, tableName, queryData);

            }
            if (null != re.get(TableConstants.KEY_ID))
                re.put(TableConstants.KEY_ID, tableName + "." + re.get(TableConstants.KEY_ID));

            re.put(TableConstants.DATA, data);
            re.put(TableConstants.QUERY_DATA, queryData);
            re.put(TableConstants.SCAN_FIELDS, stringBuilder.toString());
            if (null != orderList)
                re.put("ORDERS", orderList);
            if (null != groupByList)
                re.put("GROUPBYS", groupByList);
            if (null != likes)
                re.put("LIKES", likes);
        }
        return re;
    }


    /**
     * 给param注入值
     */
    private void addParamVal(Field field, Object t, String tableName, List<Map<String, Object>> queryData) {
        if (null != field.getAnnotation(Column.class) && getFieldValue(t, field) != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(TableConstants.COLUMN, tableName + "." + field.getAnnotation(Column.class).value());
            map.put(TableConstants.COL_VALUE, getFieldValue(t, field));
            queryData.add(map);
        }
    }

    /**
     * 获取主表属性对应数据库
     */
    private static String addTableFields(Class<?> clz, String table) {
        StringBuilder stringBuilder = new StringBuilder();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        //获取父类属性
        Field[] superFields = clz.getSuperclass().getDeclaredFields();
        for (Field superField : superFields) {
            if (superField.getAnnotation(Id.class) != null)
                stringBuilder.append(table).append(".").append(superField.getAnnotation(Id.class).value()).append(" AS ").append(table).append("_").append(superField.getName()).append(",");
            if (superField.getAnnotation(Column.class) != null)
                stringBuilder.append(table).append(".").append(superField.getAnnotation(Column.class).value()).append(" AS ").append(table).append("_").append(superField.getAnnotation(Column.class).value()).append(",");
        }
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null)
                stringBuilder.append(table).append(".").append(field.getAnnotation(Column.class).value()).append(" AS ").append(table).append("_").append(field.getAnnotation(Column.class).value()).append(",");
        }
        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

    private String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    private T doR(T obj, Map<String, Object> params) {
        if (params == null || params.size() == 0)
            return null;
        // 拿到该类
        Class<?> clz = obj.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();

        Field[] superFields = clz.getSuperclass().getDeclaredFields();
        for (Field field : superFields) {
            if (field.getAnnotation(Id.class) != null)
                setFieldValueByFieldName(field.getName(), obj, params.get(field.getAnnotation(Id.class).value()));

            if (field.getAnnotation(Column.class) != null)
                setFieldValueByFieldName(field.getName(), obj, params.get(field.getAnnotation(Column.class).value()));
        }


        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null)
                setFieldValueByFieldName(field.getName(), obj, params.get(field.getAnnotation(Column.class).value()));
        }

        return obj;
    }

    /**
     * 一对一map映射
     */
    private T linkTMap(T obj, Map<String, Object> params) {
        if (params == null || params.size() == 0)
            return null;
        // 拿到该类
        Class<?> clz = obj.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();

        Field[] superFields = clz.getSuperclass().getDeclaredFields();
        String tableName = clz.getAnnotation(Table.class).value();
        Object id = null;
        for (Field field : superFields) {
            if (field.getAnnotation(Id.class) != null) {
                id = params.get(tableName + "_" + field.getAnnotation(Id.class).value());
                setFieldValueByFieldName(field.getName(), obj, id);
            }

            if (field.getAnnotation(Column.class) != null)
                setFieldValueByFieldName(field.getName(), obj, params.get(tableName + "_" + field.getAnnotation(Column.class).value()));
        }


        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                setFieldValueByFieldName(field.getName(), obj, params.get(tableName + "_" + field.getAnnotation(Column.class).value()));
                continue;
            }
            if (field.getAnnotation(OneToOne.class) != null) {
                try {
                    String classType = field.getType().toString().replace(" ", "");
                    classType = classType.replace("class", "");
                    Object newClass = Class.forName(classType).newInstance();
                    addViceField(newClass, params);
                    setFieldValueByFieldName(field.getName(), obj, newClass);
                } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            } else if (field.getAnnotation(OneToMany.class) != null) {
                if (null == id)
                    continue;
                Type t = field.getGenericType();
                try {
                    String pojoClassName = t.toString().replace("java.util.List<", "").replace(">", "");
                    Object newClass = Class.forName(pojoClassName).newInstance();
                    Field[] suFields = newClass.getClass().getDeclaredFields();
                    for (Field f : suFields) {
                        if (f.getAnnotation(Column.class) == null)
                            continue;
                        if (field.getAnnotation(OneToMany.class).value().equals(f.getAnnotation(Column.class).value()))
                            setFieldValueByFieldName(f.getName(), newClass, id);

                    }
                    List<Map<String, Object>> mapList = baseMapper.findLinkListT(transformObj(newClass, TableConstants.LINK, null, null, null));
                    List<Object> objectList = parseList(mapList, newClass);
                    setFieldValueByFieldName(field.getName(), obj, objectList);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }

        return obj;
    }


    /**
     * 一对一map映射
     */
    private Object linkTMapV2(Object obj, Map<String, Object> params) {
        if (params == null || params.size() == 0)
            return null;
        // 拿到该类
        Class<?> clz = obj.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();

        Field[] superFields = clz.getSuperclass().getDeclaredFields();
        String tableName = clz.getAnnotation(Table.class).value();
        Object id = null;
        for (Field field : superFields) {
            if (field.getAnnotation(Id.class) != null) {
                id = params.get(tableName + "_" + field.getAnnotation(Id.class).value());
                setFieldValueByFieldName(field.getName(), obj, id);
            }

            if (field.getAnnotation(Column.class) != null)
                setFieldValueByFieldName(field.getName(), obj, params.get(tableName + "_" + field.getAnnotation(Column.class).value()));
        }


        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                setFieldValueByFieldName(field.getName(), obj, params.get(tableName + "_" + field.getAnnotation(Column.class).value()));
                continue;
            }
            if (field.getAnnotation(OneToOne.class) != null) {
                try {
                    String classType = field.getType().toString().replace(" ", "");
                    classType = classType.replace("class", "");
                    Object newClass = Class.forName(classType).newInstance();
                    addViceField(newClass, params);
                    setFieldValueByFieldName(field.getName(), obj, newClass);
                } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            } else if (field.getAnnotation(OneToMany.class) != null) {
                if (null == id)
                    continue;
                Type t = field.getGenericType();
                try {
                    String pojoClassName = t.toString().replace("java.util.List<", "").replace(">", "");
                    Object newClass = Class.forName(pojoClassName).newInstance();
                    Field[] suFields = newClass.getClass().getDeclaredFields();
                    for (Field f : suFields) {
                        if (f.getAnnotation(Column.class) == null)
                            continue;
                        if (field.getAnnotation(OneToMany.class).value().equals(f.getAnnotation(Column.class).value()))
                            setFieldValueByFieldName(f.getName(), newClass, id);

                    }
                    List<Map<String, Object>> mapList = baseMapper.findLinkListT(transformObj(newClass, TableConstants.LINK, null, null, null));
                    List<Object> objectList = parseList(mapList, newClass);
                    setFieldValueByFieldName(field.getName(), obj, objectList);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }

        return obj;
    }

    /**
     * 一对多，动态塞入参数，用于表之间关联
     */
    private List<Object> parseList(List<Map<String, Object>> mapList, Object newClass) {
        List<Object> objectList = new ArrayList<>();
        for (Map<String, Object> param : mapList) {
            Class cls=newClass.getClass();
            try {
                objectList.add(linkTMapV2(cls.newInstance(), param));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    /**
     * 对于副表onetoone属性封装
     */
    private void addViceField(Object object, Map<String, Object> params) {
        Class<?> clz = object.getClass();
        Field[] fields = clz.getDeclaredFields();
        Field[] superFields = clz.getSuperclass().getDeclaredFields();
        String tableName = clz.getAnnotation(Table.class).value();
        for (Field field : superFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod))
                continue;

            if (field.getAnnotation(Id.class) != null) {
                try {
                    Field field1 = clz.getSuperclass().getDeclaredField(field.getName());
                    field1.setAccessible(true);
                    field1.set(object, params.get(tableName + "_" + field.getAnnotation(Id.class).value()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (field.getAnnotation(Column.class) != null) {
                try {
                    Field field1 = clz.getSuperclass().getDeclaredField(field.getName());
                    field1.setAccessible(true);
                    field1.set(object, params.get(tableName + "_" + field.getAnnotation(Column.class).value()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null)
                setFieldValueByFieldName(field.getName(), object, params.get(tableName + "_" + field.getAnnotation(Column.class).value()));

        }
    }


    /**
     * 根据属性名设置属性值
     *
     * @param fieldName:属性名称
     * @param object:object
     */
    private void setFieldValueByFieldName(String fieldName, Object object, Object value) {
        try {
            // 获取obj类的字节文件对象
            Class c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(object, value);
        } catch (Exception e) {
            //注入是比说明该属性不存在于子类中，此时需要向父类属性进行扫描
            getSuperField(object, fieldName, value);
        }
    }

    /**
     * 通过反射获取父类的值，并注入值
     */
    private void getSuperField(Object paramClass, String paramString, Object value) {
        try {
            Field field = paramClass.getClass().getSuperclass().getDeclaredField(paramString);
            field.setAccessible(true);
            field.set(paramClass, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}