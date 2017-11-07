package rongji.framework.util.tree;

import java.io.Serializable;
import java.util.Collection;
/**
 * 校验节点是否包含子节点
 * @author zhangzhiyi
 *
 */
public abstract class TreeNodeParentAdapter<T extends Serializable> implements TreeNodeValidata<T> {

	@Override
	public Collection<T> getHasCheckedNodes(T[] validataNodes) {
		return null;
	}


}
