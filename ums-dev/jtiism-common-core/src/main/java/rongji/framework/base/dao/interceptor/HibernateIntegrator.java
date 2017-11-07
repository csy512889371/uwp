package rongji.framework.base.dao.interceptor;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class HibernateIntegrator {

/*	@Autowired
	private CrudListener crudListener;*/

	@Autowired
	private SessionFactory sessionFactory;

	@PostConstruct
	public void registerListeners() {
/*		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(crudListener);
		registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(crudListener);
		registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(crudListener);*/
	}

}
