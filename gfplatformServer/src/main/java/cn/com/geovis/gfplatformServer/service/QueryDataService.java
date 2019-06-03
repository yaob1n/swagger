package cn.com.geovis.gfplatformServer.service;

import java.util.List;

import cn.com.geovis.gfplatformServer.bean.HighScore;
import cn.com.geovis.gfplatformServer.bean.HighScoreParent;

public interface QueryDataService {
	
    HighScore getByClassification(String classification);
    
    //名称模糊查询    暂时不用
    List<HighScoreParent> findByName(String name);
    
    //根据分类与名称迷糊匹配数据
    List<HighScore> findByNameAndClassifcation(String classification,String name);
    
    //根据json文件生成数据到数据库
    Boolean insertFile(String classification,String AreaLayers,String AreaPanel);
    
    //判断此分类是否存在
    Boolean isExist(String classification);
    
    //根据execl导入数据到数据库
    Boolean insertExeclFile(String execlPath,String classification);

	List<HighScore> findByNameAndClassifcation1(String name, String classification);
	
	int updateById(String layers, String name, String tuliUrl,Integer id);

	boolean idIsExist(Integer id);

	boolean idIsExist1(Integer id);

	int updateById1(String layers, String description, String url, Integer id);

}
