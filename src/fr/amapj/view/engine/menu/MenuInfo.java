package fr.amapj.view.engine.menu;

import java.util.ArrayList;
import java.util.List;

import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.views.compte.MonCompteView;
import fr.amapj.view.views.droits.DroitsAdministrateurBasicForm;
import fr.amapj.view.views.droits.DroitsTresorierBasicForm;
import fr.amapj.view.views.gestioncontrat.listpart.GestionContratListPart;
import fr.amapj.view.views.gestioncontratsignes.GestionContratSignesListPart;
import fr.amapj.view.views.historiquecontrats.HistoriqueContratsView;
import fr.amapj.view.views.historiquepaiements.HistoriquePaiementsView;
import fr.amapj.view.views.importdonnees.ImportDonneesView;
import fr.amapj.view.views.listeadherents.ListeAdherentsView;
import fr.amapj.view.views.listeproducteurreferent.ListeProducteurReferentView;
import fr.amapj.view.views.maintenance.MaintenanceView;
import fr.amapj.view.views.mescontrats.MesContratsView;
import fr.amapj.view.views.meslivraisons.MesLivraisonsView;
import fr.amapj.view.views.mespaiements.MesPaiementsView;
import fr.amapj.view.views.mespermanences.PlanningPermanenceView;
import fr.amapj.view.views.parametres.ParametresView;
import fr.amapj.view.views.producteur.basicform.ProducteurBasicForm;
import fr.amapj.view.views.producteur.contrats.ProducteurContratListPart;
import fr.amapj.view.views.producteur.livraison.ProducteurLivraisonsView;
import fr.amapj.view.views.produit.ProduitBasicForm;
import fr.amapj.view.views.remiseproducteur.RemiseProducteurListPart;
import fr.amapj.view.views.saisiepermanence.SaisiePermanenceListPart;
import fr.amapj.view.views.sendmail.SendMailView;
import fr.amapj.view.views.suiviacces.SuiviAccesView;
import fr.amapj.view.views.utilisateur.UtilisateurListPart;

/**
 * Contient la description de chaque menu
 *
 */
public class MenuInfo 
{
	static private MenuInfo mainInstance;
	
	static public MenuInfo getInstance()
	{
		if (mainInstance==null)
		{
			mainInstance = new MenuInfo();
		}
		return mainInstance;
	}
	
	
	private List<MenuDescription> menus;
	
	private MenuInfo()
	{
		menus = new ArrayList<MenuDescription>();
		
		
		menus.add(new MenuDescription(MenuList.MES_CONTRATS,MesContratsView.class));
		menus.add(new MenuDescription(MenuList.MES_LIVRAISONS,  MesLivraisonsView.class));
		menus.add(new MenuDescription(MenuList.MES_PAIEMENTS,  MesPaiementsView.class));
		menus.add(new MenuDescription(MenuList.MON_COMPTE,  MonCompteView.class));
		menus.add(new MenuDescription(MenuList.LISTE_PRODUCTEUR_REFERENT,  ListeProducteurReferentView.class));
		menus.add(new MenuDescription(MenuList.LISTE_ADHERENTS,  ListeAdherentsView.class));
		menus.add(new MenuDescription(MenuList.PLANNING_DISTRIBUTION,  PlanningPermanenceView.class , RoleList.ADHERENT , ModuleList.PLANNING_DISTRIBUTION));
		
		
		// Partie historique
		menus.add(new MenuDescription(MenuList.HISTORIQUE_CONTRATS, HistoriqueContratsView.class).setCategorie("HISTORIQUE"));
		menus.add(new MenuDescription(MenuList.HISTORIQUE_PAIEMENTS,  HistoriquePaiementsView.class));
		
		// Partie producteurs
		menus.add(new MenuDescription(MenuList.LIVRAISONS_PRODUCTEUR, ProducteurLivraisonsView.class, RoleList.PRODUCTEUR ).setCategorie("PRODUCTEUR"));
		menus.add(new MenuDescription(MenuList.CONTRATS_PRODUCTEUR, ProducteurContratListPart.class , RoleList.PRODUCTEUR));
		
		// Partie référents
		menus.add(new MenuDescription(MenuList.GESTION_CONTRAT, GestionContratListPart.class , RoleList.REFERENT ).setCategorie("REFERENT"));
		menus.add(new MenuDescription(MenuList.GESTION_CONTRAT_SIGNES,  GestionContratSignesListPart.class , RoleList.REFERENT ));
		menus.add(new MenuDescription(MenuList.RECEPTION_CHEQUES, GestionContratSignesListPart.class, RoleList.REFERENT));
		menus.add(new MenuDescription(MenuList.REMISE_PRODUCTEUR, RemiseProducteurListPart.class, RoleList.REFERENT));
		menus.add(new MenuDescription(MenuList.PRODUIT, ProduitBasicForm.class , RoleList.REFERENT));
		menus.add(new MenuDescription(MenuList.SAISIE_PLANNING_DISTRIBUTION, SaisiePermanenceListPart.class , RoleList.REFERENT , ModuleList.PLANNING_DISTRIBUTION ));
		
		
		
		// Partie tésorier
		menus.add(new MenuDescription(MenuList.UTILISATEUR, UtilisateurListPart.class, RoleList.TRESORIER ).setCategorie("TRESORIER"));
		menus.add(new MenuDescription(MenuList.PRODUCTEUR, ProducteurBasicForm.class, RoleList.TRESORIER));
		menus.add(new MenuDescription(MenuList.IMPORT_DONNEES, ImportDonneesView.class, RoleList.TRESORIER));
		menus.add(new MenuDescription(MenuList.LISTE_TRESORIER, DroitsTresorierBasicForm.class, RoleList.TRESORIER));
		
		// Partie adminitrateur
		menus.add(new MenuDescription(MenuList.PARAMETRES, ParametresView.class, RoleList.ADMIN).setCategorie("ADMIN"));
		menus.add(new MenuDescription(MenuList.LISTE_ADMIN, DroitsAdministrateurBasicForm.class, RoleList.ADMIN));
		menus.add(new MenuDescription(MenuList.SUIVI_ACCES, SuiviAccesView.class, RoleList.ADMIN));
		menus.add(new MenuDescription(MenuList.MAINTENANCE, MaintenanceView.class, RoleList.ADMIN));
		menus.add(new MenuDescription(MenuList.ENVOI_MAIL, SendMailView.class, RoleList.ADMIN));
		
			
	}
	
	/**
	 * Retourne la liste des menus accessibles par l'utilisateur courant
	 * 
	 */
	public List<MenuDescription> getMenu()
	{
		ParametresDTO param = new ParametresService().getParametres();
		List<MenuDescription> res = new ArrayList<MenuDescription>();
		List<RoleList> roles = SessionManager.getSessionParameters().userRole;
		
		for (MenuDescription mn : menus)
		{
			if ( mn.hasRole(roles) && mn.hasModule(param) ) 
			{
				res.add(mn);
			}
		}
		return res;
	}
}
