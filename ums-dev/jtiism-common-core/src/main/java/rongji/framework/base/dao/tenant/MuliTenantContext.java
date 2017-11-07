package rongji.framework.base.dao.tenant;

public class MuliTenantContext {

	private String tenantInfo;

	private static ThreadLocal<?> threadLocal = new ThreadLocal<Object>() {
		@Override
		protected MuliTenantContext initialValue() {
			return new MuliTenantContext();
		}
	};

	public static MuliTenantContext get() {
		return (MuliTenantContext) threadLocal.get();
	}

	public String getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(String tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

}
