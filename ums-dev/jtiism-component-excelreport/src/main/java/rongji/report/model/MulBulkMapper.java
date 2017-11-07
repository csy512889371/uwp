package rongji.report.model;

import java.util.List;

public abstract class MulBulkMapper {



	public Boolean isInstance(Object obj, Class<?> class1) {
		return class1.isInstance(obj);
	}

	@SuppressWarnings("unchecked")
	public Boolean isInstance(Object obj, Class<?> class1, Class<?> class2) {
		if (class1.isInstance(obj)) {
			try {
				if (List.class.equals(class1)) {
					List<Object> objs = (List<Object>) obj;
					if (objs.isEmpty()) {
						return true;
					}
					return class2.isInstance(objs.get(0));
				}
			} catch (Exception e) {

			}
		}

		return false;
	}

}
