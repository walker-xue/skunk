# MySQL递归查询解决方案
创建过程，动态创建SQL获取对应的下级IDS
```sql
CREATE PROCEDURE com_sub_nodes(IN table_name VARCHAR(128), IN id_name VARCHAR(10), IN p_id_name VARCHAR(10),IN node_id VARCHAR(10),OUT node_ids VARCHAR(4000))
BEGIN

	DECLARE nodes VARCHAR (4000);
	DECLARE sub_nodes VARCHAR (4000);
	SET nodes = '#';
	SET sub_nodes = node_id;
	#SET @exe_sql = concat('SELECT GROUP_CONCAT(', id, ') INTO sub_nodes FROM ', table_name, ' WHERE	FIND_IN_SET(', p_id, ', sub_nodes) > 0');
	SET @exe_sql = concat('SELECT GROUP_CONCAT(', id_name, ') INTO @sub_nodes_tem FROM ', table_name, ' WHERE FIND_IN_SET(',p_id_name,', @sub_nodes_p_tem) > 0');

	#SELECT @exe_sql;
		WHILE sub_nodes IS NOT NULL DO
			SET nodes = CONCAT(nodes, ',', sub_nodes);
			PREPARE sqltmt FROM @exe_sql;
			SET @sub_nodes_p_tem=sub_nodes;
			## 执行SQL语句
			EXECUTE sqltmt;  
			## 释放掉预处理段
			DEALLOCATE PREPARE sqltmt;
			#SELECT GROUP_CONCAT(menu_id) INTO sub_nodes FROM acl_menu WHERE	FIND_IN_SET(menu_pid, sub_nodes) > 0;
			SET sub_nodes = @sub_nodes_tem;			
		END	WHILE;
	SET node_ids = nodes;
END;

```
函数调用存储过程返回下级IDS
```sql
CREATE FUNCTION sub_node(table_name varchar(128),id_name varchar(128),pid_name varchar(128),node_value varchar(128))
 RETURNS varchar(40000)
BEGIN
	call com_sub_nodes( table_name,id_name,pid_name,node_value,@nodes);
	#SELECT @nodes;
	RETURN '';
END;
```

调用方式
```sql
select sub_node('acl_menu','menu_id','menu_pid',1);
报错信息：
[SQL]select sub_node('acl_menu','menu_id','menu_pid',1);
[Err] 1336 - Dynamic SQL is not allowed in stored function or trigger

```