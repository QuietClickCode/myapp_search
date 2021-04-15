package com.zjj.aisearch.utils;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类:DBToVO
 * 功能:根据数据库表生成VO--Value Object
 *
 * @author sam
 * @e-mail:sanlai_lee@lisanlai.cn
 */

/**
 * 表生成实体工具类
 */
public class DBToJavaVO {
    private static final String TARGET_DIR = "D:\\myapp\\myapp_search\\src\\main\\java\\com\\zjj\\aisearch\\model\\";                            //类文件存放的路径
    private static final String DIVER_NAME = "com.alibaba.druid.pool.DruidDataSource";
    private static final String URL = "jdbc:mysql://localhost:3306/aisearch?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useAffectedRows=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DATABASE_NAME = "aisearch";                                //数据库名称
    private static final String AUTHOR = "zjj";                                        //作者
    private static final String E_MAIL = "228602890@qq.com";                    //作者邮箱
    private static final String PACKAGE_NAME = "com.zjj.aisearch.model";                        //包名
    private Connection conn;
    private Statement stmt;
    private String sql;
    private ResultSet rs;
    private String[] fields;    //属性
    private String[] dataTypes;    //数据类型
    private String[] comments;    //属性的注释

    /**
     * 方法:根据数据库表生成VO--Value Object
     */
    private void tableToVo() {

        String tableName;
        String className;
        try {
            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader bf = new BufferedReader(reader);
            System.out.println("输入要转换成VO的表名称:");
            tableName = bf.readLine().trim();
            //System.out.println("输入生成的类名,不输入的时候默认跟表名相同:");
            //className = bf.readLine().trim();
            Class.forName(DIVER_NAME);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);    //创建可滚动的,只读的结果集
            sql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT" +
                    " FROM INFORMATION_SCHEMA.COLUMNS" +
                    " WHERE table_name = '" + tableName + "'" +        //表名
                    " AND table_schema = '" + DATABASE_NAME + "'";                //数据库名
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.last();                                        //把指针指向结果集的最后
            int fieldNum = rs.getRow();                        //取得最后一条结果的行号,作为类的属性个数
            int n = fieldNum;
            if (n > 0) {                                    //判断数据表中是否存在字段
                fields = new String[n];
                dataTypes = new String[n];
                comments = new String[n];
                fields[--n] = rs.getString(1);
                dataTypes[n] = rs.getString(2);
                comments[n] = rs.getString(3);
                while (rs.previous()) {
                    fields[--n] = rs.getString(1);            //取得结果集的第一列数据,对应的列名:Field
                    dataTypes[n] = rs.getString(2);
                    comments[n] = rs.getString(3);
                }
                //打印相关信息
                System.out.println("你要转换的表是:" + tableName);
                System.out.println("该表中共有" + fieldNum + "个字段,信息如下:");
                for (int i = 0, j = fields.length; i < j; i++) {
                    System.out.println("----------------------------------------");
                    String field = fields[i];
                    System.out.println("字段名称:" + field);
                    //把字段名称格式化成java命名规则形式
                    field = formatField(field);
                    fields[i] = field;                        //把格式化后的字段放入属性数组中
                    System.out.println("数据类型:" + dataTypes[i]);
                    //把数据库字段类型转换成java数据类型
                    String dataType = dataTypes[i].toLowerCase();
                    dataType = formatDataType(dataType);
                    dataTypes[i] = dataType;

                    System.out.println("字段注释:" + comments[i]);
                    if ("".equals(comments[i]) || comments[i] == null) {
                        comments[i] = fields[i];
                    }
                    System.out.println("----------------------------------------");
                }
                //格式化类名称
                className = "";
                String s = foramtClassName(className, tableName);
                //生成类文件,写入到磁盘中
                writeObjectToFile(s);
            } else {
                System.out.println("该表不存在或者表中没有字段");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("未找到数据库驱动");
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    if (!stmt.isClosed()) {
                        stmt.close();
                        stmt = null;
                        System.gc();
                    }
                }
                if (conn != null) {
                    if (!conn.isClosed()) {
                        conn.close();
                        conn = null;
                        System.gc();
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 把数据库字段格式成java变量名
     *
     * @param field
     * @return
     */
    private String formatField(String field) {
        String[] strs = field.split("_");
        field = "";
        for (int m = 0, length = strs.length; m < length; m++) {
            if (m > 0) {
                String tempStr = strs[m].toLowerCase();
                tempStr = tempStr.substring(0, 1).toUpperCase() + tempStr.substring(1, tempStr.length());
                field += tempStr;
            } else {
                field += strs[m].toLowerCase();
            }
        }
        return field;
    }

    private String formatDataType(String dataType) {
        if (dataType.contains("char")) {
            dataType = "String";
        } else if (dataType.contains("int")) {
            dataType = "Integer";
        } else if (dataType.contains("float")) {
            dataType = "Float";
        } else if (dataType.contains("double")) {
            dataType = "Double";
        } else if (dataType.contains("number") || dataType.contains("decimal")) {
            dataType = "BigDecimal";
        } else if (dataType.contains("date")) {
            dataType = "Date";
        } else if (dataType.contains("time")) {
            dataType = "Timestamp";
        } else if (dataType.contains("clob")) {
            dataType = "Clob";
        } else {
            dataType = "Object";
        }
        return dataType;
    }

    /**
     * 格式化类名
     *
     * @param className
     * @param tableName
     * @return
     */
    private String foramtClassName(String className, String tableName) {
        //如果类名不是自己定义的,那么根据表名格式化类名
        if ("".equals(className) || className == null) {
            className = "";
            String[] tempArr = tableName.split("_");
            for (int m = 0, length = tempArr.length; m < length; m++) {
                className += tempArr[m].substring(0, 1).toUpperCase() + tempArr[m].substring(1, tempArr[m].length()).toLowerCase();
            }
            System.out.println(className);
        } else {
            //如果类名已经输入,那么不管三七二十一就把类名的第一个字母大写
            className = className.substring(0, 1).toUpperCase() + className.substring(1, className.length());
            System.out.println(className);
        }
        return className;
    }

    /**
     * 生成类并写到文件
     *
     * @param className
     */
    @SuppressWarnings("deprecation")
    private void writeObjectToFile(String className) {
        PrintWriter writer = null;
        try {
            File dir = new File(TARGET_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            System.out.println(className);
            System.out.println(TARGET_DIR + className);
            File file = new File(TARGET_DIR + className + ".java");
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            //拼文件内容
            StringBuffer content = new StringBuffer();
            content.append("package " + PACKAGE_NAME + ";\n");
            content.append("import lombok.Data;\n");
            content.append("import lombok.ToString;\n");
            content.append("import java.util.Date;\n\n");
            content.append("/**\n");
            content.append(" *@类:" + className + "\n");
            content.append(" *@作者:" + AUTHOR + "\n");
            content.append(" *@E-mail:" + E_MAIL + "\n");
            content.append(" *@日期:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            content.append(" */\n\n");
            content.append("@Data\n");
            content.append("@ToString\n");

            content.append("public class " + className + "{\n\n");
            for (int i = 0, j = fields.length; i < j; i++) {
                content.append("	/**" + comments[i] + "*/\n");
                content.append("	private " + dataTypes[i] + " " + fields[i] + ";\n\n");
            }
//            //get set 方法
//            for(int i=0,j=fields.length;i<j;i++){
//                content.append("	/**\n");
//                content.append("	 *方法: 取得"+fields[i]+"\n");
//                content.append("	 *@return: "+dataTypes[i]+"  "+fields[i]+"\n");
//                content.append("	 */\n");
//                content.append("	public "+dataTypes[i]+" get"+fields[i].substring(0,1).toUpperCase()+fields[i].substring(1,fields[i].length())+"(){\n");
//                content.append("		return this."+fields[i]+";\n");
//                content.append("	}\n\n");
//                content.append("	/**\n");
//                content.append("	 *方法: 设置"+fields[i]+"\n");
//                content.append("	 *@param: "+dataTypes[i]+"  "+fields[i]+"\n");
//                content.append("	 */\n");
//                content.append("	public void set"+fields[i].substring(0,1).toUpperCase()+fields[i].substring(1,fields[i].length())+"("+dataTypes[i]+" "+fields[i]+"){\n");
//                content.append("		this."+fields[i]+" = "+fields[i]+";\n");
//                content.append("	}\n\n");
//            }
            content.append("}");
            writer.write(content.toString());
            writer.flush();
            System.out.println("类生成成功，存放路径：" + TARGET_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new DBToJavaVO().tableToVo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
