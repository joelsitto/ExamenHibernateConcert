package hibernateDAO;

import org.hibernate.SessionFactory;

import com.example.model.*;

import java.util.List;

public class CancoDAO extends GenericDAO<Canco> {

	public CancoDAO() {
		super(Canco.class);
	}

	public void afegirInstrumentPerGenere(SessionFactory factory, Genere genere, Instrument instrument) {
		factory.getCurrentSession().beginTransaction();
		List<Canco> cancons = factory.getCurrentSession().createQuery("from Canco where genere=:genere", Canco.class)
				.setParameter("genere", genere).getResultList();
		factory.getCurrentSession().getTransaction().commit();

		for (Canco c : cancons) {
			c.getInstruments().add(instrument);
			instrument.getCancons().add(c);
			factory.getCurrentSession().beginTransaction();
			factory.getCurrentSession().merge(instrument);
			factory.getCurrentSession().getTransaction().commit();
			c.setDurada(c.getDurada() + (instrument.getPopularitat() * 0.5));
			System.out.println("Durada actualitzada per la cançó " + c.getTitol() + ": " + c.getDurada());
			actualitzar(factory, c);
		}
	}
}
