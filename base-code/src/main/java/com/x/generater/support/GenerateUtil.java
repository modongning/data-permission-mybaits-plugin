package com.x.generater.support;

import java.util.Arrays;
import java.util.HashSet;

public class GenerateUtil {
	private static HashSet<String> getMysqlKeyString() {
		StringBuffer keyStr = new StringBuffer();
		keyStr.append("add,all,alter,analyze,and,as,asc,asensitive,");
		keyStr.append("before,between,bigint,binary,blob,both,by,");
		keyStr.append("call,cascade,case,change,char,character,check,");
		keyStr.append("collate,column,condition,connection,constraint,");
		keyStr.append("continue,convert,create,cross,current_date,goto,grant,group,");
		keyStr.append("current_time,current_timestamp,current_user,cursor,");
		keyStr.append("database,databases,day_hour,day_microsecond,day_minute,");
		keyStr.append("day_second,dec,decimal,declare,default,delayed,delete,desc,");
		keyStr.append("describe,deterministic,distinct,distinctrow,div,double,drop,dual,");
		keyStr.append("each,else,elseif,enclosed,escaped,exists,exit,explain,");
		keyStr.append("false,fetch,float,float4,float8,for,force,foreign,from,fulltext,");
		keyStr.append("having,high_priority,hour_microsecond,hour_minute,hour_second,");
		keyStr.append("if,ignore,in,index,infile,inner,inout,insensitive,insert,");
		keyStr.append("int,int1,int2,int3,int4,int8,integer,interval,into,is,");
		keyStr.append("iterate,join,key,keys,kill,label,leading,leave,left,");
		keyStr.append("like,limit,linear,lines,load,localtime,localtimestamp,");
		keyStr.append("lock,long,longblob,longtext,loop,low_priority,match,");
		keyStr.append("mediumblob,mediumint,mediumtext,middleint,minute_microsecond,");
		keyStr.append("minute_second,mod,modifies,natural,not,no_write_to_binlog,");
		keyStr.append("null,numeric,on,optimize,option,optionally,or,order,");
		keyStr.append("out,outer,outfile,precision,primary,procedure,purge,");
		keyStr.append("raid0,range,read,reads,real,references,regexp,release,");
		keyStr.append("rename,repeat,replace,require,restrict,return,revoke,");
		keyStr.append("right,rlike,schema,schemas,second_microsecond,select,");
		keyStr.append("sensitive,separator,set,show,smallint,spatial,specific,");
		keyStr.append("sql,sqlexception,sqlstate,sqlwarning,sql_big_result,");
		keyStr.append("sql_calc_found_rows,sql_small_result,ssl,starting,");
		keyStr.append("straight_join,table,terminated,then,tinyblob,tinyint,");
		keyStr.append("tinytext,to,trailing,trigger,true,undo,union,unique,");
		keyStr.append("unlock,unsigned,update,usage,use,using,utc_date,utc_time,");
		keyStr.append("utc_timestamp,values,varbinary,varchar,varcharacter,");
		keyStr.append("varying,when,where,while,with,write,x509,xor,year_month,zerofill");
		return new HashSet<String>(Arrays.asList(keyStr.toString().split(",")));
	}

	/**
	 * Class作为变量时的句柄名称
	 */
	public static String getClassHandleName(String simpleName) {
		return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
	}
	
	/**
	 * 将ClassName转换为数据库表名
	 */
	public static String toDbName(String simpleName) {
		String handleName = getClassHandleName(simpleName);
		if (handleName == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < handleName.length(); i++) {
			char c = handleName.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		String dbName = sb.toString();
		if (getMysqlKeyString().contains(dbName.toLowerCase())) {
			dbName = "`" + dbName + "`";
		}
		return dbName;
	}
}
