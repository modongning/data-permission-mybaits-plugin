## 代码生成器

### 实体注解

    @Data
    @XGenModel(comment = "用户数据权限")
    public class UserPermission implements Serializable {
    	@XGenField(comment = "用户ID")
    	private Long userId;
    	@XGenField(comment = "公司ID")
    	private Long companyId;
    }

### 生成代码

    public class MainGenerater {
    	public static void main(String[] args) {
    
    		String driver = "com.mysql.jdbc.Driver";
    		String dbHost = "127.0.0.1";
    		String dbPort = "3306";
    		String dbName = "test";
    		String userName = "root";
    		String pwd = "123456";
    
    		GenerateConfig gconf = new GenerateConfig();
    //		gconf.genController();  //生成控制成代码
    //		gconf.genService();     //生成业务层代码
    		gconf.genMapper();      //生成mapper和xml
    		gconf.genTable();       //生成数据库表
    //		gconf.genStatic();      //生成前端页面
    
    		MysqlDBInfo db = new MysqlDBInfo();
    		db.setDriver(driver);
    		db.setDbHost(dbHost);
    		db.setDbName(dbName);
    		db.setDbPort(dbPort);
    		db.setUserName(userName);
    		db.setPassword(pwd);
    
    		String outputPath = "/user/test/generater";
    
    		CodeGenerater cg = new CodeGenerater();
    		cg.generateCode(UserPermission.class, gconf, db, path, path);
    	}
    }