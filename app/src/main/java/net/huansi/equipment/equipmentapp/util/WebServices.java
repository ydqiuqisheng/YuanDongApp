package net.huansi.equipment.equipmentapp.util;

import android.content.Context;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;

import static net.huansi.equipment.equipmentapp.constant.Constant.CHECK_CODE;
import static net.huansi.equipment.equipmentapp.util.SPHelper.IP_KEY;

public class WebServices {
	//public String FServerAddr = "http://192.168.2.61";
	//public String FEndPoint = FServerAddr + "/APPWS/AppWS.asmx";

//	public String FServerAddr = "http://202.181.229.247";
//	public String FEndPoint = FServerAddr + "/MobileQA/AppWS.asmx";
	//HS服务器端的地址；

	public String FEndPoint = "";
	public String FNameSpace ="http://tempuri.org/";
	public String FCheckCode = "APP008";

	//---------------------------------------------------------------------------------------
	public WebServices(Context context) {
        String localData = SPHelper.getLocalData(context, IP_KEY, String.class.getName(), "").toString();
        Log.e("TAG","local="+localData);
        FEndPoint ="http://"+SPHelper.getLocalData(context,IP_KEY,String.class.getName(),"");
		FEndPoint = FEndPoint + "/AppWS.asmx";
		FCheckCode = CHECK_CODE;
	}


	public String GetData(String sFunctionName,Map<String,String> map) {
        // 命名空间
        String nameSpace = FNameSpace;

        // 调用的方法名称
        String methodName = sFunctionName;

        // EndPoint
        String endPoint = FEndPoint;

        // SOAP Action
        String soapAction = nameSpace + methodName;
        Log.e("TAG","soapAction1="+soapAction);
        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        rpc.addProperty("sCheckCode", FCheckCode);
        //rpc.addProperty("sUserNo", sUserNo);
        //rpc.addProperty("sPassword", sPassword);
        String sParmName="";
        String sParaValue="";
        rpc.addProperty("sCheckCode", FCheckCode);
        if(map!=null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sParmName = entry.getKey();
                sParaValue = entry.getValue();
                Log.e("TAG","sParmName="+sParmName);
                Log.e("TAG","sParmValue="+sParaValue);
                rpc.addProperty(sParmName, sParaValue);
            }
        }
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint,1000*60);//endPoint=http://10.17.111.23:8064/AppWS.asmx
        try {
            // 调用WebService
            transport.call(soapAction, envelope);//（命名空间拼接方法名，SOAP请求信息）
            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;
//			SoapObject object=envelope.sObject.getProperty("return");
//			SoapObject soReturn = (SoapObject)object.getProperty("return");
//			soReturn.getProperty("insurer").toString();
            String result = "";
            // 获取返回的结果
            if(object!=null) {
                result = object.getProperty(0).toString();
            }
            //Log.e("TAG","result="+result.toString());
            return (result==null||result.isEmpty()||result.equals("anyType{}"))?"":result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String GetDataExt(String sFunctionName,Map<String,String> map) {
        // 命名空间
        String nameSpace = FNameSpace;

        // 调用的方法名称
        String methodName = sFunctionName;

        // EndPoint
        String endPoint = FEndPoint;

        // SOAP Action
        String soapAction = nameSpace + methodName;
        Log.e("TAG","soapAction2="+soapAction);
        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        rpc.addProperty("sCheckCode", FCheckCode);
        if(map!=null) {
            rpc.addProperty("DBConnName",map.get("DBConnName"));
            rpc.addProperty("str",map.get("str"));
            rpc.addProperty("sParaStr",map.get("sParaStr"));
        }
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint,1000*60);//endPoint=http://10.17.111.23:8064/AppWS.asmx
        try {
            // 调用WebService
            transport.call(soapAction, envelope);//（命名空间拼接方法名，SOAP请求信息）
            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;
//			SoapObject object=envelope.sObject.getProperty("return");
//			SoapObject soReturn = (SoapObject)object.getProperty("return");
//			soReturn.getProperty("insurer").toString();
            String result = "";
            // 获取返回的结果
            if(object!=null) {
                result = object.getProperty(0).toString();
            }
            //Log.e("TAG","result="+result.toString());
            return (result==null||result.isEmpty()||result.equals("anyType{}"))?"":result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
