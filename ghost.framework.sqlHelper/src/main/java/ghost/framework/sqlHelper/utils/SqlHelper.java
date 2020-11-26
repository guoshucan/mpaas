package ghost.framework.sqlHelper.utils;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.config.CompositeIndex;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.SingleIndex;
import cn.craccd.sqlHelper.config.Table;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;

/**
 * mongodb操作器
 *
 */
@Service
public class SqlHelper {
	@Value("${spring.database.type}")
	String database;
	@Value("${spring.database.package}")
	String packageName;
	@Value("${spring.database.print:false}")
	Boolean print;

	@Autowired
	JdbcTemplate jdbcTemplate;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	SnowFlake snowFlake = new SnowFlake(1, 1);

	String separator = System.getProperty("line.separator");

	@PostConstruct
	private void scan() {
		if (StrUtil.isEmpty(packageName)) {
			return;
		}

		Set<Class<?>> set = ClassUtil.scanPackage(packageName);
		for (Class<?> clazz : set) {
			Table table = clazz.getAnnotation(Table.class);
			if (table != null) {
				// 创建表
				checkOrCreateTable(clazz);

				// 获取表所有字段
				String sql = "";
				if (database.equals("sqlite")) {
					sql = "PRAGMA TABLE_INFO(`" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`)";
				} else if (database.equals("mysql")) {
					sql = "SHOW COLUMNS FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
				} else if (database.equals("postgresql")) {
					sql = "SELECT column_name as name FROM information_schema.columns " + //
							"WHERE table_schema='public' AND table_name='" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "'";
				}
				logQuery(formatSql(sql));
				List<Map<String, Object>> columns = jdbcTemplate.queryForList(formatSql(sql));

				// 获取表所有索引
				if (database.equals("sqlite")) {
					sql = "PRAGMA INDEX_LIST(`" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`)";
				} else if (database.equals("mysql")) {
					sql = "SHOW INDEX FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
				} else if (database.equals("postgresql")) {
					sql = "SELECT " + //
							"A.INDEXNAME as name " + //
							"FROM PG_AM B " + //
							"LEFT JOIN PG_CLASS F ON B.OID = F.RELAM " + //
							"LEFT JOIN PG_STAT_ALL_INDEXES E ON F.OID = E.INDEXRELID " + //
							"LEFT JOIN PG_INDEX C ON E.INDEXRELID = C.INDEXRELID " + //
							"LEFT OUTER JOIN PG_DESCRIPTION D ON C.INDEXRELID = D.OBJOID, " + //
							"PG_INDEXES A " + //
							"WHERE " + //
							"A.SCHEMANAME = E.SCHEMANAME AND A.TABLENAME = E.RELNAME AND A.INDEXNAME = E.INDEXRELNAME " + //
							"AND E.SCHEMANAME = 'public' AND E.RELNAME = '" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "' ";//
				}
				logQuery(formatSql(sql));
				List<Map<String, Object>> indexs = jdbcTemplate.queryForList(formatSql(sql));

				// 建立字段
				Field[] fields = ReflectUtil.getFields(clazz);
				for (Field field : fields) {
					// 创建字段
					if (!field.getName().equals("id")) {
						checkOrCreateColumn(clazz, field.getName(), columns);
					}

					// 创建索引
					if (field.isAnnotationPresent(SingleIndex.class)) {
						SingleIndex singleIndex = field.getAnnotation(SingleIndex.class);
						checkOrCreateIndex(clazz, field.getName(), singleIndex.unique(), indexs);
					}

					// 更新表默认值
					if (field.isAnnotationPresent(InitValue.class)) {
						InitValue defaultValue = field.getAnnotation(InitValue.class);
						if (defaultValue.value() != null) {
							updateDefaultValue(clazz, field.getName(), defaultValue.value());
						}
					}
				}

				// 获取组合索引
				if (clazz.isAnnotationPresent(CompositeIndex.class)) {
					CompositeIndex compositeIndex = clazz.getAnnotation(CompositeIndex.class);
					checkOrCreateIndex(clazz, compositeIndex.colums(), compositeIndex.unique(), indexs);
				}
			}

		}
	}

	private void logQuery(String sql) {
		logQuery(sql, null);
	}

	private void logQuery(String sql, Object[] params) {
		if (print) {
			try {
				if (params != null) {
					for (Object object : params) {

						if (object instanceof String) {
							object = object.toString().replace("$", "RDS_CHAR_DOLLAR");
							sql = sql.replaceFirst("\\?", "'" + object + "'").replace("RDS_CHAR_DOLLAR", "$");
						} else {
							sql = sql.replaceFirst("\\?", String.valueOf(object));
						}

					}
				}
				logger.info(separator + sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void checkOrCreateIndex(Class<?> clazz, String name, boolean unique, List<Map<String, Object>> indexs) {
		checkOrCreateIndex(clazz, new String[] { name }, unique, indexs);
	}

	private void checkOrCreateIndex(Class<?> clazz, String[] colums, boolean unique, List<Map<String, Object>> indexs) {
		List<String> columList = new ArrayList<String>();
		for (String colum : colums) {
			columList.add(StrUtil.toUnderlineCase(colum));
		}
		String name = StrUtil.join("&", columList) + "@" + StrUtil.toUnderlineCase(clazz.getSimpleName());

		Boolean hasIndex = false;
		for (Map<String, Object> map : indexs) {
			if (StrUtil.toUnderlineCase(name).equalsIgnoreCase((String) map.get("name")) || StrUtil.toUnderlineCase(name).equalsIgnoreCase((String) map.get("Key_name"))) {
				hasIndex = true;
			}
		}

		if (!hasIndex) {
			String type = unique ? "UNIQUE INDEX" : "INDEX";
			String length = "";
			if (database.equals("mysql")) {
				length = "(100)";
			}

			columList = new ArrayList<String>();
			for (String colum : colums) {
				columList.add(StrUtil.toUnderlineCase("`" + colum + "`" + length));
			}

			String sql = "CREATE " + type + "  `" + StrUtil.toUnderlineCase(name) + "` ON `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`(" + StrUtil.join(",", columList) + ")";
			logQuery(formatSql(sql));
			jdbcTemplate.execute(formatSql(sql));
		}

	}

	private void checkOrCreateColumn(Class<?> clazz, String name, List<Map<String, Object>> columns) {
		Boolean hasColumn = false;
		for (Map<String, Object> map : columns) {
			if (StrUtil.toUnderlineCase(name).equalsIgnoreCase((String) map.get("name")) || StrUtil.toUnderlineCase(name).equalsIgnoreCase((String) map.get("Field"))) {
				hasColumn = true;
			}
		}

		if (!hasColumn) {
			String sql = "ALTER TABLE `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "` ADD COLUMN `" + StrUtil.toUnderlineCase(name) + "` TEXT";
			logQuery(formatSql(sql));
			jdbcTemplate.execute(formatSql(sql));
		}

	}

	private void checkOrCreateTable(Class<?> clazz) {
		String sql = "CREATE TABLE IF NOT EXISTS `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "` (id BIGINT NOT NULL PRIMARY KEY)";
		logQuery(formatSql(sql));
		jdbcTemplate.execute(formatSql(sql));

	}

	private void updateDefaultValue(Class<?> clazz, String column, String value) {
		String sql = "SELECT COUNT(*) FROM " + StrUtil.toUnderlineCase(clazz.getSimpleName()) + " WHERE `" + StrUtil.toUnderlineCase(column) + "` IS NULL";
		logQuery(formatSql(sql));
		Long count = jdbcTemplate.queryForObject(formatSql(sql), Long.class);
		if (count > 0) {
			sql = "UPDATE " + StrUtil.toUnderlineCase(clazz.getSimpleName()) + " SET `" + StrUtil.toUnderlineCase(column) + "` = ? WHERE `" + StrUtil.toUnderlineCase(column) + "` IS NULL";
			logQuery(formatSql(sql));
			jdbcTemplate.update(formatSql(sql), value);
		}

	}

	/**
	 * 插入或更新
	 * 
	 * @param object 对象
	 */
	public void insertOrUpdate(Object object) {

		Long time = System.currentTimeMillis();
		String id = (String) ReflectUtil.getFieldValue(object, "id");
		Object objectOrg = StrUtil.isNotEmpty(id) ? findById(id, object.getClass()) : null;
		try {
			if (objectOrg == null) {
				// 插入
				// 设置插入时间
				if (ReflectUtil.getField(object.getClass(), "createTime") != null) {
					ReflectUtil.setFieldValue(object, "createTime", time);
				}
				if (ReflectUtil.getField(object.getClass(), "updateTime") != null) {
					ReflectUtil.setFieldValue(object, "updateTime", time);
				}
				// 设置默认值
				setDefaultVaule(object);

				ReflectUtil.setFieldValue(object, "id", snowFlake.nextId());

				String sql = "";
				List<String> fieldsPart = new ArrayList<String>();
				List<String> placeHolder = new ArrayList<String>();
				List<Object> paramValues = new ArrayList<Object>();

				Field[] fields = ReflectUtil.getFields(object.getClass());
				for (Field field : fields) {
					fieldsPart.add("`" + StrUtil.toUnderlineCase(field.getName()) + "`");
					placeHolder.add("?");
					paramValues.add(ReflectUtil.getFieldValue(object, field));
				}

				sql = "INSERT INTO `" + StrUtil.toUnderlineCase(object.getClass().getSimpleName()) + "` (" + StrUtil.join(",", fieldsPart) + ") VALUES (" + StrUtil.join(",", placeHolder) + ")";

				logQuery(formatSql(sql), paramValues.toArray());
				jdbcTemplate.update(formatSql(sql), paramValues.toArray());

			} else {
				// 更新
				Field[] fields = ReflectUtil.getFields(object.getClass());

				// 设置更新时间
				if (ReflectUtil.getField(object.getClass(), "updateTime") != null) {
					ReflectUtil.setFieldValue(object, "updateTime", time);
				}

				List<String> fieldsPart = new ArrayList<String>();
				List<Object> paramValues = new ArrayList<Object>();

				for (Field field : fields) {
					if (!field.getName().equals("id") && ReflectUtil.getFieldValue(object, field) != null) {
						fieldsPart.add("`" + StrUtil.toUnderlineCase(field.getName()) + "`=?");
						paramValues.add(ReflectUtil.getFieldValue(object, field));
					}
				}
				paramValues.add(id);

				String sql = "UPDATE `" + StrUtil.toUnderlineCase(object.getClass().getSimpleName()) + "` SET " + StrUtil.join(",", fieldsPart) + " WHERE id = ?";

				logQuery(formatSql(sql), paramValues.toArray());
				jdbcTemplate.update(formatSql(sql), paramValues.toArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入
	 * 
	 * @param object 对象
	 */
	public void insert(Object object) {
		String id = (String) ReflectUtil.getFieldValue(object, "id");
		Object objectOrg = StrUtil.isNotEmpty(id) ? findById(id, object.getClass()) : null;
		if (objectOrg != null) {
			// 数据库里已有相同id, 使用新id以便插入
			ReflectUtil.setFieldValue(object, "id", snowFlake.nextId());
		}

		// 没有id生成id
		if (ReflectUtil.getFieldValue(object, "id") == null) {
			ReflectUtil.setFieldValue(object, "id", snowFlake.nextId());
		}

		insertOrUpdate(object);
	}

	/**
	 * 批量插入
	 * 
	 * @param <T>
	 * 
	 * @param object 对象
	 */
	public <T> void insertAll(List<T> list) {
		Long time = System.currentTimeMillis();

		Map<String, Object> idMap = new HashMap<String, Object>();
		for (Object object : list) {
			if (ReflectUtil.getFieldValue(object, "id") != null) {
				String id = (String) ReflectUtil.getFieldValue(object, "id");
				Object objectOrg = StrUtil.isNotEmpty(id) ? findById(id, object.getClass()) : null;
				idMap.put((String) ReflectUtil.getFieldValue(object, "id"), objectOrg);
			}
		}

		for (Object object : list) {
			if (ReflectUtil.getFieldValue(object, "id") != null && idMap.get((String) ReflectUtil.getFieldValue(object, "id")) != null) {
				// 数据库里已有相同id, 使用新id以便插入
				ReflectUtil.setFieldValue(object, "id", snowFlake.nextId());
			}

			// 没有id生成id
			if (ReflectUtil.getFieldValue(object, "id") == null) {
				ReflectUtil.setFieldValue(object, "id", snowFlake.nextId());
			}

			// 设置插入时间
			if (ReflectUtil.getField(object.getClass(), "createTime") != null) {
				ReflectUtil.setFieldValue(object, "createTime", time);
			}
			if (ReflectUtil.getField(object.getClass(), "updateTime") != null) {
				ReflectUtil.setFieldValue(object, "updateTime", time);
			}
			// 设置默认值
			setDefaultVaule(object);
		}

		List<Object[]> paramValues = new ArrayList<Object[]>();
		String sqls = null;
		for (Object object : list) {
			Field[] fields = ReflectUtil.getFields(object.getClass());

			List<String> fieldsPart = new ArrayList<String>();
			List<String> placeHolder = new ArrayList<String>();

			List<Object> params = new ArrayList<Object>();
			for (Field field : fields) {
				fieldsPart.add("`" + StrUtil.toUnderlineCase(field.getName()) + "`");
				placeHolder.add("?");
				params.add(ReflectUtil.getFieldValue(object, field));
			}

			paramValues.add(params.toArray());

			if (sqls == null) {
				sqls = "INSERT INTO `" + StrUtil.toUnderlineCase(object.getClass().getSimpleName()) + "` (" + StrUtil.join(",", fieldsPart) + ") VALUES (" + StrUtil.join(",", placeHolder) + ")";
			}
		}

		//logQueryBatch(formatSql(sqls), paramValues);
		jdbcTemplate.batchUpdate(formatSql(sqls), paramValues);
	}

	/**
	 * 根据id更新
	 * 
	 * @param object 对象
	 */
	public void updateById(Object object) {
		if (StrUtil.isEmpty((String) ReflectUtil.getFieldValue(object, "id"))) {
			return;
		}
		insertOrUpdate(object);
	}

	/**
	 * 累加某一个字段的数量,原子操作
	 * 
	 * @param object
	 */
	public void addCountById(String id, String property, Long count, Class<?> clazz) {
		String sql = "UPDATE `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "` SET `" + property + "` = CAST(`" + property + "` AS DECIMAL(30,10)) + ? WHERE `id` =  ?";
		Object[] params = new Object[] { count, id };
		logQuery(formatSql(sql), params);
		jdbcTemplate.update(formatSql(sql), params);
	}

	/**
	 * 根据id更新
	 * 
	 * @param object 对象
	 */
	public void updateAllColumnById(Object object) {
		if (StrUtil.isEmpty((String) ReflectUtil.getFieldValue(object, "id"))) {
			return;
		}

		Field[] fields = ReflectUtil.getFields(object.getClass());

		List<String> fieldsPart = new ArrayList<String>();
		List<Object> paramValues = new ArrayList<Object>();

		for (Field field : fields) {
			if (!field.getName().equals("id")) {
				fieldsPart.add("`" + StrUtil.toUnderlineCase(field.getName()) + "`=?");
				paramValues.add(ReflectUtil.getFieldValue(object, field));
			}
		}
		paramValues.add((String) ReflectUtil.getFieldValue(object, "id"));

		String sql = "UPDATE `" + StrUtil.toUnderlineCase(object.getClass().getSimpleName()) + "` SET " + StrUtil.join(",", fieldsPart) + " WHERE id = ?";

		logQuery(formatSql(sql), paramValues.toArray());
		jdbcTemplate.update(formatSql(sql), paramValues.toArray());

	}

	/**
	 * 根据id删除
	 * 
	 * @param id    对象
	 * @param clazz 类
	 */
	public void deleteById(String id, Class<?> clazz) {

		if (StrUtil.isEmpty(id)) {
			return;
		}
		deleteByQuery(new ConditionAndWrapper().eq("id", id), clazz);
	}

	/**
	 * 根据id删除
	 * 
	 * @param id    对象
	 * @param clazz 类
	 */
	public void deleteByIds(Collection<String> ids, Class<?> clazz) {
		if (ids == null || ids.size() == 0) {
			return;
		}

		deleteByQuery(new ConditionAndWrapper().in("id", ids), clazz);
	}
	
	/**
	 * 根据id删除
	 * 
	 * @param id    对象
	 * @param clazz 类
	 */
	public void deleteByIds(String[] ids, Class<?> clazz) {
		deleteByIds(Arrays.asList(ids), clazz);
	}

	/**
	 * 根据条件删除
	 * 
	 * @param query 查询
	 * @param clazz 类
	 */
	public void deleteByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {
		List<Object> values = new ArrayList<Object>();
		String sql = "DELETE FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}
		logQuery(formatSql(sql), values.toArray());
		jdbcTemplate.update(formatSql(sql), values.toArray());
	}

	/**
	 * 设置默认值
	 * 
	 * @param object 对象
	 */
	private void setDefaultVaule(Object object) {
		Field[] fields = ReflectUtil.getFields(object.getClass());
		for (Field field : fields) {
			// 获取注解
			if (field.isAnnotationPresent(InitValue.class)) {
				InitValue defaultValue = field.getAnnotation(InitValue.class);

				String value = defaultValue.value();

				if (ReflectUtil.getFieldValue(object, field) == null) {
					// 获取字段类型
					Class<?> type = field.getType();
					if (type.equals(String.class)) {
						ReflectUtil.setFieldValue(object, field, value);
					}
					if (type.equals(Short.class)) {
						ReflectUtil.setFieldValue(object, field, Short.parseShort(value));
					}
					if (type.equals(Integer.class)) {
						ReflectUtil.setFieldValue(object, field, Integer.parseInt(value));
					}
					if (type.equals(Long.class)) {
						ReflectUtil.setFieldValue(object, field, Long.parseLong(value));
					}
					if (type.equals(Float.class)) {
						ReflectUtil.setFieldValue(object, field, Float.parseFloat(value));
					}
					if (type.equals(Double.class)) {
						ReflectUtil.setFieldValue(object, field, Double.parseDouble(value));
					}
					if (type.equals(Boolean.class)) {
						ReflectUtil.setFieldValue(object, field, Boolean.parseBoolean(value));
					}
				}
			}
		}
	}

	/**
	 * 按查询条件获取Page
	 * 
	 * @param query 查询
	 * @param page  分页
	 * @param clazz 类
	 * @return Page 分页
	 */
	public Page findPage(ConditionWrapper conditionWrapper, Sort sort, Page page, Class<?> clazz) {
		List<Object> values = new ArrayList<Object>();
		// 查询出一共的条数
		Long count = findCountByQuery(conditionWrapper, clazz);

		String sql = "SELECT * FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}
		if (sort != null) {
			sql += " " + sort.toString();
		} else {
			sql += " ORDER BY id DESC";
		}
		if (database.equalsIgnoreCase("mysql") || database.equalsIgnoreCase("sqlite")) {
			sql += " LIMIT " + (page.getCurrent() - 1) * page.getLimit() + "," + page.getLimit();
		} else {
			sql += " LIMIT " + page.getLimit() + " OFFSET " + (page.getCurrent() - 1) * page.getLimit();
		}

		page.setCount(count);

		logQuery(formatSql(sql), values.toArray());
		page.setRecords(buildObjects(jdbcTemplate.queryForList(formatSql(sql), values.toArray()), clazz));

		return page;
	}

	/**
	 * 按查询条件获取Page
	 * 
	 * @param query 查询
	 * @param page  分页
	 * @param clazz 类
	 * @return Page 分页
	 */
	public Page findPage(Sort sort, Page page, Class<?> clazz) {
		return findPage(null, sort, page, clazz);
	}
	
	/**
	 * 按查询条件获取Page
	 * 
	 * @param query 查询
	 * @param page  分页
	 * @param clazz 类
	 * @return Page 分页
	 */
	public Page findPage(ConditionWrapper conditionWrapper, Page page, Class<?> clazz) {
		return findPage(conditionWrapper, null, page, clazz);
	}

	/**
	 * 按查询条件获取Page
	 * 
	 * @param query 查询
	 * @param page  分页
	 * @param clazz 类
	 * @return Page 分页
	 */
	public Page findPage(Page page, Class<?> clazz) {
		return findPage(null, null, page, clazz);
	}

	/**
	 * 根据id查找
	 * 
	 * @param id    id
	 * @param clazz 类
	 * @return T 对象
	 */
	public <T> T findById(String id, Class<T> clazz) {
		if (StrUtil.isEmpty(id)) {
			return null;
		}

		return findOneByQuery(new ConditionAndWrapper().eq("id", id), clazz);

	}

	/**
	 * 根据条件查找单个
	 * 
	 * @param query 查询
	 * @param clazz 类
	 * @return T 对象
	 */
	public <T> T findOneByQuery(ConditionWrapper conditionWrapper, Sort sort, Class<T> clazz) {
		List<Object> values = new ArrayList<Object>();
		List<T> list = new ArrayList<T>();
		String sql = "SELECT * FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}
		if (sort != null) {
			sql += " " + sort.toString();
		} else {
			sql += " ORDER BY id DESC";
		}
		sql += " limit 1";

		logQuery(formatSql(sql), values.toArray());
		list = buildObjects(jdbcTemplate.queryForList(formatSql(sql), values.toArray()), clazz);
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 根据条件查找单个
	 * 
	 * @param query 查询
	 * @param clazz 类
	 * @return T 对象
	 */
	public <T> T findOneByQuery(Sort sort, Class<T> clazz) {
		return findOneByQuery(null, sort, clazz);
	}

	/**
	 * 根据条件查找单个
	 * 
	 * @param <T>      类型
	 * @param condition
	 * @param clazz    类
	 * @return T 对象
	 */
	public <T> T findOneByQuery(ConditionWrapper conditionWrapper, Class<T> clazz) {
		return findOneByQuery(conditionWrapper, null, clazz);

	}

	/**
	 * 根据条件查找List
	 * 
	 * @param <T>   类型
	 * @param query 查询
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(ConditionWrapper conditionWrapper, Sort sort, Class<T> clazz) {
		List<Object> values = new ArrayList<Object>();

		String sql = "SELECT * FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}
		if (sort != null) {
			sql += " " + sort.toString();
		} else {
			sql += " ORDER BY id DESC";
		}

		logQuery(formatSql(sql), values.toArray());
		return buildObjects(jdbcTemplate.queryForList(formatSql(sql), values.toArray()), clazz);
	}

	/**
	 * 根据条件查找List
	 * 
	 * @param <T>      类型
	 * @param condition 查询
	 * @param clazz    类
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(ConditionWrapper conditionWrapper, Class<T> clazz) {
		return (List<T>) findListByQuery(conditionWrapper, null, clazz);
	}

	/**
	 * 根据条件查找List
	 * 
	 * @param <T>      类型
	 * @param condition 查询
	 * @param clazz    类
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(Sort sort, Class<T> clazz) {
		return (List<T>) findListByQuery(null, sort, clazz);
	}

	/**
	 * 根据条件查找某个属性
	 * 
	 * @param <T>           类型
	 * @param query         查询
	 * @param documentClass 类
	 * @param property      属性
	 * @param propertyClass 属性类
	 * @return List 列表
	 */
	public <T> List<T> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, String property, Class<T> propertyClass) {
		List<?> list = findListByQuery(conditionWrapper, documentClass);
		List<T> propertyList = extractProperty(list, property, propertyClass);

		return propertyList;
	}

	/**
	 * 根据条件查找某个属性
	 * 
	 * @param <T>           类型
	 * @param condition      查询
	 * @param documentClass 类
	 * @param property      属性
	 * @return List 列表
	 */
	public List<String> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, String property) {
		return findPropertiesByQuery(conditionWrapper, documentClass, property, String.class);
	}

	/**
	 * 根据id查找某个属性
	 * 
	 * @param <T>           类型
	 * @param condition      查询
	 * @param documentClass 类
	 * @param property      属性
	 * @return List 列表
	 */
	public List<String> findPropertiesByIds(Collection<String> ids, Class<?> documentClass, String property) {
		if (ids == null || ids.size() == 0) {
			return new ArrayList<String>();
		}

		ConditionAndWrapper ConditionAndWrapper = new ConditionAndWrapper();
		ConditionAndWrapper.in("id", ids);

		return findPropertiesByQuery(ConditionAndWrapper, documentClass, property, String.class);
	}
	
	/**
	 * 根据id查找某个属性
	 * 
	 * @param <T>           类型
	 * @param condition      查询
	 * @param documentClass 类
	 * @param property      属性
	 * @return List 列表
	 */
	public List<String> findPropertiesByIds(String[] ids, Class<?> documentClass, String property) {
		return findPropertiesByIds(Arrays.asList(ids), documentClass, property);
	}

	/**
	 * 根据条件查找id
	 * 
	 * @param query 查询
	 * @param clazz 类
	 * @return List 列表
	 */
	public List<String> findIdsByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {

		return findPropertiesByQuery(conditionWrapper, clazz, "id");
	}

	/**
	 * 根据id集合查找
	 * 
	 * @param List  ids id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(Collection<String> ids, Class<T> clazz) {
		return findListByIds(ids, null, clazz);
	}
	
	/**
	 * 根据id集合查找
	 * 
	 * @param List  ids id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(String[] ids, Class<T> clazz) {
		return findListByIds(Arrays.asList(ids), null, clazz);
	}

	/**
	 * 根据id集合查找
	 * 
	 * @param List  ids id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(Collection<String> ids, Sort sort, Class<T> clazz) {
		if (ids == null || ids.size() == 0) {
			return new ArrayList<T>();
		}

		ConditionAndWrapper ConditionAndWrapper = new ConditionAndWrapper();
		ConditionAndWrapper.in("id", ids);

		return findListByQuery(ConditionAndWrapper, sort, clazz);
	}

	
	/**
	 * 根据id集合查找
	 * 
	 * @param List  ids id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(String[] ids, Sort sort, Class<T> clazz) {
		return findListByIds(Arrays.asList(ids), sort, clazz);
	}
	/**
	 * 查询全部
	 * 
	 * @param <T>   类型
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findAll(Class<T> clazz) {
		return findAll(null, clazz);
	}

	/**
	 * 查询全部
	 * 
	 * @param <T>   类型
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findAll(Sort sort, Class<T> clazz) {
		return findListByQuery(null, sort, clazz);
	}

	/**
	 * 查找全部的id
	 * 
	 * @param clazz 类
	 * @return List 列表
	 */
	public List<String> findAllIds(Class<?> clazz) {
		return findIdsByQuery(null, clazz);
	}

	/**
	 * 查找数量
	 * 
	 * @param condition 查询
	 * @param clazz    类
	 * @return Long 数量
	 */
	public Long findCountByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {
		List<Object> values = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM `" + StrUtil.toUnderlineCase(clazz.getSimpleName()) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}

		logQuery(formatSql(sql), values.toArray());
		return jdbcTemplate.queryForObject(formatSql(sql), values.toArray(), Long.class);
	}

	/**
	 * 查找全部数量
	 * 
	 * @param clazz 类
	 * @return Long 数量
	 */
	public Long findAllCount(Class<?> clazz) {
		return findCountByQuery(null, clazz);
	}

	/**
	 * 获取list中对象某个属性,组成新的list
	 * 
	 * @param list     列表
	 * @param clazz    类
	 * @param property 属性
	 * @return List<T> 列表
	 */
	private <T> List<T> extractProperty(List<?> list, String property, Class<T> clazz) {
		Set<T> rs = new HashSet<T>();
		for (Object object : list) {
			Object value = ReflectUtil.getFieldValue(object, property);
			if (value.getClass().equals(clazz)) {
				rs.add((T) value);
			}
		}

		return new ArrayList<T>(rs);
	}

	/**
	 * Map转Bean
	 * 
	 * @param <T>
	 * @param queryForList
	 * @param clazz
	 * @return
	 */
	private <T> List<T> buildObjects(List<Map<String, Object>> queryForList, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		try {

			Field[] fields = ReflectUtil.getFields(clazz);

			for (Map<String, Object> map : queryForList) {
				Object obj = clazz.getDeclaredConstructor().newInstance();

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String mapKey = entry.getKey();
					Object mapValue = entry.getValue();

					for (Field field : fields) {
						if (StrUtil.toUnderlineCase(field.getName()).equals(mapKey)) {
							ReflectUtil.setFieldValue(obj, field.getName(), mapValue);
							break;
						}
					}

				}

				list.add((T) obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private String formatSql(String sql) {
		if (StrUtil.isEmpty(sql)) {
			return "";
		}

		if (!database.equalsIgnoreCase("mysql")) {
			sql = sql.replace("`", "\"");
		}

		String tab = "\t";
		sql = sql.replace("FROM", separator + "FROM")//
				.replace("WHERE", separator + "WHERE")//
				.replace("ORDER", separator + "ORDER")//
				.replace("LEFT", separator + "LEFT")//
				.replace("LIMIT", separator + "LIMIT")//
				.replace("AND", separator + tab + "AND")//
				.replace("SET", separator + "SET")//
				.replace("VALUES", separator + "VALUES");//
		return sql;
	}
}
