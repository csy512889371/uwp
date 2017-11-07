package rongji.framework.util.tree;

import java.io.Serializable;
import java.util.Collection;

public interface TreeNodeValidata<T extends Serializable> {

	Collection<T> getHasChildrenNodes(T[] validataNodes);
	Collection<T> getHasCheckedNodes(T[] validataNodes);
}
