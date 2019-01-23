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
    public static final String url = "jdbc:kbase://192.168.105.111";// kbase��������ַ
    public static final String driver = "com.kbase.jdbc.Driver";
    public static final String username = "DBOWN";
    public static final String password = "";

    @RequestMapping("/AbstractTest.do")
    public void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
        String text = "��ˮ�ĵ����� ����仯��ˮ�ĵ�����, ���ض��µ���, ��Ҫͨ����ˮ�����¡� �������������������á������ڶԾ���������, �����ꡢ��, ���������ʱ����ռ��";

        KSTM_ABSTRACT_EXTRACTOR kae = new KSTM_ABSTRACT_EXTRACTOR();
        kae.nSentenceCount = 5;
        kae.bUsePercentage = false;
        TPI_RETURN_RESULT result = conn.KBase_STM_AbstractExtractor_Do(0, text,
                kae, null, 0);
        OutputStream ps = response.getOutputStream();
        //��仰����˼��ʹ�÷�������������utf8��ʽ
        ps.write(result.rtnBuf.getBytes("UTF-8"));
        ps.close();
    }

    @RequestMapping("/SmartReminder.do")
    public void SmartReminder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
        TPI_RETURN_RESULT result = conn.KBase_TOP_GetItem("test_dic", "ũ��");
        OutputStream ps = response.getOutputStream();
        //��仰����˼��ʹ�÷�������������utf8��ʽ
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
            // conn.KBase_STM_ShutDown();�ر�֪ʶ��
            // int hDomain = conn.KBase_STM_OpenDomain(domainName);����֪ʶ��
            // int vsmInit = conn.KBase_STM_VSMGenerator_Init(hDomain);��ʼ��֪ʶ��һ��û��
            KSTM_TEXTWEIGHT[] weight = GetKSTM_TEXTWEIGHT();
            TPI_RETURN_RESULT result = conn
                    .KBase_STM_VSMGenerator_Do(0, weight, weight.length, param);

            OutputStream ps = response.getOutputStream();
            //��仰����˼��ʹ�÷�������������utf8��ʽ
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
        weight.pText = "֪ʶ����������Ľ���";
        weight.nWeight = 5;
        weightList[0] = weight;
        return weightList;
    }

    @RequestMapping("/FieldTest.do")
    public void FieldTest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        try {
            Class.forName("com.kbase.jdbc.Driver"); // classLoader,���ض�Ӧ����
            ConnectionImpl conn = (ConnectionImpl) DriverManager.getConnection("jdbc:kbase://192.168.105.71", "DBOWN",
                    "");

            // ConnectionImpl conn = (ConnectionImpl) TestKbaseController.getConn();
            StatementImpl stmt = (StatementImpl) conn.createStatement();
            /**
             * 1����ȡ�����ֶ���Ϣ
             */
            String sql = "SELECT  * FROM CCND0005 where  �ļ���='ABRB20050101T001'";
            // �Ƿ���utf-8�Ĳ�ѯ
            ResultSetImpl rs = (ResultSetImpl) stmt.executeQuery(sql, false);
            // ��ResultSetImpl�������ӳ�䵽ResultSetMetaData
            com.kbase.jdbc.ResultSetMetaData metaData = (com.kbase.jdbc.ResultSetMetaData) rs
                    .getMetaData();
            // STEP 5: Extract data from result set
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("---------------------------");
            stringBuilder.append("\r\n");
            // ��������е���Ŀ��ʵ������
            int columnCount = metaData.getColumnCount();
            stringBuilder.append("�ֶ�����" + columnCount);
            stringBuilder.append("\r\n");
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                // ���ָ���е�����
                String columnName = metaData.getColumnName(i);
                // ���ָ���е�����������
                String columnTypeName = metaData.getColumnTypeName(i);
                stringBuilder.append("��" + i + "�ֶ�����:" + columnName + ",��������:"
                        + columnTypeName + "�ֶγ��ȣ�" + metaData.getColumnLen(i));
                stringBuilder.append("\r\n");
            }
            stringBuilder.append("---------------------------");
            stringBuilder.append("\r\n");
            /**
             * 2����ȡ�����ֶ���Ϣ�ֶ���Ϣ
             */
            String sql2 = "SELECT count(*) as temp,���� as test FROM CCNDtotal,cmfdtoal where �ļ���='ABRB20050101T001' ";
            ResultSetImpl rs2 = (ResultSetImpl) stmt.executeQuery(sql2, false);
            /**
             * KBaseGetRecordSetFieldName �õ���¼����Ӧ��ȫ���ֶ�����
             *
             * @param hSet
             *            ��¼�����
             * @param nFlag
             *            ��ʶ��0-������������1-������������":"�ָ��������ֶ���:����,...��
             *
             * @return TPI_RETURN_RESULT rtnBuf���Ա����¼����Ӧ��ȫ���ֶ���
             **/
            int fieldCount = rs2.KBaseGetFieldCount();
            stringBuilder.append("�ֶ�����" + fieldCount);
            stringBuilder.append("\r\n");
            TPI_RETURN_RESULT fieldResult = rs2.KBaseGetRecordSetFieldName(0);
            String recordSetField = fieldResult.rtnBuf;
            // ��ȡ�����ļ���[test, ���̺�, ��ֽ������, �ʷ���, ��ֽƴ����, ��, ����, ���, �ں�, ����λ��, ��Ŀ, �ļ���, �����, ����, ������, ������, ����, ȫ��, ��������, �ɻ���ؼ���,...]
            String[] selectColumns = recordSetField.substring(0,
                    recordSetField.length() - 1).split(",");
            stringBuilder.append("�ֶ�����"
                    + recordSetField.substring(0, recordSetField.length() - 1));
            stringBuilder.append("\r\n");
            while (rs2.next()) {
                for (int i = 0; i < fieldCount; i++) {
                    stringBuilder.append("[" + i + "]������[" + selectColumns[i]
                            + "] --ֵ���ݣ�" + rs2.getString(i));
                }
                stringBuilder.append("\r\n");
            }

            stringBuilder.append("---------------------------");

            OutputStream ps = response.getOutputStream();
            //��仰����˼��ʹ�÷�������������utf8��ʽ
            ps.write(stringBuilder.toString().getBytes("UTF-8"));
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,���ض�Ӧ����
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
