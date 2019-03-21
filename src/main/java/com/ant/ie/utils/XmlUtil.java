package com.ant.ie.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.ant.ie.model.ExpressTrace;

public class XmlUtil {
	
	public static String mapToXML(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<String> keys = map.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			String k = itr.next();
			String v = map.get(k);
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		System.out.println("生成的XML=>\n"+sb.toString());
		return sb.toString();
	}

	/**
	 * 将string类型的xml数据转换为map
	 * 
	 * @param strxml
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static Map<String, String> doXMLParse(String strxml) throws IOException, JDOMException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		HashMap<String, String> m = new HashMap<>();
		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren();
		Iterator<Element> it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List<Element> children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = XmlUtil.getChildrenText(children);
			}

			m.put(k, v);
		}
		// 关闭流
		in.close();

		return m;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List<Element> children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator<Element> it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List<Element> list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(XmlUtil.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}
	
	/** XML格式字符串转换成MAP,不包括List */
	public static Map<String,String> xmlToMap(String xml){
		try {
			// 获取节点
			Map<String,String> map = new HashMap<>();
			org.dom4j.Document document = DocumentHelper.parseText(xml);
			org.dom4j.Element nodeElement = document.getRootElement();
			List<org.dom4j.Element> node = nodeElement.elements();
			for(Iterator<org.dom4j.Element> it = node.iterator();it.hasNext();) {
				org.dom4j.Element elm = (org.dom4j.Element) it.next();
				map.put(elm.getName(), elm.getText());
				/* 获取属性 */
				List<Attribute> attrList = elm.attributes();
				for(Attribute attribute : attrList) {
					map.put(attribute.getName(), attribute.getValue());
				}
				/* 获取子节点的属性 */
				for(Iterator<org.dom4j.Element> childIt = elm.elementIterator();childIt.hasNext();) {
					org.dom4j.Element childElm = (org.dom4j.Element) childIt.next();
					map.put(childElm.getName(), childElm.getText());
					// 获取属性
					List<Attribute> childAttrList = childElm.attributes();
					for(Attribute childAttribute : childAttrList) {
						map.put(childAttribute.getName(), childAttribute.getValue());
					}
					/* 获取子子节点的属性 
					for(Iterator<org.dom4j.Element> oneIt = elm.elementIterator();oneIt.hasNext();) {
						org.dom4j.Element oneElm = (org.dom4j.Element) oneIt.next();
						map.put(oneElm.getName(), oneElm.getText());
						// 获取属性
						List<Attribute> oneAttrList = oneElm.attributes();
						for(Attribute oneAttribute : oneAttrList) {
							map.put(oneAttribute.getName(), oneAttribute.getValue());
						}
						/* 获取子子子节点的属性 
						for(Iterator<org.dom4j.Element> twoIt = elm.elementIterator();twoIt.hasNext();) {
							org.dom4j.Element twoElm = (org.dom4j.Element) twoIt.next();
							map.put(twoElm.getName(), twoElm.getText());
							// 获取属性
							List<Attribute> twoAttrList = twoElm.attributes();
							for(Attribute twoAttribute : twoAttrList) {
								map.put(twoAttribute.getName(), twoAttribute.getValue());
							}
							twoElm = null;
						}
						oneElm = null;
					}*/
					childElm = null;
				}
				elm = null;
			}
			node = null;
			nodeElement = null;
			document = null;
			return map;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* 顺丰xml解析出路由数据 */
	public static List<ExpressTrace> xmlToListExpress(String xml){
		List<ExpressTrace> le = new ArrayList<>();
		ExpressTrace et = new ExpressTrace();
		try {
			// 获取节点
			Map<String,String> map = new HashMap<>();
			org.dom4j.Document document = DocumentHelper.parseText(xml);
			org.dom4j.Element nodeElement = document.getRootElement();
			List<org.dom4j.Element> node = nodeElement.elements();
			for(Iterator<org.dom4j.Element> it = node.iterator();it.hasNext();) {
				org.dom4j.Element elm = (org.dom4j.Element) it.next();
				map.put(elm.getName(), elm.getText());
				/* 获取属性 */
				List<Attribute> attrList = elm.attributes();
				for(Attribute attribute : attrList) {
					map.put(attribute.getName(), attribute.getValue());
				}
				/* 获取子节点的属性 */
				for(Iterator<org.dom4j.Element> childIt = elm.elementIterator();childIt.hasNext();) {
					org.dom4j.Element childElm = (org.dom4j.Element) childIt.next();
					map.put(childElm.getName(), childElm.getText());
					// 获取属性
					List<Attribute> childAttrList = childElm.attributes();
					for(Attribute childAttribute : childAttrList) {
						map.put(childAttribute.getName(), childAttribute.getValue());
					}
					/* 获取子子节点的属性 */
					for(Iterator<org.dom4j.Element> oneIt = childElm.elementIterator();oneIt.hasNext();) {
						org.dom4j.Element oneElm = (org.dom4j.Element) oneIt.next();
						map.put(oneElm.getName(), oneElm.getText());
						// 获取属性
						List<Attribute> oneAttrList = oneElm.attributes();
						for(Attribute oneAttribute : oneAttrList) {
							if("accept_address".equals(oneAttribute.getName())) {	// 地址
								et.setOpeAddress(oneAttribute.getValue());
							}
							if("remark".equals(oneAttribute.getName())) {			// 说明
								et.setOpeRemark(oneAttribute.getValue());
							}
							if("accept_time".equals(oneAttribute.getName())) {		// 时间
								et.setOpeTime(oneAttribute.getValue());
							}
							map.put(oneAttribute.getName(), oneAttribute.getValue());
						}
						le.add(et);
						/* 获取子子子节点的属性 
						for(Iterator<org.dom4j.Element> twoIt = elm.elementIterator();twoIt.hasNext();) {
							org.dom4j.Element twoElm = (org.dom4j.Element) twoIt.next();
							map.put(twoElm.getName(), twoElm.getText());
							// 获取属性
							List<Attribute> twoAttrList = twoElm.attributes();
							for(Attribute twoAttribute : twoAttrList) {
								map.put(twoAttribute.getName(), twoAttribute.getValue());
							}
							twoElm = null;
						}*/
						oneElm = null;
					}
					childElm = null;
				}
				elm = null;
			}
			node = null;
			nodeElement = null;
			document = null;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return le;
	}
}