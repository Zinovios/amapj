package fr.amapj.view.engine.grid.booleangrid;

import java.util.ArrayList;
import java.util.List;

import fr.amapj.view.engine.grid.GridHeaderLine;



/**
 * Liste des parametres pour un PopupBooleanGrid
 *
 */
public class BooleanGridParam
{
	//
	public int nbLig;
	
	//
	public int nbCol;
	
	// Contient les boolean qte[numero_ligne][numero_colonne]
	public boolean[][] box;
	
	// Largeur de la colonne en pixel, exemple 110
	public int largeurCol;
	
	// Escpace entre les colonnes en pixel, exemple 3 
	public int espaceInterCol;

	// Message specifique a afficher en haut de popup
	public String messageSpecifique;
	
	public List<GridHeaderLine> headerLines = new ArrayList<>();
	
	public List<String> leftPartLine = new ArrayList<>();	
	
	public List<String> leftPartLine2 = null;
}
