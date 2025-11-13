package hibernateDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class GenericDAO<T> implements IGenericDAO<T> {

	private Class<T> entityClass;

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public void guardar(SessionFactory sessionFactory, T entity) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			session.persist(entity);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void actualitzar(SessionFactory sessionFactory, T entity) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			session.merge(entity);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void eliminar(SessionFactory sessionFactory, Long id) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			T entity = session.get(entityClass, id);
			if (entity != null) {
				session.remove(entity);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public T obtenirPerId(SessionFactory sessionFactory, Long id) {
		try (Session session = sessionFactory.openSession()) {
			return session.get(entityClass, id);
		}
	}

	@Override
	public List<T> obtenirTots(SessionFactory sessionFactory) {
		try (Session session = sessionFactory.openSession()) {
			return session.createQuery("FROM " + entityClass.getName(), entityClass).list();
		}
	}
}
