package fr.amapj.view.engine.menu;

import java.util.List;

import com.vaadin.navigator.View;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.model.models.param.EtatModule;
import fr.amapj.service.services.parametres.ParametresDTO;

/**
 * Contient la description d'une entrée menu
 *
 */
public class MenuDescription 
{
	//
	private ModuleList module;
	
	// Role ayant acces à cette entrée de menu
	private RoleList role;
	
	// Nom du menu (element de la liste MenuList)
	private MenuList menuName;
	
	// Nom de la classe implementant la vue liée à ce menu
	private Class<? extends View> viewClass;
	
	private String categorie;
	
	
	
	/**
	 * Dans ce cas, cette entrée est accessible à tous
	 */
	public MenuDescription(MenuList menuName, Class<? extends View> viewClass )
	{
		this(menuName, viewClass, RoleList.ADHERENT, ModuleList.GLOBAL);
	}
	
	public MenuDescription(MenuList menuName, Class<? extends View> viewClass,RoleList role)
	{
		this(menuName, viewClass,role,ModuleList.GLOBAL);
	}
	
	
	
	public MenuDescription(MenuList menuName, Class<? extends View> viewClass,RoleList role,ModuleList module)
	{
		this.module = module;
		this.role =  role;
		this.menuName = menuName;
		this.viewClass = viewClass;
	}



	public MenuList getMenuName()
	{
		return menuName;
	}

	public Class<? extends View> getViewClass()
	{
		return viewClass;
	}
	
	
	public boolean hasRole(List<RoleList> roles)
	{
		return roles.contains(role);
	}
	
	
	/**
	 * Retourne true si cette entrée de menu est activé par son module
	 * @param param
	 * @return
	 */
	public boolean hasModule(ParametresDTO param)
	{
		switch (module)
		{
		case GLOBAL:
			return true;
		
		case PLANNING_DISTRIBUTION:
			return param.etatPlanningDistribution.equals(EtatModule.ACTIF);

		default:
			throw new AmapjRuntimeException("Erreur de programmation");
		}
	}
	
	
	

	public String getCategorie()
	{
		return categorie;
	}

	public MenuDescription setCategorie(String categorie)
	{
		this.categorie = categorie;
		return this;
	}

	
	
	
	
}
