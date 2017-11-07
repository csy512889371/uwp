package rongji.framework.util.tree;

import java.io.Serializable;
import java.util.Collection;
/**
 * 校验节点是否被选中
 * @author zhangzhiyi
 *
 */
public abstract class TreeNodeCheckedAdapter<T extends Serializable> implements TreeNodeValidata<T> {

	@Override
	public Collection<T> getHasChildrenNodes(T[] validataNodes) {
		return null;
	}
}
