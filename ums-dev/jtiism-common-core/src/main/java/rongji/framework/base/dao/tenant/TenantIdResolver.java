package rongji.framework.base.dao.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		return MuliTenantContext.get().getTenantInfo();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
