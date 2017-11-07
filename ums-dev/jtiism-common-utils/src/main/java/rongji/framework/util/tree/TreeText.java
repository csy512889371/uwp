package rongji.framework.util.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 标注该属性表示该值为一个tree节点的text即显示文本 
 * @author zhangzhiyi copyright by RJ soft.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TreeText
{

}
