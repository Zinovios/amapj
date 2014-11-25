package fr.amapj.view.engine.grid.integergrid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.view.engine.grid.ErreurSaisieException;
import fr.amapj.view.engine.grid.GridHeaderLine;
import fr.amapj.view.engine.grid.GridIJData;
import fr.amapj.view.engine.grid.ShortCutManager;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.tools.BaseUiTools;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;

/**
 * Popup pour la saisie des quantites 
 *  
 */
@SuppressWarnings("serial")
abstract public class PopupIntegerGrid extends CorePopup
{

	private Table table;

	private Label prixTotal;

	protected IntegerGridParam param = new IntegerGridParam();

	private ShortCutManager shortCutManager;

	/**
	 * 
	 */
	public PopupIntegerGrid()
	{

	}

	abstract public void loadParam();

	/**
	 * Retourne true si il faut fermer le popup
	 * @return
	 */
	abstract public boolean performSauvegarder();

	protected void createContent(VerticalLayout mainLayout)
	{
		loadParam();
		param.initialize();

		if (param.messageSpecifique != null)
		{
			mainLayout.addComponent(new Label(param.messageSpecifique, ContentMode.HTML));
		}

		// Construction des headers
		for (GridHeaderLine line : param.headerLines)
		{
			constructHeaderLine(mainLayout, line);
		}

		// Construction de la table de saisie
		table = new Table();
		table.addStyleName("big");

		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);

		// Colonne de gauche contenant un libellé
		table.addContainerProperty(new Integer(-1), Label.class, null);
		table.setColumnWidth(new Integer(-1), param.largeurCol - 10);

		// Les autres colonnes correspondant à la saisie des quantites
		for (int i = 0; i < param.nbCol; i++)
		{
			Class clzz;
			if (param.readOnly)
			{
				clzz = Label.class;
			}
			else
			{
				clzz = TextField.class;
			}
			table.addContainerProperty(new Integer(i), clzz, null);
			table.setColumnWidth(new Integer(i), param.largeurCol - 10);
		}

		//
		if (param.readOnly==false)
		{
			shortCutManager = new ShortCutManager(param.nbLig, param.nbCol, param.excluded);
			shortCutManager.addShorcut(this);
		}

		// Creation de toutes les cellules pour la saisie
		for (int i = 0; i < param.nbLig; i++)
		{
			addRow(i);
		}

		if (param.readOnly)
		{
			table.setEditable(false);
		}
		else
		{
			table.setEditable(true);
		}
		table.setSelectable(true);
		table.setSortEnabled(false);
		table.setPageLength(getPageLength());

		// Footer 0 pour avoir un espace
		HorizontalLayout footer0 = new HorizontalLayout();
		footer0.setWidth("200px");
		footer0.setHeight("20px");

		// Footer 1 avec le prix total
		HorizontalLayout footer1 = new HorizontalLayout();
		footer1.setWidth("200px");

		Label dateLabel = new Label("Prix Total");
		dateLabel.addStyleName("prix");
		dateLabel.setSizeFull();
		footer1.addComponent(dateLabel);
		footer1.setExpandRatio(dateLabel, 1.0f);

		prixTotal = new Label("");
		displayMontantTotal();
		prixTotal.addStyleName("prix");
		prixTotal.setSizeFull();
		footer1.addComponent(prixTotal);
		footer1.setExpandRatio(prixTotal, 1.0f);
		
		// Construction globale
		mainLayout.addComponent(table);
		mainLayout.addComponent(footer0);
		mainLayout.addComponent(footer1);

		mainLayout.setMargin(true);
		
	}
		
	protected void createButtonBar()
	{
		if (param.readOnly)
		{
			Button ok = addDefaultButton("Continuer ...", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleContinuer();
				}
			});
		}
		else
		{
			if (param.nbLig > 1)
			{
				Button copierButton = addButton("Copier la 1ère ligne partout", new Button.ClickListener()
				{
					@Override
					public void buttonClick(ClickEvent event)
					{
						handleCopier();
					}
				});
				popupButtonBarLayout.setComponentAlignment(copierButton, Alignment.TOP_LEFT);
			}

			Button saveButton = addDefaultButton("Continuer ...", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleSauvegarder();
				}
			});
		

			Button cancelButton = addButton("Annuler", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleAnnuler();
				}
			});
		}
	}

	protected void handleCopier()
	{
		try
		{
			doHandleCopier();
		}
		catch (ErreurSaisieException e)
		{
			Notification.show("Erreur de saisie sur la premiere ligne - Impossible de copier");
		}
	}

	private void doHandleCopier() throws ErreurSaisieException
	{
		Item item = table.getItem(new Integer(0));

		for (int j = 0; j < param.nbCol; j++)
		{
			if (isExcluded(0, j) == false)
			{
				// Lecture de la valeur dans la case tout en haut
				TextField tf = (TextField) item.getItemProperty(new Integer(j)).getValue();
				int qteRef = readValueInCell(tf);

				// Copie de cette valeur dans toutes les cases en dessous
				for (int i = 0; i < param.nbLig; i++)
				{
					if (isExcluded(i, j) == false)
					{
						Item item1 = table.getItem(new Integer(i));
						TextField tf1 = (TextField) item1.getItemProperty(new Integer(j)).getValue();
						tf1.setConvertedValue(qteRef);
					}
				}
			}
		}
	}

	private void constructHeaderLine(VerticalLayout mainLayout, GridHeaderLine line)
	{
		HorizontalLayout header1 = new HorizontalLayout();
		header1.setWidth(getLargeurTotal());
		if (line.height != -1)
		{
			header1.setHeight(line.height + "px");
		}

		for (String str : line.cells)
		{
			Label dateLabel = new Label(str);
			if (line.styleName != null)
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
		if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() < 9)
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
		return (param.nbCol + 1) * (param.largeurCol + param.espaceInterCol) + "px";
	}

	private void displayMontantTotal()
	{
		prixTotal.setValue(new CurrencyTextFieldConverter().convertToString(param.montantTotal));
	}

	private void addRow(int lig)
	{
		List<Object> cells = new ArrayList<Object>();

		Label dateLabel = new Label(param.leftPartLine.get(lig));
		dateLabel.addStyleName("big");
		dateLabel.addStyleName("align-center");
		dateLabel.setWidth(param.largeurCol + "px");

		cells.add(dateLabel);
		for (int j = 0; j < param.nbCol; j++)
		{
			int qte = param.qte[lig][j];
			boolean isExcluded = isExcluded(lig, j);

			// En lecture simple
			if (param.readOnly)
			{
				//
				String txt;

				if (isExcluded)
				{
					txt = "XXXXXX";
				}
				else if (qte == 0)
				{
					txt = "";
				}
				else
				{
					txt = "" + qte;
				}
				Label tf = new Label(txt);
				tf.addStyleName("align-center");
				tf.addStyleName("big");
				tf.setWidth((param.largeurCol - 10) + "px");
				cells.add(tf);
			}
			// En mode normal
			else 
			{
				// Si la cellule est exclue
				if (isExcluded)
				{
					TextField tf = new TextField();
					tf.setValue("XXXXXX");
					tf.setEnabled(false);
					tf.addStyleName("align-center");
					tf.addStyleName("big");
					tf.setWidth((param.largeurCol - 10) + "px");
					cells.add(tf);
				}
				else
				{
					//
					final TextField tf = BaseUiTools.createQteField("");
					tf.setData(new GridIJData(lig, j));
					if (qte == 0)
					{
						tf.setConvertedValue(null);
					}
					else
					{
						tf.setConvertedValue(new Integer(qte));
					}
					tf.addValueChangeListener(new Property.ValueChangeListener()
					{
						@Override
						public void valueChange(ValueChangeEvent event)
						{
							try
							{
								GridIJData ij = (GridIJData) tf.getData();
								int qte = readValueInCell(tf);
								param.updateQte(ij.i(), ij.j(), qte);
								displayMontantTotal();
							}
							catch (ErreurSaisieException e)
							{
								// TODO pas si simple
								// tf.setStyleName("erreurqte");
								Notification.show("Erreur de saisie");
							}
						}
					});
	
					tf.addStyleName("align-center");
					tf.addStyleName("big");
					tf.setWidth((param.largeurCol - 10) + "px");
					shortCutManager.registerTextField(tf);
					cells.add(tf);
				}
			}
		}

		table.addItem(cells.toArray(), new Integer(lig));

	}

	/**
	 * Indique si cette cellule est exclue de la saisie
	 * @param lig
	 * @param col
	 * @return
	 */
	private boolean isExcluded(int lig, int col)
	{
		if (param.excluded == null)
		{
			return false;
		}
		return param.excluded[lig][col];
	}

	/**
	 * Retourne la valeur dans la cellule sous la forme d'un entier
	 * jette une exception si il y a une erreur
	 */
	private int readValueInCell(TextField tf) throws ErreurSaisieException
	{
		try
		{
			Integer val = (Integer) tf.getConvertedValue();
			int qte = 0;
			if (val != null)
			{
				qte = val.intValue();
			}
			return qte;
		}
		catch (ConversionException e)
		{
			throw new ErreurSaisieException();
		}
	}

	
	protected void handleAnnuler()
	{
		close();
	}
	
	
	protected void handleContinuer()
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
			return;
		}

		boolean ret = performSauvegarder();
		if (ret==true)
		{
			close();
		}
	}

	/**
	 * Lecture de la table pour mettre à jour contratDTO
	 * @return
	 */
	private void updateModele() throws ErreurSaisieException
	{
		for (int i = 0; i < param.nbLig; i++)
		{
			Item item = table.getItem(new Integer(i));

			for (int j = 0; j < param.nbCol; j++)
			{
				if (isExcluded(i, j)==false)
				{
					TextField tf = (TextField) item.getItemProperty(new Integer(j)).getValue();
					int qte = readValueInCell(tf);
					param.qte[i][j] = qte;
				}
			}
		}
	}

}
