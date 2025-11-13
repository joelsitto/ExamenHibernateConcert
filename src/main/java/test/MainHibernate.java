package test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.model.*;

import hibernateDAO.*;

public class MainHibernate {
	public static void main(String[] args) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = factory.openSession();

		try {
			/* CREAR DADES DE PROVA */
			Escenari esc1 = new Escenari("Main Stage", TipusEscenari.PRINCIPAL);
			Escenari esc2 = new Escenari("Escenari Secundari", TipusEscenari.SECUNDARI);
			Escenari esc3 = new Escenari("Escenari Temàtic", TipusEscenari.TEMATIC);
			Escenari esc4 = new Escenari("Escenari Temàtic", TipusEscenari.PRINCIPAL);
			Instrument guitarra = new Instrument("Guitarra", 8.5);

			session.beginTransaction();
			session.persist(esc1);
			session.persist(esc2);
			session.persist(esc3);
			session.persist(esc4);
			session.persist(guitarra);
			session.getTransaction().commit();

			/* CREAR DAO */
			ArtistaDAO artistaDAO = new ArtistaDAO();
			CancoDAO cancoDAO = new CancoDAO();

			Canco canco = new Canco("Por una patata", Genere.POP, 4.5);
			Canco canco2 = new Canco("El dia que todos los alumnos de DAM sacaron un 10", Genere.ELECTRONIC, 2.32);
			cancoDAO.guardar(factory, canco);

			List<Genere> generes1 = new ArrayList<>();
			generes1.add(Genere.POP);
			generes1.add(Genere.ELECTRONIC);
			Artista artista = new Artista("Rosalia", 500.0, generes1);
			artista.setConcertsDisponibles(10);
			artista.getCancons().add(canco);
			artista.getCancons().add(canco2);
			canco.setArtista(artista);
			canco2.setArtista(artista);

			artistaDAO.guardar(factory, artista);
			cancoDAO.actualitzar(factory, canco);
			cancoDAO.actualitzar(factory, canco2);

			// ASSIGNAR ARTISTA
			artistaDAO.assignarArtistaAMaxEscenaris(artista, factory);

			// AFEGIR INSTRUMENT PER GENERE

			cancoDAO.afegirInstrumentPerGenere(factory, Genere.POP, guitarra);

			// DISTRIBUCIÓ ESCENARIS
			List<Genere> generes2 = new ArrayList<>();
			generes2.add(Genere.ELECTRONIC);
			generes2.add(Genere.JAZZ);

			Artista artista2 = new Artista("Totakeke", 1000.0, generes2);
			artista2.setConcertsDisponibles(3);
			artistaDAO.guardar(factory, artista2);
			Artista artista3 = new Artista("Sona", 30.0, generes2);
			artista2.setConcertsDisponibles(3);
			artistaDAO.guardar(factory, artista3);
			Artista artista4 = new Artista("Hatsune Miku", 750.0, generes2);
			artista2.setConcertsDisponibles(3);
			artistaDAO.guardar(factory, artista4);
			Artista artista5 = new Artista("Los DAMs", 70.0, generes1);
			artista2.setConcertsDisponibles(3);
			artistaDAO.guardar(factory, artista5);
			
			artistaDAO.distribucioEscenaris(factory);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			factory.close();
		}
	}
}
