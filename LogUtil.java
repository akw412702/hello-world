package com.weavernoth.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;


public class LogUtil {
	private static BaseBean bb = new BaseBean();
	private static String strIsLog = bb.getPropValue("wnutil", "islog");
	private static String modeid=bb.getPropValue("wnutil", "modeid");
	private static RecordSet Rs = new RecordSet();
	public LogUtil(){}
	public static void main(String[] args) {
		System.out.println(1111);
	}

	/**
	 * 将字符串输出到log日志文件
	 * 系统自动读取wnutil.properties文件中的设置，如果设置为输出日志：islog=Y时，才输出信息
	 * 代码调用如下
	 * LogUtil.doWriteLog("输出的日志内);
	 *
	 * @param strLogMessage 输出的日志信息
	 */
	public static void doWriteLog(String strLogMessage){
		//如果输出标志为Y，则输出日志
		if("Y".equalsIgnoreCase(strIsLog)){
			bb.writeLog(strLogMessage);
		}
	}

	/**
	 * 将字符串输出到log日志文件
	 * 系统自动读取wnutil.properties文件中的设置，如果设置为输出日志：islog=Y时，才输出信息
	 * 代码调用如下
	 * LogUtil.doWriteLog("输出的日志内信息);
	 *
	 * @param strLogMessage 输出的日志信息
	 */
	public static void doWriteLog(String className,String strLogMessage){
		//如果输出标志为Y，则输出日志
		if("Y".equalsIgnoreCase(strIsLog)){
			Date date = new Date();
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
			String d = sd.format(date);
			className=className+""+d;
			writeLog(className,strLogMessage);
		}
	}

	/**
	 * 将信息写入到日志
	 */
	public static boolean writeLog(String className,String ErrorContent){
		String content=info(ErrorContent);
		File fileName = new File(System.getProperty("user.dir")+"/log/"+className+".log");
		//创建文件
		if(!fileName.exists()){
			try {
				fileName.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName,true);
			o.write(content.getBytes("utf-8"));
			o.close();
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return flag;
	}

	/**
	 *
	 * @param ErrorContent 错误内容
	 * @return
	 */
	public static String info(String ErrorContent){
		StringBuffer buffer=new StringBuffer();
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sd.format(date)+"  ";
		buffer.append(d);
		//buffer.append("错误类名为：").append(className).append("\r\n");
		//buffer.append("信息为：").append(ErrorInfo).append("\r\n");
		buffer.append("").append(ErrorContent).append("\r\n");
		return buffer.toString();
	}

	/**
	 * @param strLogMessage
	 * @param obj
	 */
	public static void doWriteLog(String strLogMessage, Object obj) {
		//如果输出标志为Y，则输出日志
		if("Y".equalsIgnoreCase(strIsLog)){
			bb.writeLog(strLogMessage,obj);
		}

	}
	/**
	 * @param strLogMessage
	 * @param obj
	 */
	public static void doSaveLog(Object obj,String strLogMessage) {
		doSaveStrLog(obj.toString(),strLogMessage);
	}
	/**
	 * @param strLogMessage
	 * @param classname
	 */
	public static void doSaveStrLog(String classname,String strLogMessage) {
		if(!"".equals(strLogMessage)){
			strLogMessage=StringEscapeUtils.escapeSql(strLogMessage);
		}
		//如果输出标志为Y，则输出日志
		if("Y".equalsIgnoreCase(strIsLog)){
			Date now=new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String createdate= df.format(now);
			SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
			String createtime= df2.format(now);
			String insersql="insert into uf_xtrz (ffm,rzxx,formmodeid,modedatacreater,modedatacreatertype,"
					+ "modedatacreatedate,modedatacreatetime) values "
					+ "('"+classname+"','"+strLogMessage+"',"+modeid+",1,0,'"+createdate+"','"+createtime+"')";
			Rs.execute(insersql);
		}
	}

}
