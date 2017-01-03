//package com.yufei.cache;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//import cn.org.rapid_framework.util.holder.ApplicationContextHolder;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wsmc.common.cache.CacheConstant;
//import com.wsmc.common.cache.ICacheClient;
//import com.wsmc.ent.model.TentUser;
//import com.wsmc.ent.model.result.TentCompany;
//import com.wsmc.sys.model.TsysFile;
//import com.wsmc.sys.model.TsysGeography;
//import com.wsmc.sys.model.TsysUser;
//
//public class CacheUtil {
//
//	private static final String T_SYS_USER_SQL = "select id ,user_name as userName,real_name as realName,password ,mobile ,status ,last_login_ip as lastLoginIp,last_login_time as lastLoginTime,remark ,creator ,create_time as createTime,last_modifior as lastModifior,last_modify_time as lastModifyTime,deleted from t_sys_user ";
//	private static final String T_ENT_USER_SQL = "select id ,user_name as userName,real_name as realName,password ,mobile ,status ,source,last_login_ip as lastLoginIp,last_login_time as lastLoginTime,remark ,creator ,create_time as createTime,last_modifior as lastModifior,last_modify_time as lastModifyTime,deleted from t_ent_user ";
//	private static final String T_SYS_GEOGRAPHY_SQL = "select id ,parent_id as parentId,parent_ids as parentIds,name_cn as nameCn,name_short_cn as nameShortCn,name_en as nameEn,name_short_en as nameShortEn,name_pinyin as namePinyin,name_short_pinyin as nameShortPinyin,position ,level ,sort ,hot ,cross_section as crossSection,description ,status ,creator ,create_time as createTime,last_modifior as lastModifior,last_modify_time as lastModifyTime,deleted from t_sys_geography ";
//	private static final String T_SYS_FILE_SQL = "select id ,file_input_name as fileInputName,file_name as fileName,file_path as filePath,file_real_path as fileRealPath,file_type as fileType,remarks ,creator ,create_time as createTime,last_modifior as lastModifior,last_modify_time as lastModifyTime from t_sys_file";
//	private static final String T_ENT_COMPANY_SQL = "select id ,user_id as userId,company_name as companyName,big_type as bigType,small_type as smallType,attribute ,business ,reg_time as regTime,reg_capital as regCapital,business_license as businessLicense,license_pic as licenstPic,province_id as provinceId,city_id as cityId,area_id as areaId,address as address,company_telephone as companyTelephone,contact ,mobile ,web ,email ,logo_pic as logoPic,publicity_pic as publicityPic,profile ,first_review as firstReview,mess_review as messReview,auth_review as authReview,creator ,create_time as createTime,last_modifior as lastModifior,last_modify_time as lastModifyTime,deleted FROM t_ent_company";
//
//	//默认3天
//	private static final int DEFAULT_EXPIRED_TIME = 29*24*60*60;
//
//	/**
//	 * 删除指定单条记录的缓存数据
//	 * @param prefixKey
//	 * @param key
//	 */
//	public static void delete4Object(String prefixKey, String key){
//		ICacheClient client = WebContextHolder.getCacheClient();
//		String cacheKey = prefixKey+":"+key;
//		client.delete(cacheKey);
//		cacheKey = prefixKey+"_JSON:"+key;
//		client.delete(cacheKey);
//		String newcacheKey = prefixKey+"_PID_JSON:"+key;
//		client.delete(newcacheKey);
//	}
//
//	/**
//	 * 新增缓存数据
//	 */
//	public static void save4Object(String prefixKey, String key,Object o){
//		String jsonString = JSON.toJSONString(o);
//		String cacheKey = prefixKey+"_JSON:"+key;
//		ICacheClient client = WebContextHolder.getCacheClient();
//		client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, jsonString);
//	}
//
//	/**
//	 * 更新指定单条记录的缓存数据
//	 * @param prefixKey
//	 * @param key
//	 */
//	public static void update4Object(String prefixKey, String key,Object o){
//		ICacheClient client = WebContextHolder.getCacheClient();
//		String cacheKey = prefixKey+"_JSON:"+key;
//		String jsonString = JSON.toJSONString(o);
//		client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, jsonString);
//		String newcacheKey = prefixKey+"_PID_JSON:"+key;
//		client.delete(newcacheKey);
//	}
//
//	/**
//	 * 通过KEY从缓存中获取Object数据，若在缓存中不存在，则从数据库中读取,并放入缓存。
//	 * @param clazz
//	 * @param prefixKey
//	 * @param key
//	 */
//	public static <T> T get4Object(Class<T> clazz , String prefixKey, String key){
//		ICacheClient client = WebContextHolder.getCacheClient();
//		String cacheKey = prefixKey+"_JSON:"+key;
//		String value = client.get_2_0(cacheKey);
//		T t = null;
//		if(StringUtils.isBlank(value)){
//			try{
//				value = get4ObjectJson_2_0(prefixKey, key);
//				if(StringUtils.isNotBlank(value)){
//					client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, value);
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		if(StringUtils.isNotBlank(value)){
//			t = JSONObject.parseObject(value, clazz);
//		}
//
//		return t;
//	}
//
//	private static String get4ObjectJson_2_0(String prefixKey, String key){
//		String result = "";
//		JdbcTemplate jdbcDao = (JdbcTemplate)ApplicationContextHolder.getBean("jdbcTemplate");
//		List<Map<String, Object>> sqlList = jdbcDao.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND  CACHE_KEY=?","Object",prefixKey);
//		List<Map<String, Object>> queryForList = null;
//		if(sqlList != null){
//			String sql = (String)sqlList.get(0).get("SQL_TEXT");
//			if(StringUtils.isNotBlank(sql)){
//				queryForList = jdbcDao.queryForList(sql, key);
//				if(queryForList != null && queryForList.size() == 1){
//					result = JSON.toJSONString(queryForList.get(0));
//				}
//			}
//		}
//
//		return result;
//	}
//
//	/**
//	 * 通过KEY从缓存中获取List数据，若在缓存中不存在，则从数据库中读取，并放入缓存
//	 * @param clazz
//	 * @param key
//	 */
//
//	public static <T> List<T> get4List_2_0(Class<T> clazz ,String key){
//		ICacheClient client = WebContextHolder.getCacheClient();
//		String result = client.get_2_0(key);
//		List<T>  cacheList = null;
//		if(StringUtils.isNotBlank(result)){
//			cacheList = JSON.parseArray(result, clazz);
//		}
//
//		if(cacheList == null){
//
//			if(clazz == String.class){
//				cacheList = (List<T>) get4ListString(key);
//			}else {
//				cacheList = getListObjectFromDb(clazz, key,null);
//			}
//
//			if(cacheList != null && cacheList.size() > 0){
//				client.set_2_0(key, DEFAULT_EXPIRED_TIME, JSON.toJSONString(cacheList));
//			}
//		}
//
//		return cacheList == null ? new ArrayList<T>() : cacheList;
//	}
//
//	private static List<String> get4ListString(String key){
//
//		JdbcTemplate jdbcDao = (JdbcTemplate)ApplicationContextHolder.getBean("jdbcTemplate");
//		List<Map<String, Object>> sqlList = jdbcDao.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND CACHE_KEY=?","List",key);
//		if(sqlList.size() != 0){
//			String sqlText = (String)sqlList.get(0).get("SQL_TEXT");
//			if(StringUtils.isNotBlank(sqlText)){
//
//				RowMapper<String> rowMapper = null;
//				List<String> cacheList = jdbcDao.query(sqlText, rowMapper);
//				if(cacheList != null){
//					return cacheList;
//				}
//
//			}
//		}
//
//		return new ArrayList<String>();
//	}
//
//	private static <T> List<T> getListObjectFromDb(Class<T> clazz ,String prefixKey,String key) {
//		List<T> cacheList = new ArrayList<T>();
//		try {
//			String result = get4ListObjectJson(prefixKey,key);
//			if(StringUtils.isNotBlank(result)){
//				cacheList = JSON.parseArray(result, clazz);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return cacheList;
//	}
//
//	private static String get4ListObjectJson(String prefixKey,String key) {
//		JdbcTemplate jdbcDao = (JdbcTemplate)ApplicationContextHolder.getBean("jdbcTemplate");
//		List<Map<String, Object>> sqlList = jdbcDao.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND CACHE_KEY=?","List",prefixKey);
//		if(sqlList.size() != 0){
//			String sqlText = (String)sqlList.get(0).get("SQL_TEXT");
//			if(StringUtils.isNotBlank(sqlText)){
//				List<Map<String, Object>> cacheList = jdbcDao.queryForList(sqlText, key);
//				return JSON.toJSONString(cacheList);
//			}
//		}
//		return "";
//	}
//
//	/**
//	 * 从数据库读取同步方法
//	 * @param key
//	 * @return
//	 */
//	public static void setRedisFromDB(String key){
//		ICacheClient client = WebContextHolder.getCacheClient();
//		List<String> values = getDBDate(key);
//		if(CollectionUtils.isNotEmpty(values)){
//			if(CacheConstant.T_SYS_USER.equals(key)){
//				for(String value : values){
//					TsysUser tsysUser = JSON.parseObject(value, TsysUser.class);
//					client.set_2_0(key+"_JSON:"+tsysUser.getId(), DEFAULT_EXPIRED_TIME, value);
//				}
//			}else if(CacheConstant.T_ENT_USER.equals(key)){
//				for(String value : values){
//					TentUser tentUser = JSON.parseObject(value, TentUser.class);
//					client.set_2_0(key+"_JSON:"+tentUser.getId(), DEFAULT_EXPIRED_TIME, value);
//				}
//			}else if(CacheConstant.T_SYS_GEOGRAPHY.equals(key)){
//				for(String value : values){
//					TsysGeography tsysGeography = JSON.parseObject(value, TsysGeography.class);
//					client.set_2_0(key+"_JSON:"+tsysGeography.getId(), DEFAULT_EXPIRED_TIME, value);
//				}
//			}else if(CacheConstant.T_SYS_FILE.equals(key)){
//				for(String value : values){
//					TsysFile tsysFile = JSON.parseObject(value, TsysFile.class);
//					client.set_2_0(key+"_JSON:"+tsysFile.getId(), DEFAULT_EXPIRED_TIME, value);
//				}
//			}else if(CacheConstant.T_ENT_COMPANY.equals(key)){
//				for(String value : values){
//					TentCompany tentCompany = JSON.parseObject(value, TentCompany.class);
//					client.set_2_0(key+"_JSON:"+tentCompany.getId(), DEFAULT_EXPIRED_TIME, value);
//				}
//			}
//
//		}
//	}
//	private static List<String> getDBDate(String key){
//		List<String> result = new ArrayList<String>();
//		JdbcTemplate jdbcDao = (JdbcTemplate)ApplicationContextHolder.getBean("jdbcTemplate");
//		String sqlText = null;
//		if(CacheConstant.T_SYS_USER.equals(key)){
//			sqlText = T_SYS_USER_SQL;
//		}else if(CacheConstant.T_ENT_USER.equals(key)){
//			sqlText = T_ENT_USER_SQL;
//		}else if(CacheConstant.T_SYS_GEOGRAPHY.equals(key)){
//			sqlText = T_SYS_GEOGRAPHY_SQL;
//		}else if(CacheConstant.T_SYS_FILE.equals(key)){
//			sqlText = T_SYS_FILE_SQL;
//		}else if(CacheConstant.T_ENT_COMPANY.equals(key)){
//			sqlText = T_ENT_COMPANY_SQL;
//		}
//		if(StringUtils.isNotBlank(sqlText)){
//			List<Map<String, Object>> mapList = jdbcDao.queryForList(sqlText);
//			for(int i =0;i<mapList.size();i++){
//				result.add(JSON.toJSONString(mapList.get(i)));
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 从数据库读取指定KEY对应的Map数据
//	 * @param key
//	 * @return
//	 */
////	public static Map<String, String> get4Map(String key) {
////		ICacheClient client = WebContextHolder.getCacheClient();
////		Map<String, String> cacheMap = client.get(key);
////		if(cacheMap == null || cacheMap.size() == 0){
////			JdbcTemplate jdbcDao = (JdbcTemplate)ApplicationContextHolder.getBean("jdbcTemplate");
////			List<Map<String, Object>> sqlList = jdbcDao.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND CACHE_KEY=?","Map",key);
////			cacheMap = new HashMap<String, String>();
////			if(sqlList.size() == 1){
////				String sqlText = (String)sqlList.get(0).get("SQL_TEXT");
////				if(StringUtils.isNotBlank(sqlText)){
////					List<Map<String, Object>> mapList = jdbcDao.queryForList(sqlText);
////					for (Map<String, Object> map : mapList) {
////						cacheMap.put((String)map.get("KEY_NAME"), (String)map.get("DATA_VALUE"));
////					}
////				}
////			}
////			if(cacheMap != null && !cacheMap.isEmpty())
////				client.set(key, DEFAULT_EXPIRED_TIME, cacheMap);
////		}
////		return cacheMap;
////	}
//	/**
//	 * 从数据库读取指定KEY对应的List数据
//	 * @param clazz 要返回的数据类型,For Example : TsysUser.class
//	 * @param prefixKey 一类缓存的标示，一般配置在CacheConstant类下
//	 * @param key 和prefixKey参数组装成key，在缓存中查询
//	 * @return
//	 */
//	public static <T> List<T> get4List_2_0(Class<T> clazz ,String prefixKey,String key){
//		ICacheClient client = WebContextHolder.getCacheClient();
//		String cacheKey = prefixKey+"_JSON:"+key;
//		String result = client.get_2_0(cacheKey);
//		List<T>  cacheList = null;
//		if(StringUtils.isNotBlank(result)){
//			cacheList = JSON.parseArray(result, clazz);
//		}
//
//		if(cacheList == null){
//			cacheList = getListObjectFromDb(clazz, prefixKey, key);
//			if(cacheList != null && cacheList.size() > 0){
//				client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, JSON.toJSONString(cacheList));
//			}
//		}
//
//		return cacheList == null ? new ArrayList<T>() : cacheList;
//	}
//}
