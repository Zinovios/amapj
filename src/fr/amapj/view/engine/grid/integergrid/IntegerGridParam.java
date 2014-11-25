package fr.amapj.view.engine.grid.integergrid;

import java.util.ArrayList;
import java.util.List;

import fr.amapj.view.engine.grid.GridHeaderLine;



/**
 * Liste des parametres pour un PopupIntegerGrid
 *
 */
public class IntegerGridParam
{
	//
	public int nbLig;
	
	//
	public int nbCol;
	
	// Contient les prix
	public int[] prix;
	
	// Contient les quantites qte[numero_ligne][numero_colonne]
	public int[][] qte;
	
	// Largeur de la colonne en pixel, exemple 110
	public int largeurCol;
	
	// Escpace entre les colonnes en pixel, exemple 3 
	public int espaceInterCol;

	// Grille en lecture seule
	public boolean readOnly;
	
	// Message specifique a afficher en haut de popup
	public String messageSpecifique;
	
	public List<GridHeaderLine> headerLines = new ArrayList<>();
	
	public List<String> leftPartLine = new ArrayList<>();
	
	// Contient toutes les cases qui sont exclues de la saisies
	// Si excluded est null : toutes les cases sont autorisées
	// Si une case est egale a true : alors la case est exclue
	public boolean[][] excluded;
	
	
	/*
	 * Partie calculée automatiquement et initialisée par la méthode initialize
	 */
	
	// Montant total du contrat en centimes
	public int montantTotal;
	
	// Montant total des lignes
	public int montantLig[];
	
	// Montant total des colonnes
	public int montantCol[];
			
	
	/**
	 * 
	 */
	public void initialize()
	{
		// Montant total des colonnes et montant total total
		montantCol = new int[nbCol];
		montantTotal = 0;
		for (int j = 0; j < nbCol; j++)
		{
			montantCol[j] = 0;
			int p = prix[j];
			for (int i = 0; i < nbLig; i++)
			{
				montantCol[j] = montantCol[j] +qte[i][j]*p;
				montantTotal = montantTotal +qte[i][j]*p;
			}
		}
		
		
		// Montant total des lignes
		montantLig = new int[nbLig];
		for (int i = 0; i < nbLig; i++)
		{
			montantLig[i] = 0;
			for (int j = 0; j < nbCol; j++)
			{
				montantLig[i] = montantLig[i] +qte[i][j]*prix[j];
			}
		}
	}
	
	
	/**
	 * Cette méthode met à jour à la fois la quantité et le montant total
	 * 
	 */
	public void updateQte(int lig,int col,int newQte)
	{
		int delta = (newQte-qte[lig][col])*prix[col];
		
		qte[lig][col] = newQte;
		montantLig[lig] = montantLig[lig]+ delta;
		montantCol[col] = montantCol[col]+ delta;
		montantTotal = montantTotal+delta;
	}
	
	
}
