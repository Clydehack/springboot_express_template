package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceUtil {

	private static Logger log = LoggerFactory.getLogger(ServiceUtil.class);

	private static Map<String, String> map = null;

	private static boolean debug = true;

	private static String serviceConfigName;

	/**
	 * 读取配置文件 通用方法 出过来文件路径
	 * 
	 * @param 配置文件的路径
	 * 
	 * @return map 集合
	 */
	public static Map<String, String> readProperties(String filePath) {
		Map<String, String> map = new HashMap<String, String>();
		InputStream in = null;
		try {
			in = ServiceUtil.class.getResourceAsStream(filePath);
			Properties props = new Properties();
			props.load(in);
			Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				map.put(key, Property);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
		return map;
	}

	/**
	 * 调用接口方法的通用方法
	 * 
	 * @param 服务接口参数
	 * 
	 * @param 服务url
	 *            key servicePath.properties配置
	 * 
	 * @return 返回字符串
	 */
	public static String invokeService(String str, String serviceUrl) {
		return invokeService(str, serviceUrl, null, null);
	}

	public static String invokeService(String str, String serviceUrl, Map<String, String> headerMap,
			String httpMethod) {
		HttpURLConnection connection = null;
		String dataString = null;
		String charset = "UTF-8";
		httpMethod = httpMethod == null ? "POST" : httpMethod;
		try {
			String tempUrl = null;
			if (serviceUrl.startsWith("http://") || serviceUrl.startsWith("https://")) {
				tempUrl = serviceUrl;
			} else {
				tempUrl = map.get(serviceUrl);
			}
			if ("GET".equals(httpMethod))
				tempUrl = tempUrl + "?" + str;
			// 调用参数记入日志
			log.debug(tempUrl + str);
			// Create connection
			URL url = new URL(tempUrl);
			URLConnection temp = url.openConnection();
			if (temp instanceof HttpsURLConnection) {
				HttpsURLConnection httpsConn = (HttpsURLConnection) temp;
				httpsConn.setSSLSocketFactory(createSSLSocketFactory());
				connection = httpsConn;
			} else {
				connection = (HttpURLConnection) temp;
			}
			connection.setConnectTimeout(20 * 1000);
			connection.setReadTimeout(20 * 1000);
			connection.setRequestMethod(httpMethod);
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			} else {

				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
				connection.setRequestProperty("Charset", charset);
				connection.setRequestProperty("Accept", "text/html,application/json,application/xml");
				// connection.setRequestProperty("Accept","textml,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			}
			byte[] data = str.getBytes(charset);
			if (!"GET".equals(httpMethod)) {
				connection.setRequestProperty("Content-Length", data.length + "");
			}
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			if (!"GET".equals(httpMethod)) {
				/*
				 * DataOutputStream out = new DataOutputStream(connection .getOutputStream());//
				 * 打开输出流往对端服务器写数据 out.write(str.getBytes());// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
				 * out.flush();// 刷新 out.close();// 关闭输出流
				 */
				OutputStream os = connection.getOutputStream();
				os.write(data);
				os.flush();
			}
			// Get Response
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				if (debug) {
					Map<String, List<String>> map = connection.getHeaderFields();
					for (Map.Entry<String, List<String>> entry : map.entrySet()) {
						System.out.println("Key:" + entry.getKey() + ",Value:" + entry.getValue());
					}
				}
				InputStream is = connection.getInputStream();

				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String line = null;
				StringBuilder response = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
				dataString = response.toString();
			} else {
				InputStream is = connection.getErrorStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String line = null;
				StringBuilder response = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
				log.error("invokeService error:" + response.toString());
			}
		} catch (Exception e) {
			log.error("invokeService", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return dataString;
	}
	
	/* 做聚合服务时拓展的，是否有用待定 */
	public static String invokeService(String str, String serviceUrl, Map<String, String> headerMap, String httpMethod,
			String Nul) {
		HttpURLConnection connection = null;
		String dataString = null;
		String charset = "UTF-8";
		httpMethod = httpMethod == null ? "POST" : httpMethod;
		try {
			String tempUrl = null;
			if (serviceUrl.startsWith("http://") || serviceUrl.startsWith("https://")) {
				tempUrl = serviceUrl;
			} else {
				tempUrl = map.get(serviceUrl);
			}
			if ("GET".equals(httpMethod))
				tempUrl = tempUrl + "?" + str;
			// Create connection
			URL url = new URL(tempUrl);
			URLConnection temp = url.openConnection();
			if (temp instanceof HttpsURLConnection) {
				HttpsURLConnection httpsConn = (HttpsURLConnection) temp;
				httpsConn.setSSLSocketFactory(createSSLSocketFactory());
				connection = httpsConn;
			} else {
				connection = (HttpURLConnection) temp;
			}
			connection.setConnectTimeout(20 * 1000);
			connection.setReadTimeout(20 * 1000);
			connection.setRequestMethod(httpMethod);
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			} else {

				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
				connection.setRequestProperty("Charset", charset);
				// connection.setRequestProperty("Accept","text/html,application/json,application/xml");
				// connection.setRequestProperty("Accept","textml,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			}
			byte[] data = str.getBytes(charset);
			if (!"GET".equals(httpMethod)) {
				connection.setRequestProperty("Content-Length", data.length + "");
			}
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			if (!"GET".equals(httpMethod)) {
				/*
				 * DataOutputStream out = new DataOutputStream(connection .getOutputStream());//
				 * 打开输出流往对端服务器写数据 out.write(str.getBytes());// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
				 * out.flush();// 刷新 out.close();// 关闭输出流
				 */
				OutputStream os = connection.getOutputStream();
				os.write(data);
				os.flush();
			}
			// Get Response
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				if (debug) {
					Map<String, List<String>> map = connection.getHeaderFields();
					for (Map.Entry<String, List<String>> entry : map.entrySet()) {
						System.out.println("Key:" + entry.getKey() + ",Value:" + entry.getValue());
					}
				}
				InputStream is = connection.getInputStream();

				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String line = null;
				StringBuilder response = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
				dataString = response.toString();
			} else {
				InputStream is = connection.getErrorStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String line = null;
				StringBuilder response = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
				log.error("invokeService error:" + response.toString());
			}
		} catch (Exception e) {
			log.error("invokeService", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return dataString;
	}

	/**
	 * 2018年5月4-5月10，主索引迁移到springboot，聚合服务→卡管正常，卡管和聚合服务 →主索引乱码，原版： 将参数转换成post提交参数格式
	 * example a=11&b=21&c=31
	 * 
	 * @param para
	 *            参数
	 * @return String
	 */
	public static String convertPostParameter(Map<String, String> para) {
		if (para == null)
			return "";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		try {
			for (Map.Entry<String, String> entry : para.entrySet()) {
				if (first) {
					sb.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=')
							.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
					first = false;
				} else {
					sb.append('&');
					sb.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=')
							.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported UTF-8");
		}
		return sb.toString();
	}

	/**
	 * 2018年5月4-5月10，主索引迁移到springboot，聚合服务→卡管正常，卡管和聚合服务
	 * →主索引乱码，新版：可能是两边转码的方法不一样，导致主索引接到的数据是乱码 将参数转换成post提交参数格式 example a=11&b=21&c=31
	 * 
	 * @param para
	 *            参数
	 * @return String
	 * 
	 *         public static String convertPostParameter(Map<String, String> para){
	 *         if(para==null) return ""; StringBuilder sb = new StringBuilder();
	 *         boolean first = true; try { for(Map.Entry<String, String> entry :
	 *         para.entrySet()){ if(first){
	 *         sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),
	 *         "UTF-8")); first = false; }else{ sb.append('&');
	 *         sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),
	 *         "UTF-8")); } } } catch (UnsupportedEncodingException e) { // TODO
	 *         Auto-generated catch block e.printStackTrace(); } return
	 *         sb.toString(); }
	 */

	private static SSLSocketFactory createSSLSocketFactory() throws Exception {
		String trustStore = map.get("trustStore");
		String storePass = map.get("storePass");
		String storetype = map.get("storeType");
		storetype = storetype == null ? "JKS" : storetype;
		InputStream is = null;
		SSLSocketFactory ssf = null;
		try {
			KeyStore keystore = KeyStore.getInstance(storetype);
			is = ServiceUtil.class.getClassLoader().getResourceAsStream(trustStore);
			keystore.load(is, storePass.toCharArray());
			KeyManagerFactory keymanagerfactory = KeyManagerFactory.getInstance("SunX509");
			keymanagerfactory.init(keystore, storePass.toCharArray());
			KeyManager akeymanager[] = keymanagerfactory.getKeyManagers();
			TrustManagerFactory trustmanagerfactory = TrustManagerFactory.getInstance("SunX509");
			trustmanagerfactory.init(keystore);
			TrustManager atrustmanager[] = trustmanagerfactory.getTrustManagers();
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(akeymanager, atrustmanager, null);
			ssf = sslcontext.getSocketFactory();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
		}
		return ssf;
	}

	public static void sendData(String str, String serviceUrl, int connectTimeout, int readTimeout,
			Map<String, String> headerMap) throws Exception {
		HttpURLConnection connection = null;
		String charset = "UTF-8";
		try {
			byte[] data = str.getBytes(charset);
			// Create connection
			URL url = new URL(map.get(serviceUrl));
			URLConnection temp = url.openConnection();
			if (temp instanceof HttpsURLConnection) {
				HttpsURLConnection httpsConn = (HttpsURLConnection) temp;
				httpsConn.setSSLSocketFactory(createSSLSocketFactory());
				connection = httpsConn;
			} else {
				connection = (HttpURLConnection) temp;
			}
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Length", data.length + "");
			connection.setRequestProperty("Accept", "text/html,application/json,application/xml");
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			OutputStream os = connection.getOutputStream();
			os.write(data);
			os.flush();

			// Get Response
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
			} else {
				InputStream is = connection.getErrorStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String line = null;
				StringBuilder response = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
				throw new Exception(response.toString());
			}
		} catch (Exception e) {
			log.error("sendData", e);
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public void setDebug(boolean debug) {
		ServiceUtil.debug = debug;
	}

	public void setServiceConfigName(String serviceConfigName) {
		ServiceUtil.serviceConfigName = serviceConfigName;
		map = readProperties('/' + ServiceUtil.serviceConfigName);
	}

	public static void main(String[] arg) {
		Map<String, String> para = new HashMap<String, String>();
		// para.put("type","1");
		// para.put("personIdentifier","530801197603062217");
		para.put("appId", "65e2219a5f3a7b57771c9a115deaa436fd445f4a");
		para.put("appSecret", "9227b810769ac201f2c90b13560d8892f5fed3ca043a176499f205c8c9e3636e");
		String data = ServiceUtil.convertPostParameter(para);
		// data = ServiceUtil.invokeService(data,"person.interior.queryPersonInfo");
		data = ServiceUtil.invokeService(data, "oauth2.accessToken");
		System.out.println(data);
	}
}