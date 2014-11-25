package fr.amapj.model.samples.populate;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Cette classe permet de créer des utilisateur dans la base de données
 *
 */
public class InsertDbUtilisateurWithDbTools
{
	
	@DbWrite
	public void createData()
	{
		EntityManager em = TransactionHelper.getEm();
		

		Utilisateur u = new Utilisateur();
		u.setNom("nom_c");
		u.setPrenom("prenom_c");
		u.setEmail("c");
		
		
		em.persist(u);
		
		u = new Utilisateur();
		u.setNom("nom_d");
		u.setPrenom("prenom_d");
		u.setEmail("d");
		
		em.persist(u);
		
		
	}

	public static void main(String[] args)
	{
		TestTools.init();
		
		InsertDbUtilisateurWithDbTools insertDbRole = new InsertDbUtilisateurWithDbTools();
		System.out.println("Debut de l'insertion des données");
		insertDbRole.createData();
		System.out.println("Fin de l'insertion des données");

	}

}
