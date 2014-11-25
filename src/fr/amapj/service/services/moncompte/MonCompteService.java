package fr.amapj.service.services.moncompte;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;

public class MonCompteService
{
	private final static Logger logger = Logger.getLogger(MonCompteService.class.getName());

	public MonCompteService()
	{

	}

	/**
	 * Permet de récuperer les infos de l'utilisateur courant
	 */
	@DbRead
	public UtilisateurDTO getUtilisateurInfo()
	{
		EntityManager em = TransactionHelper.getEm();
	
		Long id = SessionManager.getUserId();
		Utilisateur u = em.find(Utilisateur.class, id);
		UtilisateurDTO dto = new UtilisateurService().createUtilisateurDto(em, u);
		return dto;

	}

	/**
	 * Permet de changer le password
	 * 
	 * Retourne true si le mot de passe a pu être changé, 
	 * false sinon 
	 */
	@DbWrite
	public boolean setNewEmail(final Long userId, final String newEmail)
	{
		 EntityManager em = TransactionHelper.getEm();
	
		Utilisateur r = em.find(Utilisateur.class, userId);
		if (r == null)
		{
			logger.severe("Impossible de retrouver l'utilisateur avec l'id " + userId);
			return false;
		}

		r.setEmail(newEmail);
		return true;

	}

	// PARTIE MISE A JOUR DES COORDONNEES
	
	@DbWrite
	public void updateCoordoonees(final UtilisateurDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur u = em.find(Utilisateur.class, dto.id);

		u.setNumTel1(dto.numTel1);
		u.setNumTel2(dto.numTel2);
		u.setLibAdr1(dto.libAdr1);
		u.setCodePostal(dto.codePostal);
		u.setVille(dto.ville);
	}

}
