# sqlHelper

#### 介绍
本项目是基于spring-data-jdbc的orm,支持 sqlite,mysql,postgresql三种数据库,主要特点是像mongodb一样使用sql数据库.

在敏捷开发中,最难以管理的业务关系是数据库的表结构,因为数据库表结构不可写成代码,无法使用版本管理工具进行迭代管理,每次新的需求来了以后,要使用各种手段修改各处的数据库表结构,开发数据库,测试数据库,正式数据库等等,而且要保证他们一致,否则上一个版本的代码运行在下一个版本的数据库上是会出现错误的.

传统关系型数据库,要修改表结构必须使用alter,create等语句。为了保证项目中测试数据库与正式数据库或其他数据库结构一致,有了flyway这种东西,但实际使用中依然不便。首先flyway以sql文件名版本号的形式来维护数据库版本,项目时间一长,flyway文件夹的sql文件数量会变得非常庞大,另外一点,两个开发者同时想要修改表结构时,极易产生版本冲突,两人可能在同一时间都提交了同一个版本号的sql文件,导致flyway执行出错,这种问题处理起来及其麻烦。另外如果一个开发人员本地代码的pojo类与数据库表字段对不上（已经被另外一个开发人员的flyway更新）,执行指定字段的select或insert语句是会报错的,此时他只能等待另外一名开放人员将新版pojo类提交。

理想情况下,需求快速变化的敏捷开发应该使用mongodb这种文档性数据库,每个表(集合)的表结构都是动态的,可以插入任意结构的数据,本人另外一个项目mongoHelper就是为此而生的orm,如果能接受直接使用mongodb,可使用该项目: https://gitee.com/cym1102/mongoHelper

sqlHelper为mongoHelper的兄弟项目,旨在为关系型数据库提供近似mongodb的使用体验.即开发过程中完全不用关心数据库结构,在任意一个空白或是有结构的数据库中,在项目启动的瞬间都可以立刻构建出与pojo类对应的数据库结构,可以立即开始进行业务开发.除了查询sql语句的执行效果,已经完全不必打开数据库客户端对数据库结构进行管理了.

#### 软件架构
本项目只适用于springBoot项目,项目也依赖springBoot相关库,springMVC项目无法使用,另外项目依赖了hutool提供的诸多Util工具,让代码更简洁。

演示应用项目：https://gitee.com/cym1102/nginxWebUI

这个项目也是实际在使用的项目,是nginx的网页配置工具,如有需要可以看看

#### 安装教程
1.  引入maven库

```
    <dependency>
        <groupId>cn.craccd</groupId>
        <artifactId>sqlHelper</artifactId>
        <version>0.2.3</version>
    </dependency>
```

2.  配置springBoot配置文件application.yml

```
spring:
  database: 
    type: sqlite              #支持数据库 sqlite,mysql,postgresql
    package: com.cym.model    #pojo类所在的包路径
    print: true               #是否打印sql
    sqlite-path: /home/nginxWebUI/sqlite.db       #释放sqlite文件路径
 datasource:                 #数据库访问配置,使用mysql和postgresql时才需要配置
    url: jdbc:mysql://10.10.10.10:3306/test
    username: root
    password: root
```

如果使用sqlite,本项目在启动时会释放一个sqlite.db文件(可配置文件名)到用户文件夹下,在完全没有数据库的服务器环境下,也可以直接使用sqlite数据库.

如果使用mysql或postgresql还需要配置datasorce,包括数据库url和用户名密码

3.  在合适的地方添加SqlConfig.java配置类

```
@Configuration
@ComponentScan(basePackages = { "cn.craccd" })
public class SqlConfig {
	
}
```

4.  在springBoot主运行类中加入@EnableTransactionManagement注解, 支持事务

#### 使用说明

###### 1.  基本操作
本orm会在容器中注入一个对象SqlHelper,这个对象拥有诸多单表查询功能,如下
- 按id删除：deleteById(String, Class<?>)
- 按条件删除：deleteByQuery(ConditionAndWrapper, Class<?>)
- 查询所有：findAll(Class<T>)
- 查询数量：findCount(Class<?>)
- 根据id查询：findById(String, Class<T>)
- 根据条件查询：findListByQuery(ConditionAndWrapper, Class<?>)
- 根据条件查询并分页：findPage(ConditionAndWrapper, Page, Class<?>)
- 插入：insert(Object)
- 插入或更新：insertOrUpdate(Object)
- 根据id更新：updateById(Object)
- 根据id更新全部字段：updateAllColumnById(Object)
- 累加某一个字段的数量, 原子操作：addCountById(String id, String property, Long count, Class<?> clazz)

这个SqlHelper能够完成所有查询任务,插入和更新操作能够自动判断pojo的类型操作对应表,查询操作根据传入的Class进行对应表操作,本orm所有数据库操作都基于SqlHelper的功能,不用像mybatis一样,每个表都要建立一套Mapper,xml,Service,model,大大减少数据层的代码量。可以将SqlHelper直接注入到controller层,简单的操作直接调用SqlHelper进行操作,不需要调用service层。

而复杂的操作及事务处理需要service层,将SqlHelper注入service,并使用service层的@Transactional注解就能使用springBoot管理的事务功能。


###### 2.  复杂查询功能
本orm的查询功能都在SqlHelper的findByQuery,findPage方法中.使用ConditionAndWrapper和ConditionOrWrapper对象作为sql的拼接对象

```
// 根据输入条件进行查询
public List<User> search(String word, Integer type) {
	ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper();

	if (StrUtil.isNotEmpty(word)) {
		conditionAndWrapper.and(new ConditionOrWrapper().like("name", word).like("phone", word));
	}
	if (type != null) {
		conditionAndWrapper.eq("type", type);
	}
		
	List<User> userList = sqlHelper.findListByQuery(conditionAndWrapper, User.class);

	return userList ;
}
```
以上代码组装了类似于select * from user where (name like '%xxx%' or phone like '%xxx%') and type = xxx的查询语句。

本项目不支持使用left join rigth join等连接查询,关系型数据库的连表查询能解决很多问题,但在大公司中已不再推荐使用,因为很难做数据库优化,数据量庞大时查询时间很慢而且很难进行优化。需要连表查询时,先查出对方id集,再使用in进行包含查询,可以很方便的走索引,而且分库的时候很容易修改。这样使用的话,实际是将关系型数据库用成了近似文档型数据库,表之间不再产生关联。

基于以上理念,本orm还提供了一些小功能用于完善这种多次连接查询,在sqlHelper中有以下方法
 - 只查出表的id作为List返回：findIdsByQuery(ConditionAndWrapper conditionAndWrapper, Class<?> clazz)
 - 只查出表的某个字段作为List返回：findPropertiesByQuery(ConditionAndWrapper conditionAndWrapper,  Class<?> documentClass, String property, Class<T> propertyClass)

用法示例：

```
// 查出订单下的所有商品（OrderProduct.class为订单商品对照表）
public List<Product> getProductList(String orderId) {
	List<String> productIds = sqlHelper.findPropertiesByQuery(new ConditionAndWrapper().eq("orderId", orderId), OrderProduct.class,  "productId", String.class);
	return sqlHelper.findListByQuery(new ConditionAndWrapper().in("id", productIds), Product.class);
}


// 根据产品名查出所有订单
public Page search(Page page, String keywords) {
	ConditionOrWrapper conditionOrWrapper = new ConditionOrWrapper();
		
	if (StrUtil.isNotEmpty(keywords)) {
			
	    List<String> productIds = sqlHelper.findIdsByQuery(new ConditionAndWrapper().like("name", keywords), Product.class);
	    List<String> orderIds = sqlHelper.findPropertiesByQuery(new ConditionAndWrapper().in("productId", productIds), OrderProduct.class,  "orderId", String.class);
	
	    conditionOrWrapper.in("id", orderIds);
	}

	page = sqlHelper.findPage(conditionOrWrapper, page, Order.class);
	return page;
}
```


###### 3.  分页查询,
本orm提供一个Page类,包含count总记录数,limit每页记录数,curr起始页（从1开始）,records结果列表四个属性,只要将包含curr和limit数据的Page对象传入findPage,即可查询出records,count的数据并自动返回到Page对象中。这里三个属性参考了layui的分页参数,可直接无缝对接layui的分页控件。

```
public Page search(Page page, String word, Integer type) {
    ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper();

	if (StrUtil.isNotEmpty(word)) {
		conditionAndWrapper.and(new ConditionOrWrapper().like("name", word).like("phone", word));
	}
	if (type != null) {
		conditionAndWrapper.eq("type", type);
	}
	Sort sort = Sort.by(Direction.DESC, "creatTime");	
	page = sqlHelper.findPage(conditionAndWrapper, sort, page, User.class);

	return page;
}
```

###### 4.  表映射对象
spring.database.package所指向的包下,存放数据库表所对应的pojo类,项目启动时本orm会扫描该包下所有@Table注解的类并建立相应表、索引、字段默认值,所有字段都会自动在数据库中建立,删除的字段则不管,依然留在数据库中,保证数据库向前兼容性.另外本项目新增了一个属性注解@InitValue,用于提供字段初始值,不使用mysql提供的默认值功能主要时是为了尽量将数据库表结构的所有内容都放到java代码中,方便进行版本管理.

项目还提供了@SingleIndex @CompositeIndex标记属性建立相应单列索引和复合索引。

```
@Table
@CompositeIndex(colums = { "name", "phone" }, unique = true)
public class User extends BaseModel {
	Integer type; // 类型 0客户 1经销商
	String name;
	@SingleIndex(unique = true)
	String phone;

        public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
        public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
```
另外本orm提供了一个基础model,包含id（本项目固定id属性为主键）,createTime,updateTime三个必备属性,createTime和updateTime在插入和更新时会自动填充时间戳,其他pojo类可继承此BaseModel,简化代码编写。

```
public class BaseModel implements Serializable{
	String id;
	Long createTime;
	Long updateTime;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
}

```

###### 5.数据库导入导出工具
本orm提供一个数据库导入导出工具ImportExportUtil,注入到容器中,拥有exportDb和importDb两个方法,可按照pojo类导入导出相应表结构和数据,导出的数据格式为json,不同于一般备份工具导出格式为sql,json格式能保证导出的数据灵活性,可使用定时任务定期备份数据库。

###### 6.打印查询语句
spring-data-jdbc默认的打印语句方式为修改配置文件logging.level.root: debug。但这里打印出来的语句不是很好阅读,没有很好的格式化,参数都用?来代替,要直接复制到数据库客户端执行很困难,本orm重写了sql打印,只需要配置spring.database.print=true即可,能够打印出如下的sql语句,参数都是实际填充到语句中的,但实际执行的时候确实是使用了参数查询的方式,绝不是把参数拼接到sql语句中,不会出现sql注入问题.

```
SELECT * 
FROM "server" 
WHERE  ("server_name" LIKE '%123%' OR "proxy_pass" LIKE '%123%')  
	AND CAST("type" AS DECIMAL) = 0  
ORDER BY id DESC 
LIMIT 10 OFFSET 0
```
