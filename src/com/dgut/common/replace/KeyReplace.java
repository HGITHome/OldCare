package com.dgut.common.replace;

import java.util.List;

public class KeyReplace {
	public static List<Object[]> Gender(List<Object[]> GenderList) {
		for (Object[] o : GenderList) {
			if (o[0].toString() != null && o[0].toString().equals("0")) {
				o[0] = "女";
			}
			if (o[0].toString().equals("1")) {
				o[0] = "男";
			}
		}
		return GenderList;
	}

	public static List<Object[]> diploma(List<Object[]> GiplomaList) {
		for (Object[] o : GiplomaList) {
			if (o[0].toString().equals("A1.1")) {
				o[0] = "文盲";
			} else if (o[0].toString().equals("A1.2")) {
				GiplomaList.get(1)[0] = "小学";
			} else if (o[0].toString().equals("A1.3")) {
				GiplomaList.get(2)[0] = "初中";
			} else if (o[0].toString().equals("A1.4")) {
				GiplomaList.get(3)[0] = "高中/技校/中专";
			} else {
				GiplomaList.get(4)[0] = "大专及以上";
			}
		}
		return GiplomaList;
	}

	public static List<Object[]> religion(List<Object[]> ReligionList) {
		for (Object[] o : ReligionList) {
			if (o[0].toString().equals("0")) {
				o[0] = "无宗教信仰";
			}
			else {
				o[0] = "有宗教信仰";
			}
		}
		return ReligionList;
	}

	public static List<Object[]> marry_status(List<Object[]> Marry_statusList) {
		for(Object[] o :Marry_statusList){
			if (o[0].toString().equals("A2.1")) {
				o[0] = "已婚";
			} else if (o[0].toString().equals("A2.2")) {
				o[0] = "丧偶";
			} else if (o[0].toString().equals("A2.3")) {
				o[0] = "离婚";
			} else if (o[0].toString().equals("A2.4")) {
				o[0] = "未婚";
			} else {
				o[0] = "未知";
			}
		}
		return Marry_statusList;
	}
	public static List<Object[]> town(List<Object[]> TownList) {
		for(Object[] o :TownList){
			if (o[0].toString().equals("1")) {
				o[0] = "莞城";
			} else if (o[0].toString().equals("2")) {
				o[0] = "南城";
			} else if (o[0].toString().equals("3")) {
				o[0] = "东城";
			} else if (o[0].toString().equals("4")) {
				o[0] = "万江";
			} else if (o[0].toString().equals("5")) {
				o[0] = "石碣镇";
			} else if (o[0].toString().equals("6")) {
				o[0] = "石龙镇";
			} else if (o[0].toString().equals("7")) {
				o[0] = "茶山镇";
			} else if (o[0].toString().equals("8")) {
				o[0] = "石排镇";
			} else if (o[0].toString().equals("9")) {
				o[0] = "企石镇";
			} else if (o[0].toString().equals("10")) {
				o[0] = "横沥镇";
			} else if (o[0].toString().equals("11")) {
				o[0] = "桥头镇";
			} else if (o[0].toString().equals("12")) {
				o[0] = "谢岗镇";
			} else if (o[0].toString().equals("13")) {
				o[0] = "东坑镇";
			} else if (o[0].toString().equals("14")) {
				o[0] = "常平镇";
			} else if (o[0].toString().equals("15")) {
				o[0] = "寮步镇";
			} else if (o[0].toString().equals("16")) {
				o[0] = "大朗镇";
			} else if (o[0].toString().equals("17")) {
				o[0] = "黄江镇";
			} else if (o[0].toString().equals("18")) {
				o[0] = "清溪镇";
			} else if (o[0].toString().equals("19")) {
				o[0] = "塘厦镇";
			} else if (o[0].toString().equals("20")) {
				o[0] = "凤岗镇";
			} else if (o[0].toString().equals("21")) {
				o[0] = "长安镇";
			} else if (o[0].toString().equals("22")) {
				o[0] = "虎门镇";	
			} else if (o[0].toString().equals("23")) {
				o[0] = "厚街镇";
			} else if (o[0].toString().equals("24")) {
				o[0] = "沙田镇";
			} else if (o[0].toString().equals("25")) {
				o[0] = "道滘镇";
			} else if (o[0].toString().equals("26")) {
				o[0] = "洪梅镇";	
			} else if (o[0].toString().equals("27")) {
				o[0] = "麻涌镇";
			} else if (o[0].toString().equals("28")) {
				o[0] = "中堂镇";
			} else if (o[0].toString().equals("29")) {
				o[0] = "高埗镇";
			} else if (o[0].toString().equals("30")) {
				o[0] = "樟木头镇";
			} else if (o[0].toString().equals("31")) {
				o[0] = "大岭山镇";
			} else {
				o[0] = "望牛墩镇";
			}
		}
		return TownList;
	}
}
