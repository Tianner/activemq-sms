package com.sunwoda.activemq.sms.utils;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class MsgUtils {
	//签名
	private static String signName;
	//模板
	private static String templateCode;
	//短信模板
	private static String templateParam;
	//outId
	private static String outId;
	
	private static IClientProfile profile;
	static{
		ResourceBundle bundle = ResourceBundle.getBundle("dayuMsg");
		String product = bundle.getString("dayu.product");
		String domain = bundle.getString("dayu.domain");
		String accessKeyId = bundle.getString("dayu.accessKeyId");
		String accessKeySecret = bundle.getString("dayu.accessKeySecret");
		signName = bundle.getString("dayu.signName");
		templateCode = bundle.getString("dayu.templateCode");
		templateParam = bundle.getString("dayu.templateParam");
		outId = bundle.getString("dayu.outId");
		profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
				accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	public static boolean sendMsg(String phoneNumber,String...checkCode) throws Exception{
		IAcsClient acsClient = new DefaultAcsClient(profile);
		SendSmsRequest request = new SendSmsRequest();
		
		request.setPhoneNumbers(phoneNumber);
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName(signName);
		 //必填:短信模板-可在短信控制台中找到
		 request.setTemplateCode(templateCode);
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 String msg = pastMsg(templateParam,checkCode);
		 System.out.println(msg);
		 request.setTemplateParam(msg);
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 request.setOutId(outId);
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
		//请求成功
			return true;
		}else{
			return false;
		}
	}
	private static String pastMsg(String template, String...checkCode) throws Exception {
		if(template == null){
			throw new Exception("短信模板不匹配");
		}
		Pattern pattern = Pattern.compile("\\$\\{\\w*\\}");
		Matcher matcher = pattern.matcher(template);
		ArrayList<String> list = new ArrayList<>();
		while(matcher.find()){
			String group = matcher.group();
			group = group.substring(2, group.length()-1);
			list.add(group);
		}
		System.out.println(list);
		if(list.size() != checkCode.length){
			throw new Exception("短信模板不匹配");
		}
		String result = "{";
		for(int i = 0;i<checkCode.length;i++){
			result = result + "\""+list.get(i)+"\":\""+checkCode[i]+"\",";
		}
		result = result.substring(0,result.length()-1)+"}";
		return result;
	}
}
