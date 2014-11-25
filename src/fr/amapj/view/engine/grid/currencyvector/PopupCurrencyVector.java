package fr.amapj.view.engine.grid.currencyvector;

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
 * TODO : il y a un souci si la dernire ligne est exclue
 *  
 */
@SuppressWarnings("serial")
abstract public class PopupCurrencyVector extends CorePopup
{

	private Table table;


	protected CurrencyVectorParam param = new CurrencyVectorParam();

	private ShortCutManager shortCutManager;
	
	private TextField lastLineTextField;
	
	private Label montantTotalPaiement;
	
	private String message = 	"Une proposition de paiement a été calculée et est affichée ci dessous.<br/>"+
								"Vous pouvez modifier cette proposition en saisissant directement les montants en face de chaque mois<br/>"+
								"Le dernier mois est calculé automatiquement pour ajuster le contrat<br/><br/>";

	/**
	 * 
	 */
	public PopupCurrencyVector()
	{

	}

	abstract public void loadParam();

	abstract public boolean performSauvegarder();

	protected void createContent(VerticalLayout mainLayout)
	{
		loadParam();

		if (param.messageSpecifique != null)
		{
			mainLayout.addComponent(new Label(param.messageSpecifique, ContentMode.HTML));
		}
		
		if ((param.readOnly==false) && (param.computeLastLine==true))
		{
			mainLayout.addComponent(new Label(message, ContentMode.HTML));
		}
		
		if (param.avoirInitial!=0)
		{
			// Footer 1 avec le montant total des paiements
			HorizontalLayout footer1 = new HorizontalLayout();
			footer1.setWidth("410px");
			fillFooter(footer1,"Avoir initial",param.avoirInitial);
			
			// Footer 2 pour avoir un espace
			HorizontalLayout footer2 = new HorizontalLayout();
			footer2.setWidth("200px");
			footer2.setHeight("20px");

			mainLayout.addComponent(footer1);
			mainLayout.addComponent(footer2);
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

		// colonne de droite correspondant à la saisie des quantites
		Class clzz;
		if (param.readOnly)
		{
			clzz = Label.class;
		}
		else
		{
			clzz = TextField.class;
		}
		table.addContainerProperty(new Integer(0), clzz, null);
		table.setColumnWidth(new Integer(0), param.largeurCol - 10);

		// Convertion du vecteur en un tableau
		boolean[][] excluded = new boolean[param.excluded.length][1];
		for (int i = 0; i < param.excluded.length; i++)
		{
			excluded[i][0] = param.excluded[i];
		}

		if (param.readOnly==false)
		{
			shortCutManager = new ShortCutManager(param.nbLig, 1, excluded);
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

		

		// Construction globale
		mainLayout.addComponent(table);
		mainLayout.addComponent(footer0);
		
	
		if ( (param.readOnly==true) || (param.computeLastLine==false) )
		{
			// Footer 1 avec le montant total des paiements
			HorizontalLayout footer1 = new HorizontalLayout();
			footer1.setWidth("410px");
			montantTotalPaiement = fillFooter(footer1,"Montant total des paiements",getMontantTotalPaiement());
			
			// Footer 2 pour avoir un espace
			HorizontalLayout footer2 = new HorizontalLayout();
			footer2.setWidth("200px");
			footer2.setHeight("20px");
			
			// Footer 3 avec le prix total du contrat
			HorizontalLayout footer3 = new HorizontalLayout();
			footer3.setWidth("410px");
			fillFooter(footer3,"Montant total dû",param.montantCible);


			mainLayout.addComponent(footer1);
			mainLayout.addComponent(footer2);
			mainLayout.addComponent(footer3);
		}
		else
		{
			// Footer 1 avec le prix total
			HorizontalLayout footer1 = new HorizontalLayout();
			footer1.setWidth("410px");
			fillFooter(footer1,"Montant total à régler",param.montantCible);
			mainLayout.addComponent(footer1);
		}

		mainLayout.setMargin(true);

	}

	private int getMontantTotalPaiement()
	{
		int montantTotal = param.avoirInitial;
		for (int i = 0; i < param.montant.length; i++)
		{
			montantTotal = montantTotal+param.montant[i];
		}
		return montantTotal;
	}

	private Label fillFooter(HorizontalLayout footer1, String message, int montantCible)
	{
		
			Label dateLabel = new Label(message);
			dateLabel.addStyleName("prix");
			dateLabel.setSizeFull();
			footer1.addComponent(dateLabel);
			footer1.setExpandRatio(dateLabel, 1.0f);
	
			Label prixTotal = new Label(new CurrencyTextFieldConverter().convertToString(montantCible));
			prixTotal.addStyleName("prix");
			prixTotal.setSizeFull();
			footer1.addComponent(prixTotal);
			footer1.setExpandRatio(prixTotal, 1.0f);
			
			return prixTotal;
	}

	protected void createButtonBar()
	{
		if (param.readOnly)
		{
			Button ok = addDefaultButton("OK", new Button.ClickListener()
			{

				@Override
				public void buttonClick(ClickEvent event)
				{
					handleAnnuler();
				}
			});
		}
		else
		{
			if (param.nbLig > 2)
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

			Button saveButton = addDefaultButton("Sauvegarder", new Button.ClickListener()
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

		// Lecture de la valeur dans la case tout en haut
		TextField tf = (TextField) item.getItemProperty(new Integer(0)).getValue();
		int qteRef = readValueInCell(tf);

		// Copie de cette valeur dans toutes les cases en dessous
		for (int i = 1; i < param.nbLig-1; i++)
		{
			if (isExcluded(i) == false)
			{
				Item item1 = table.getItem(new Integer(i));
				TextField tf1 = (TextField) item1.getItemProperty(new Integer(0)).getValue();
				tf1.setConvertedValue(qteRef);
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
		return (1 + 1) * (param.largeurCol + param.espaceInterCol) + "px";
	}

	

	private void addRow(int lig)
	{
		List<Object> cells = new ArrayList<Object>();

		Label dateLabel = new Label(param.leftPartLine.get(lig));
		dateLabel.addStyleName("big");
		dateLabel.addStyleName("align-center");
		dateLabel.setWidth(param.largeurCol + "px");

		cells.add(dateLabel);

		int qte = param.montant[lig];
		boolean isExcluded = isExcluded(lig);

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
				txt = "" + new CurrencyTextFieldConverter().convertToString(qte);
			}
			Label tf = new Label(txt);
			tf.addStyleName("align-center");
			tf.addStyleName("big");
			tf.setWidth((param.largeurCol - 10) + "px");
			cells.add(tf);
		}
		else
		{
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
				// Si derniere ligne : on  autorise les nombres négatifs (sauf si on en mode de saisie de la dernière ligne)
				boolean allowNegativeNumber =  (lig==param.nbLig-1) && (param.computeLastLine==true);
				final TextField tf = BaseUiTools.createCurrencyField("",allowNegativeNumber);
				tf.setData(new GridIJData(lig, 0));
				tf.setConvertedValue(new Integer(qte));
				tf.addValueChangeListener(new Property.ValueChangeListener()
				{
					@Override
					public void valueChange(ValueChangeEvent event)
					{
						try
						{
							updateModele();
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
				
				// Si derniere ligne : on desactive la saisie , sauf si pas de recalcul
				if ((lig==param.nbLig-1) && (param.computeLastLine==true) )
				{
					tf.setEnabled(false);
					lastLineTextField = tf;
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
	private boolean isExcluded(int lig)
	{
		if (param.excluded == null)
		{
			return false;
		}
		return param.excluded[lig];
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

	protected void handleSauvegarder()
	{
		try
		{
			updateModele();
		}
		catch (ErreurSaisieException e)
		{
			Notification.show("Erreur de saisie ");
			return;
		}

		boolean ret = performSauvegarder();
		if (ret==true)
		{
			close();
		}
	}

	/**
	 * Lecture de la table pour mettre à jour le modele
	 * @return
	 */
	private void updateModele() throws ErreurSaisieException
	{
		// On lit les lignes de 0 à N-1
		int cumul = param.avoirInitial;
		for (int i = 0; i < param.nbLig-1; i++)
		{
			Item item = table.getItem(new Integer(i));

			if (isExcluded(i) == false)
			{
				TextField tf = (TextField) item.getItemProperty(new Integer(0)).getValue();
				int qte = readValueInCell(tf);
				param.montant[i] = qte;
				cumul = cumul+qte;
			}
		}
		if (param.computeLastLine==true)
		{
			param.montant[param.nbLig-1] = param.montantCible-cumul;
			lastLineTextField.setConvertedValue(param.montant[param.nbLig-1]);
		}
		else
		{
			Item item = table.getItem(new Integer(param.nbLig-1));
			TextField tf = (TextField) item.getItemProperty(new Integer(0)).getValue();
			int qte = readValueInCell(tf);
			param.montant[param.nbLig-1] = qte;
			cumul = cumul+qte;
			montantTotalPaiement.setValue(new CurrencyTextFieldConverter().convertToString(cumul));
		}
		
	}

}
