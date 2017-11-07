package rongji.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;


/**
 * <p>Title:      </p>
 * <p>Description:      </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2013-7-31
 * @version 3.0 
 */
public final class ResourceFinder {

	private static String className = ResourceFinder.class.getName();
	private static String prefix = className.substring(0, className.lastIndexOf(".")).replace(".", "/");
	private static String rootPath;
	
	/**
	 * 加载配置文件
	 * (配置文件所在的文件夹,在系统根目录下)
	 * @param path 文件夹/配置文件名
	 * @return File
	 * @throws IOException 
	 */
	public static File getCongfigFile(String path) throws Exception{
		
		if (path == null) throw new IOException();
		
		if(rootPath==null){
			ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();

            //获取class的根路径
            Resource res = rpr.getResource("classpath:");
            String classRootPath = res.getFile().getPath();
            if (classRootPath != null) {
                classRootPath = classRootPath.replace('\\', '/');
            } else {
                classRootPath = "";
            }
            rootPath = classRootPath + "/";            
		}
		
		if (path != null) {
            //规范资源路径
			path = path.replace('\\', '/');
            if (path.startsWith("/")) {
                //去除开头的"/"
            	path = path.substring(1, path.length());
            }
        } else {
        	path = "";
        }
		
		return new File(rootPath+path);
	}
	
	public static File getCongfig(String path) throws Exception{
		if (path == null) throw new IOException();
		return new PathMatchingResourcePatternResolver().getResource("classpath:"+path).getFile();
	}
	/**
     * 获取资源文件
     * （资源文件在当前类路径下）
     * 
     * @param resourcePath 资源文件名
     * @return File
     * @throws Exception
     */
    public static File getResourceFile(String resourcePath) throws Exception {
    	
    	if (resourcePath == null) throw new IOException();
    	String classRootPath = new PathMatchingResourcePatternResolver().getResource("classpath:").getFile().getPath();
    	 if (classRootPath != null) {
             classRootPath = classRootPath.replace('\\', '/');
         } else {
             classRootPath = "";
         }
    	String path = classRootPath + "/"+ prefix + "/" + resourcePath;
    	
        return new File(path);
    }
    
    /**
     * 获取资源文件,返回Properties
     * (资源文件在src路径下)
     * 
     * @param resource  资源文件名
     * @return Properties
     * @throws Exception
     */
    public static Properties getResourceContent(String resource) throws Exception {
        return PropertiesLoaderUtils.loadProperties(
                new PathMatchingResourcePatternResolver().getResource("classpath:"+resource));
    }
    
    /**
     * 获取资源文件,返回Properties
     * (资源文件所在的文件夹,在系统根目录下)
	 * @param path 文件夹/资源文件名
     * @return
     * @throws Exception
     */
    public static Properties getResource(String path) throws Exception {
    	if (path == null) throw new IOException();
    	if(rootPath==null){
			ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();

            //获取class的根路径
            Resource res = rpr.getResource("classpath:");
            String classRootPath = res.getFile().getPath();
            if (classRootPath != null) {
                classRootPath = classRootPath.replace('\\', '/');
            } else {
                classRootPath = "";
            }
            rootPath = classRootPath + "/";            
		}
		
		if (path != null) {
            //规范资源路径
			path = path.replace('\\', '/');
            if (path.startsWith("/")) {
                //去除开头的"/"
            	path = path.substring(1, path.length());
            }
        } else {
        	path = "";
        }
		
		return PropertiesLoaderUtils.loadProperties(
                new PathMatchingResourcePatternResolver().getResource("classpath:"+rootPath+path));
    }
}
