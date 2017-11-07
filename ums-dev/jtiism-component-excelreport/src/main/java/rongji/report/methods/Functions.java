package rongji.report.methods;

import java.util.Collection;
import java.util.List;

import rongji.report.model.ReportContext;

public abstract class Functions {

	protected ReportContext reportContext = null;

	public Functions() {
	}

	public abstract void setReportContext(ReportContext reportContext);

	protected Boolean isInstance(Object obj, Class<?> class1) {
		if (obj == null) {
			return true;
		}
		return class1.isInstance(obj);
	}

	@SuppressWarnings("unchecked")
	protected Boolean isInstance(Object obj, Class<?> class1, Class<?> class2) {
		if (class1.isInstance(obj)) {
			try {
				if (List.class.equals(class1)) {
					Collection<Object> objs = (Collection<Object>) obj;
					if (objs.isEmpty()) {
						return true;
					}
					for (Object newObj : objs) {
						return class2.isInstance(newObj);
					}
				}
			} catch (Exception e) {
			}
		}

		return false;
	}

}
