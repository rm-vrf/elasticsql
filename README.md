# ElasticSQL

-- Database on top of Elasticsearch

IT建设搞了不少年了，积累了大量的数据。要分析这些数据，一般的关系型数据库处理起来很困难。

Elasticsearch是个开源分布式搜索引擎，它的特点有：分布式，零配置，自动发现，索引自动分片，索引副本机制，RESTful风格接口，多数据源，自动搜索负载等。由于他的分布特性，很适合用来做大量数据的保存和搜索，可以用Elasticsearch处理数十亿的数据。更具体的好处就不在这里多说了，看Elasticsearch官网介绍吧：http://www.elasticsearch.org，或者：http://www.elasticsearch.cn。

但是Elasticsearch使用和维护与传统的数据库有较大的差异，其RESTful形式的接口提供了很好的编程体验，对于日常数据维护比较难用。Elasticsearch的查询接口使用的是比较独特的DSL（领域专用语言），与关系型数据库的SQL差异很大。

ElasticSQL是建立在Elasticsearch之上的一层SQL代理，有了他，就能够使用MySQL兼容协议访问Elasticsearch里的数据，使用SQL语句创建索引和数据，并且对数据进行删改和查询。

现在可以采用任意MySQL的连接管理工具对数据进行维护，比如使用mysql工具连接到ElasticSQL：

<pre>
localhost:~ root$ mysql -hlocalhost -uusername -ppassword -A
</pre>

然后可以创建一个数据库，并且为这个数据库设置分片数和副本数：

<pre>
mysql> create database testdb shards=5, replicas=2;
</pre>

ElasticSQL会在后台Elasticsearch中创建testdb索引。然后再创建一个数据表：

<pre>
mysql> create table person {
    ->   name string, 
    ->   title string not_analyzed,
    ->   address string,
    ->   birthday datetime,
    ->   grade int
    -> };
</pre>

现在创建了一张表，并且设置了字段类型和索引模式。现在可以使用SQL向数据表中输入数据：

<pre>
mysql> insert into person 
    ->   (name, title, address, birthday, grade) 
    ->   values 
    ->   ('张三', 'Mr', '合肥市潜山路888号', '2005-10-15', 2);

mysql> insert into person 
    ->   (name, title, address, birthday, grade) 
    ->   values 
    ->   ('李四', 'Mr', '潜山路887号', '2002-02-10', 3);
</pre>

查询数据：

<pre>
mysql> select * from person where title='Mr' order by grade; 
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
| _id                    | _index | name | title | address        | birthday   | grade | _version     | _score   |
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
| dSYjD7CUQICxrkmACIXvlw | 1/2    | 张三  | Mr    | 合肥市潜山路888号 | 2005-10-15 | 2     | 1            | 1        |
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
| GaCodbWHSqiWKf1RfjWyDQ | 2/2    | 李四  | Mr    | 潜山路887号      | 2002-02-10 | 3     | 1            | 1        |
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
</pre>

<pre>
mysql> select * from person where fulltext(address, '潜山路'); 
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
| _id                    | _index | name | title | address        | birthday   | grade | _version     | _score   |
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
| GaCodbWHSqiWKf1RfjWyDQ | 1/2    | 李四  | Mr    | 潜山路887号      | 2002-02-10 | 3     | 1            | 0.85     |
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
| dSYjD7CUQICxrkmACIXvlw | 2/2    | 张三  | Mr    | 合肥市潜山路888号 | 2005-10-15 | 2     | 1            | 0.82     |
+------------------------+--------+------+-------+----------------+------------+-------+--------------+----------+
</pre>

可以采用任一MySQL管理工具对数据进行管理，也可以使用MySQL JDBC编程接口对数据进行访问。

# ElasticSQL支持的MySQL协议

* Initial Handshake
* Authentication
* COM_QUIT
* COM_INIT_DB
* COM_QUERY
* COM_FIELD_LIST
* COM_CREATE_DB
* COM_STMT_EXECUTE
* Replication Protocol 

以上工作计划中，今年应该能完成。
