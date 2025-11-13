package hibernateDAO;

import org.hibernate.SessionFactory;
import java.util.List;

public interface IGenericDAO<T> {

	void guardar(SessionFactory sessionFactory, T entity);

	void actualitzar(SessionFactory sessionFactory, T entity);

	void eliminar(SessionFactory sessionFactory, Long id);

	T obtenirPerId(SessionFactory sessionFactory, Long id);

	List<T> obtenirTots(SessionFactory sessionFactory);
}
