package rongji.framework.util.tree;

import java.io.Serializable;
import java.util.Collection;
/**
 * 空实现
 * @author zhangzhiyi
 *
 */
public abstract class TreeNodeEmptyAdapter<T extends Serializable> implements TreeNodeValidata<T> {

	@Override
	public Collection<T> getHasChildrenNodes(T[] validataNodes) {
		return null;
	}

	@Override
	public Collection<T> getHasCheckedNodes(T[] validataNodes) {
		return null;
	}



}
