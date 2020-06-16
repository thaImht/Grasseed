package com.hetao.grasseed.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumberUtil {
	
	/**
	 * 
	 * @param v
	 * @return 如果为null,返回0
	 */
	public static short toPrimary(Short v) {
		return toPrimary(v, (short)0);
	}
	
	/**
	 * 
	 * @param v
	 * @param defaultValue
	 * @return 如果为null,返回defaultValue
	 */
	public static short toPrimary(Short v, short defaultValue) {
		if(v == null) {
			return defaultValue;
		}
		return v;
	}
	
	/**
	 * 
	 * @param v
	 * @return 如果为null,返回0
	 */
	public static int toPrimary(Integer v) {
		return toPrimary(v, 0);
	}
	
	/**
	 * 
	 * @param v
	 * @param defaultValue
	 * @return 如果为null,返回defaultValue
	 */
	public static int toPrimary(Integer v, int defaultValue) {
		if(v == null) {
			return defaultValue;
		}
		return v;
	}
	
	/**
	 * 
	 * @param v
	 * @return 如果为null,返回0
	 */
	public static long toPrimary(Long v) {
		return toPrimary(v, 0L);
	}
	
	/**
	 * 
	 * @param v
	 * @param defaultValue
	 * @return 如果为null,返回defaultValue
	 */
	public static long toPrimary(Long v, long defaultValue) {
		if(v == null) {
			return defaultValue;
		}
		return v;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static Double bigDecimalToDouble(BigDecimal input) {
		if(input == null) return null;
		
		return input.doubleValue();
	}
	
	/**
	 * 
	 * @param inputList
	 * @return
	 */
	public static List<Double> bigDecimalToDouble(List<BigDecimal> inputList) {
		List<Double> lst = new ArrayList<>();
		if(inputList != null) {
			for(BigDecimal v : inputList) {
				lst.add(bigDecimalToDouble(v));
			}
		}
		return lst;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final Integer toInteger(String s){
		return toInteger(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final Integer toInteger(String s, Integer deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return Integer.valueOf(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<Integer> toInteger(List<String> list, Integer defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<Integer> ret = new ArrayList<>();
		for(String s : list) {
			Integer i = toInteger(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final Long toLong(String s){
		return toLong(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final Long toLong(String s, Long deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return Long.valueOf(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<Long> toLong(List<String> list, Long defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<Long> ret = new ArrayList<>();
		for(String s : list) {
			Long i = toLong(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final Double toDouble(String s){
		return toDouble(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final Double toDouble(String s, Double deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return Double.parseDouble(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<Double> toDouble(List<String> list, Double defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<Double> ret = new ArrayList<>();
		for(String s : list) {
			Double i = toDouble(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final Float toFloat(String s){
		return toFloat(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final Float toFloat(String s, Float deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return Float.parseFloat(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<Float> toFloat(List<String> list, Float defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<Float> ret = new ArrayList<>();
		for(String s : list) {
			Float i = toFloat(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final Short toShort(String s){
		return toShort(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final Short toShort(String s, Short deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return Short.valueOf(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<Short> toShort(List<String> list, Short defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<Short> ret = new ArrayList<>();
		for(String s : list) {
			Short i = toShort(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final Byte toByte(String s){
		return toByte(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final Byte toByte(String s, Byte deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return Byte.valueOf(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<Byte> toByte(List<String> list, Byte defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<Byte> ret = new ArrayList<>();
		for(String s : list) {
			Byte i = toByte(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final BigDecimal toBigDecimal(String s){
		return toBigDecimal(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final BigDecimal toBigDecimal(String s, BigDecimal deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return new BigDecimal(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<BigDecimal> toBigDecimal(List<String> list, BigDecimal defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<BigDecimal> ret = new ArrayList<>();
		for(String s : list) {
			BigDecimal i = toBigDecimal(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final BigInteger toBigInteger(String s){
		return toBigInteger(s, null);
	}
	
	/**
	 * 
	 * @param s
	 * @param deafaultValue
	 * @return
	 */
	public static final BigInteger toBigInteger(String s, BigInteger deafaultValue){
		if( StringUtils.isEmpty(s) ) {
			return deafaultValue;
		}

		try {
			return new BigInteger(s);
		}catch(Exception e) {
			return deafaultValue;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param defaultValue
	 * @return
	 */
	public static final List<BigInteger> toBigInteger(List<String> list, BigInteger defaultValue){
		if(list == null || list.isEmpty()) return Collections.emptyList();
		
		List<BigInteger> ret = new ArrayList<>();
		for(String s : list) {
			BigInteger i = toBigInteger(s, defaultValue);
			ret.add(i);
		}
		
		return ret;
	}

}
