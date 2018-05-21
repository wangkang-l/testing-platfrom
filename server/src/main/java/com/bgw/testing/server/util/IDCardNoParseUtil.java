package com.bgw.testing.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDCardNoParseUtil {

    /**

     * 根据身份证号码计算年龄

     * @param idCardNo

     * @return

     */
    public static Integer IDCardNoToAge(String idCardNo){

        int length = idCardNo.length();

        String dates = "";

        if(length == 18){

//            int se = Integer.valueOf(idCardNo.substring(length-1))%2;

            dates = idCardNo.substring(6,10);

            SimpleDateFormat df = new SimpleDateFormat("yyyy");

            String year = df.format(new Date());

            int u = Integer.parseInt(year)-Integer.parseInt(dates);

            return u ;

        }else{

            if(length == 15){

                dates = idCardNo.substring(6,8);

                return Integer.parseInt(dates);

            }else{

                return 0;

            }

        }

    }

    /**

     * 根据身份证号码计算出生日期

     * @param idCardNo

     * @return

     */

    public static Date IDCardNoToBirthday(String idCardNo){

        if(idCardNo.length() != 18 ){

            return null;

        }

        //通过String[]的subString方法来读取信息

        String yyyy = idCardNo.substring(6, 10);//出生的年份

        String mm = idCardNo.substring(10,12);

        String dd = idCardNo.substring(12,14);

        String birth = yyyy.concat("-").concat(mm).concat("-").concat(dd);

        Date date = new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date birthday = null;

        try {

            birthday = df.parse(birth);

        } catch (ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return birthday;

    }

    /**

     * 校验身份证

     * @param idCardNo

     * @return

     */

    public static  String isValidDate(String idCardNo){

        String errorInfo = "";//记录错误信息

        //最后一位身份证的号码

        String[] ValCodeArr = {"1","0","x","9","8","7","6","5","4","3","2"};

        //17位系数

        String[] Coefficient = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",

                "9", "10", "5", "8", "4", "2"};

        String Ai = "";

        //=============号码长度15位或18位

        if(idCardNo.length() != 15 && idCardNo.length() != 18){

            errorInfo ="身份证无效";

            return errorInfo;

        }

        //============前17位应为纯数字

        if(idCardNo.length() == 18){

            Ai = idCardNo.substring(0, 17);

        }else if(idCardNo.length() == 15){

            Ai = idCardNo.substring(0,6)+"19"+idCardNo.substring(6,15);//中间拼的“19”是年数“19**”年

        }

        if(isNumeric(Ai) == false){

            errorInfo="身份证无效";

            return errorInfo;

        }

        //===========出生年月是否有效

        String IdCardNoYear = Ai.substring(6,10);

        String IdCardNoMonth = Ai.substring(10,12);

        String IdCardNoDay = Ai.substring(12,14);

        if(isDate(IdCardNoYear+"-"+IdCardNoMonth+"-"+IdCardNoDay) == false){

            errorInfo = "身份证无效";

        }

        GregorianCalendar gc = new GregorianCalendar();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {

            if((gc.get(Calendar.YEAR)-Integer.parseInt(IdCardNoYear))>150 || (gc.getTime().getTime()-sdf.parse(IdCardNoYear+"-"+

                    IdCardNoMonth+"-"+IdCardNoDay).getTime())<0){

                errorInfo = "身份证无效";

                return errorInfo;

            }

        } catch (NumberFormatException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        //月份范围1-12

        if(Integer.parseInt(IdCardNoMonth)>12 || Integer.parseInt(IdCardNoMonth)==0){

            errorInfo = "身份证无效";

            return errorInfo;

        }

        //日期范围1-31

        if(Integer.parseInt(IdCardNoDay)>31 || Integer.parseInt(IdCardNoDay) == 0){

            errorInfo = "身份证无效";

            return errorInfo;

        }

        //========地区码

        Hashtable Hashtable =  GetAreaCode();

        if(Hashtable.get(Ai.substring(0,2))== null){

            errorInfo = "身份证无效";

            return errorInfo;

        }

        //======判断最后一位值

        int TotalmulAiCoefficient = 0;

        for(int i = 0;i<17;i++){

            TotalmulAiCoefficient = TotalmulAiCoefficient + Integer.parseInt(String.valueOf(Ai.charAt(i)))*Integer.parseInt(Coefficient[i]);

        }

        int modValue = TotalmulAiCoefficient%11;

        String strVerifyCode = ValCodeArr[modValue];

        Ai = Ai+strVerifyCode;

        if(idCardNo.length() == 18){

            if(Ai.equals(idCardNo) == false){

                errorInfo ="身份证无效";

                return errorInfo;

            }

        }else{

            return "身份证校验通过";

        }

        return "身份证校验通过";

    }


    /**

     * 判断字符串是否为数字

     * @param ai

     * @return

     */

    private static boolean isNumeric(String ai) {

        // TODO Auto-generated method stub

        Pattern pattern = Pattern.compile("[0-9]*");

        Matcher isNum = pattern.matcher(ai);

        if(isNum.matches()){

            return true;

        }else{

            return false;

        }

    }

    /**

     * 判断字符串是否为日期格式

     * @return

     */

    private static boolean isDate(String strDate){

        // TODO Auto-generated method stub

        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

        Matcher matcher = pattern.matcher(strDate);

        if(matcher.matches()){

            return true;

        }else{

            return false;

        }

    }


    /**

     * 地区表

     * @return

     */

    @SuppressWarnings("unchecked")

    private static Hashtable GetAreaCode() {

        Hashtable hashtable = new Hashtable();

        hashtable.put("11", "北京");

        hashtable.put("12", "天津");

        hashtable.put("13", "河北");

        hashtable.put("14", "山西");

        hashtable.put("15", "内蒙古");

        hashtable.put("21", "辽宁");

        hashtable.put("22", "吉林");

        hashtable.put("23", "黑龙江");

        hashtable.put("31", "上海");

        hashtable.put("32", "江苏");

        hashtable.put("33", "浙江");

        hashtable.put("34", "安徽");

        hashtable.put("35", "福建");

        hashtable.put("36", "江西");

        hashtable.put("37", "山东");

        hashtable.put("41", "河南");

        hashtable.put("42", "湖北");

        hashtable.put("43", "湖南");

        hashtable.put("44", "广东");

        hashtable.put("45", "广西");

        hashtable.put("46", "海南");

        hashtable.put("50", "重庆");

        hashtable.put("51", "四川");

        hashtable.put("52", "贵州");

        hashtable.put("53", "云南");

        hashtable.put("54", "西藏");

        hashtable.put("61", "陕西");

        hashtable.put("62", "甘肃");

        hashtable.put("63", "青海");

        hashtable.put("64", "宁夏");

        hashtable.put("65", "新疆");

        hashtable.put("71", "台湾");

        hashtable.put("81", "香港");

        hashtable.put("82", "澳门");

        hashtable.put("91", "国外");

        return hashtable;

    }

}
