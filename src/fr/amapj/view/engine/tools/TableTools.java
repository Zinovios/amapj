package fr.amapj.view.engine.tools;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

/**
 * Outil pour la gestion des tables
 * 
 *
 */
public class TableTools
{
	
	static public boolean updateTable(Table cdesTable,List<? extends TableItem> res,String[] sortColumns,boolean[] sortAscending)
	{
		boolean oneLineSelected = false;
		
		BeanItemContainer mcInfos = (BeanItemContainer) cdesTable.getContainerDataSource();
		
		
		// Conservation de la valeur actuellement sélectionnée (si il y en a une)
		TableItem current = (TableItem) cdesTable.getValue();
		
		// Affacement de tous les elements
		mcInfos.removeAllItems();
				
		// Chargement des elements
		mcInfos.addAll(res);
				
		// Tris 
		mcInfos.sort(sortColumns,sortAscending);
				
		// Gestion de la selection
		if (current!=null)
		{
			boolean done = false;
			for (TableItem dto : res)
			{
				if (dto.getId().equals(current.getId()))
				{
					cdesTable.setValue(dto);
					done = true;
				}		
			}
			return done;
		}
		else
		{
			return false;
		}
		
	}
}

	

