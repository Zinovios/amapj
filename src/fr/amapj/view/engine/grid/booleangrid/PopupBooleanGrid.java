package fr.amapj.view.engine.grid.booleangrid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.view.engine.grid.ErreurSaisieException;
import fr.amapj.view.engine.grid.GridHeaderLine;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup pour la saisie des quantites 
 *  
 */
@SuppressWarnings("serial")
abstract public class PopupBooleanGrid extends CorePopup
{
	
	protected Table table;
	
	protected Button saveButton;
	protected String saveButtonTitle = "Sauvegarder";
	protected Button cancelButton;
	protected String cancelButtonTitle = "Annuler";
	
	protected BooleanGridParam param = new BooleanGridParam();
	
	/**
	 * 
	 */
	public PopupBooleanGrid()
	{
				
	}
	
	abstract public void loadParam();
	

	abstract public void performSauvegarder();
	
		
	
	protected void createContent(VerticalLayout mainLayout)
	{
		loadParam();
		
		if (param.messageSpecifique!=null)
		{
			mainLayout.addComponent(new Label(param.messageSpecifique,ContentMode.HTML));
		}
		
		// Construction des headers
		for (GridHeaderLine line : param.headerLines)
		{
			constructHeaderLine(mainLayout,line);
		}
		
		// Construction de la table de saisie 
		table = new Table();
		table.addStyleName("big");
		
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		
		// Colonne de gauche contenant un libellé
		table.addContainerProperty(new Integer(-1), Label.class, null);
		table.setColumnWidth(new Integer(-1), param.largeurCol-10);
		
		if (param.leftPartLine2!=null)
		{
			table.addContainerProperty(new Integer(-2), Label.class, null);
			table.setColumnWidth(new Integer(-2), param.largeurCol-10);
		}
		
		
		
		// Les autres colonnes correspondant à la saisie des boolean
		for (int i = 0; i < param.nbCol; i++)
		{
			table.addContainerProperty(new Integer(i), CheckBox.class, null);
			table.setColumnWidth(new Integer(i), param.largeurCol-10);
		}
		
		
		// Creation de toutes les cellules pour la saisie
		for (int i = 0; i < param.nbLig; i++)
		{
			addRow(i);
		}

		
		table.setEditable(true);

		table.setSelectable(true);
		table.setSortEnabled(false);
		table.setPageLength(getPageLength());
		
		mainLayout.addComponent(table);

	}
	
	
	protected void createButtonBar()
	{		
		saveButton = addDefaultButton(saveButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSauvegarder();
			}
		});
				
		
		cancelButton = addButton(cancelButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
	}
	

	
	private void constructHeaderLine(VerticalLayout mainLayout, GridHeaderLine line)
	{
		HorizontalLayout header1 = new HorizontalLayout();
		header1.setWidth(getLargeurTotal());
		if (line.height!=-1)
		{
			header1.setHeight(line.height+"px");
		}
		
		for (String str : line.cells)
		{
			Label dateLabel = new Label(str);
			if (line.styleName!=null)
			{
				dateLabel.addStyleName(line.styleName);
			}
			header1.addComponent(dateLabel);
			dateLabel.setSizeFull();
			header1.setExpandRatio(dateLabel, 1.0f);	
		}
		mainLayout.addComponent(header1);
	}



	private int getPageLength()
	{
		WebBrowser webBrowser = UI.getCurrent().getPage().getWebBrowser();
		int pageLength = 15;
		
		// Pour ie 8 et inférieur : on se limite a 6 lignes, sinon ca rame trop
		if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion()<9)
		{
			pageLength = 6;
		}
		
		pageLength = Math.min(pageLength, param.nbLig);
		return pageLength;
	}



	/**
	 * Calcul de la largeur totale de la table
	 * @return
	 */
	private String getLargeurTotal()
	{
		// La colonne additionnelle
		int addCol = 0;
		if (param.leftPartLine2!=null)
		{
			addCol++;
		}
		
		return (param.nbCol+1+addCol)*(param.largeurCol+param.espaceInterCol)+"px";
	}

	private void addRow(int lig)
	{
		List<Object> cells = new ArrayList<Object>();
		
		
		
		Label dateLabel = new Label(param.leftPartLine.get(lig));
		dateLabel.addStyleName("big");
		dateLabel.addStyleName("align-center");
		dateLabel.setWidth(param.largeurCol+"px");
		cells.add(dateLabel);
		
		if (param.leftPartLine2!=null)
		{
			dateLabel = new Label(param.leftPartLine2.get(lig));
			dateLabel.addStyleName("big");
			dateLabel.addStyleName("align-center");
			dateLabel.setWidth(param.largeurCol+"px");
			cells.add(dateLabel);
		}
		
		
		
		for (int j = 0; j < param.nbCol; j++)
		{
			boolean box = param.box[lig][j];
			
			CheckBox checkbox = new CheckBox();
			checkbox.setValue(box);
			checkbox.addStyleName("align-center");
			checkbox.addStyleName("big");
			checkbox.setWidth((param.largeurCol-10)+"px");
			cells.add(checkbox);
		}
		
		table.addItem(cells.toArray(), new Integer(lig));
		
	}


	/**
	 * Retourne la valeur dans la cellule sous la forme d'un boolean
	 * jette une exception si il y a une erreur
	 */
	private boolean readValueInCell(CheckBox tf)
	{
		return tf.getValue().booleanValue();
	}


	protected void handleAnnuler()
	{
		close();
	}

	protected void handleSauvegarder()
	{
		try
		{
			updateModele();
		}
		catch (ErreurSaisieException e)
		{
			Notification.show("Erreur de saisie");
			return ;
		}
		
		performSauvegarder();
		
		close();
	}
	
	
	/**
	 * Lecture de la table pour mettre à jour le modele
	 * @return
	 */
	private void updateModele() throws ErreurSaisieException
	{	
		for (int i = 0; i < param.nbLig; i++)
		{
			Item item = table.getItem(new Integer(i));
			
			for (int j = 0; j < param.nbCol; j++)
			{
				CheckBox tf = (CheckBox) item.getItemProperty(new Integer(j)).getValue();
				boolean val = readValueInCell(tf);
				param.box[i][j] = val;
			}
		}
	}

}
