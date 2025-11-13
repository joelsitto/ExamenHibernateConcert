package hibernateDAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.example.model.Artista;
import com.example.model.Escenari;

public class ArtistaDAO extends GenericDAO<Artista> {

	public ArtistaDAO() {
		super(Artista.class);
	}

	public void assignarArtistaAMaxEscenaris(Artista artista, SessionFactory factory) {

        //Artista artista = obtenirPerId(factory, idArtista);

		if (artista.getConcertsDisponibles() == 0) {
			System.out.println("L'artista no té concerts disponibles");
			return;
		}
		Session session = factory.getCurrentSession();
		session.beginTransaction();


		List<Escenari> escenarisDisponibles = session.createQuery("from Escenari where artista is null", Escenari.class).getResultList();

		session.getTransaction().commit();

		session = factory.openSession();
		for (Escenari escenari : escenarisDisponibles) {
			if (artista.getConcertsDisponibles() > 0) {
				escenari.setArtista(artista);
				artista.getEscenaris().add(escenari);
				session.beginTransaction();
				session.merge(escenari);
				session.getTransaction().commit();
				artista.setConcertsDisponibles(artista.getConcertsDisponibles() - 1);
				System.out.println("Assignat artista a escenari: " + escenari.getNom());
			} else {
				System.out.println("L'artista no pot més concerts.");
				break;
			}
		}

		actualitzar(factory, artista);

	}

	public void distribucioEscenaris(SessionFactory factory) {
		Session session = factory.openSession();
		try {
			session.beginTransaction();

			List<Artista> artistes = session.createQuery("FROM Artista", Artista.class).getResultList();
			List<Escenari> escenaris = session.createQuery("FROM Escenari", Escenari.class).getResultList();

			if (artistes.isEmpty() || escenaris.isEmpty()) {
				System.out.println("No hi ha artistes o escenaris disponibles");
				return;
			}

			List<Escenari> principals = escenaris.stream().filter(e -> e.getTipus().name().equals("PRINCIPAL"))
					.toList();
			List<Escenari> secundaris = escenaris.stream().filter(e -> e.getTipus().name().equals("SECUNDARI"))
					.toList();
			List<Escenari> tematics = escenaris.stream().filter(e -> e.getTipus().name().equals("TEMATIC")).toList();

			int totalEscenaris = escenaris.size();
			int totalArtistes = artistes.size();

			int artistesPrincipal = (int) Math.round((double) principals.size() / totalEscenaris * totalArtistes);
			int artistesSecundari = artistesPrincipal
					+ (int) Math.round((double) secundaris.size() / totalEscenaris * totalArtistes);

			int indexArtista = 0;

			// PRINCIPALS
			for (Escenari e : principals) {
				if (indexArtista < artistesPrincipal && indexArtista < artistes.size()) {
					e.setArtista(artistes.get(indexArtista));
					session.merge(e);
					indexArtista++;
				}
			}

			// SECUNDARIS
			for (Escenari e : secundaris) {
				if (indexArtista < artistesPrincipal + artistesSecundari && indexArtista < artistes.size()) {
					e.setArtista(artistes.get(indexArtista));
					session.merge(e);
					indexArtista++;
				}
			}

			// TEMATICS
			for (Escenari e : tematics) {
				if (indexArtista < artistes.size()) {
					e.setArtista(artistes.get(indexArtista));
					session.merge(e);
					indexArtista++;
				}
			}

			session.getTransaction().commit();

			System.out.println("\n=== DISTRIBUCIÓ D'ESCENARIS ===");
			for (Escenari e : escenaris) {
				System.out.println("Escenari: " + e.getNom() + " (" + e.getTipus() + "): " + e.getArtista());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
