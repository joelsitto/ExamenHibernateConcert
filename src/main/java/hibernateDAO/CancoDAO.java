package hibernateDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.example.model.*;

import java.util.List;

public class CancoDAO extends GenericDAO<Canco> {

	public CancoDAO() {
		super(Canco.class);
	}

	public void afegirInstrumentPerGenere(SessionFactory factory, Genere genere, Instrument instrument) {
		factory.getCurrentSession().beginTransaction();
        Session session = factory.getCurrentSession();


		List<Canco> cancons = session.createQuery("from Canco where genere = :genere", Canco.class).setParameter("genere", genere).getResultList();
		session.getTransaction().commit();

		for (Canco canco : cancons) {
			canco.getInstruments().add(instrument);
			instrument.getCancons().add(canco);
            session.beginTransaction();
            session.merge(instrument);
            session.getTransaction().commit();
            canco.setDurada(canco.getDurada() + (instrument.getPopularitat() * 0.5));
			System.out.println("Durada actualitzada per la cançó " + canco.getTitol() + ": " + canco.getDurada());
			actualitzar(factory, canco);
		}
	}
}
