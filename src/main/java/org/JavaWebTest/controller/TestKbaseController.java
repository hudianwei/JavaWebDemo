package org.JavaWebTest.controller;

import com.kbase.jdbc.Connection;
import com.kbase.jdbc.ConnectionImpl;
import com.kbase.jdbc.ResultSetImpl;
import com.kbase.jdbc.StatementImpl;
import kbase.struct.KSTM_ABSTRACT_EXTRACTOR;
import kbase.struct.KSTM_TEXTWEIGHT;
import kbase.struct.KSTM_VSM_GENERATOR;
import kbase.struct.TPI_RETURN_RESULT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Description:
 * @author: HU
 * @date: 2019/1/3 9:54
 */

@Controller
@RequestMapping("/testkbase")
public class TestKbaseController {
    public static final String url = "jdbc:kbase://192.168.105.111";// kbase服务器地址
    public static final String driver = "com.kbase.jdbc.Driver";
    public static final String username = "DBOWN";
    public static final String password = "";

    @RequestMapping("/AbstractTest.do")
    public void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
        String text = "对水文的作用 气候变化对水文的作用, 在特定下垫面, 主要通过降水、气温、 蒸发等气象因素起作用。表现在对径流的作用, 包括年、月, 洪峰流量在时间与空间的";

        KSTM_ABSTRACT_EXTRACTOR kae = new KSTM_ABSTRACT_EXTRACTOR();
        kae.nSentenceCount = 5;
        kae.bUsePercentage = false;
        TPI_RETURN_RESULT result = conn.KBase_STM_AbstractExtractor_Do(0, text,
                kae, null, 0);
        OutputStream ps = response.getOutputStream();
        //这句话的意思，使得放入流的数据是utf8格式
        ps.write(result.rtnBuf.getBytes("UTF-8"));
        ps.close();
    }

    @RequestMapping("/SmartReminder.do")
    public void SmartReminder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
        TPI_RETURN_RESULT result = conn.KBase_TOP_GetItem("test_dic", "农民");
        OutputStream ps = response.getOutputStream();
        //这句话的意思，使得放入流的数据是utf8格式
        ps.write(result.rtnBuf.getBytes("UTF-8"));
        ps.close();
    }

    @RequestMapping("/VsmTest.do")
    public void VsmTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        try {
            ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
            KSTM_VSM_GENERATOR param = new KSTM_VSM_GENERATOR();
            param.bOutputWord = true;
            param.nMaxFeatureNum = 10;
            param.nSortItem = 1;

            String domainName = "CJFD";
            // conn.KBase_STM_ShutDown();关闭知识域
            // int hDomain = conn.KBase_STM_OpenDomain(domainName);启动知识域
            // int vsmInit = conn.KBase_STM_VSMGenerator_Init(hDomain);初始化知识域，一般没有
            KSTM_TEXTWEIGHT[] weight = GetKSTM_TEXTWEIGHT();
            TPI_RETURN_RESULT result = conn
                    .KBase_STM_VSMGenerator_Do(0, weight, weight.length, param);

            OutputStream ps = response.getOutputStream();
            //这句话的意思，使得放入流的数据是utf8格式
            ps.write(result.rtnBuf.getBytes("UTF-8"));
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private KSTM_TEXTWEIGHT[] GetKSTM_TEXTWEIGHT() {
        // TODO Auto-generated method stub
        KSTM_TEXTWEIGHT[] weightList = new KSTM_TEXTWEIGHT[1];

        KSTM_TEXTWEIGHT weight = new KSTM_TEXTWEIGHT();
        weight.pText = "知识是人类进步的阶梯";
        weight.nWeight = 5;
        weightList[0] = weight;
        return weightList;
    }

    @RequestMapping("/FieldTest.do")
    public void FieldTest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        try {
            Class.forName("com.kbase.jdbc.Driver"); // classLoader,加载对应驱动
            ConnectionImpl conn = (ConnectionImpl) DriverManager.getConnection("jdbc:kbase://192.168.105.71", "DBOWN",
                    "");

            // ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
            StatementImpl stmt = (StatementImpl) conn.createStatement();
            /**
             * 1、获取所有字段信息
             */
            String sql = "SELECT  * FROM CCND0005 where  文件名='ABRB20050101T001'";
            // 是否是utf-8的查询
            ResultSetImpl rs = (ResultSetImpl) stmt.executeQuery(sql, false);
            // 从ResultSetImpl结果集中映射到ResultSetMetaData
            com.kbase.jdbc.ResultSetMetaData metaData = (com.kbase.jdbc.ResultSetMetaData) rs
                    .getMetaData();
            // STEP 5: Extract data from result set
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("---------------------------");
            stringBuilder.append("\r\n");
            // 获得所有列的数目及实际列数
            int columnCount = metaData.getColumnCount();
            stringBuilder.append("字段数：" + columnCount);
            stringBuilder.append("\r\n");
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                // 获得指定列的列名
                String columnName = metaData.getColumnName(i);
                // 获得指定列的数据类型名
                String columnTypeName = metaData.getColumnTypeName(i);
                stringBuilder.append("列" + i + "字段名称:" + columnName + ",数据类型:"
                        + columnTypeName + "字段长度：" + metaData.getColumnLen(i));
                stringBuilder.append("\r\n");
            }
            stringBuilder.append("---------------------------");
            stringBuilder.append("\r\n");
            /**
             * 2、获取部分字段信息字段信息
             */
            String sql2 = "SELECT count(*) as temp,题名 as test FROM CCNDtotal,cmfdtoal where 文件名='ABRB20050101T001' ";
            ResultSetImpl rs2 = (ResultSetImpl) stmt.executeQuery(sql2, false);
            /**
             * KBaseGetRecordSetFieldName 得到记录集对应的全部字段名称
             *
             * @param hSet
             *            记录集句柄
             * @param nFlag
             *            标识，0-不包括别名；1-包括别名，以":"分隔，即“字段名:别名,...”
             *
             * @return TPI_RETURN_RESULT rtnBuf属性保存记录集对应的全部字段名
             **/
            int fieldCount = rs2.KBaseGetFieldCount();
            stringBuilder.append("字段数：" + fieldCount);
            stringBuilder.append("\r\n");
            TPI_RETURN_RESULT fieldResult = rs2.KBaseGetRecordSetFieldName(0);
            String recordSetField = fieldResult.rtnBuf;
            // 获取列名的集合[test, 光盘号, 报纸中文名, 邮发号, 报纸拼音名, 年, 日期, 版号, 期号, 文章位置, 栏目, 文件名, 分类号, 引题, 正标题, 副标题, 作者, 全文, 更新日期, 旧机标关键词,...]
            String[] selectColumns = recordSetField.substring(0,
                    recordSetField.length() - 1).split(",");
            stringBuilder.append("字段名："
                    + recordSetField.substring(0, recordSetField.length() - 1));
            stringBuilder.append("\r\n");
            while (rs2.next()) {
                for (int i = 0; i < fieldCount; i++) {
                    stringBuilder.append("[" + i + "]列名：[" + selectColumns[i]
                            + "] --值数据：" + rs2.getString(i));
                }
                stringBuilder.append("\r\n");
            }

            stringBuilder.append("---------------------------");

            OutputStream ps = response.getOutputStream();
            //这句话的意思，使得放入流的数据是utf8格式
            ps.write(stringBuilder.toString().getBytes("UTF-8"));
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username,
                    password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
