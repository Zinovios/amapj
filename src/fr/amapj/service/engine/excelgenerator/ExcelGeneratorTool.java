package fr.amapj.service.engine.excelgenerator;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Permet la génération facile des fichiers excel
 * 
 * Dans ce système, il est nécessaire de fixer dès le départ le nombre de colonnes dans la feuille
 * 
 *  
 *
 */
public class ExcelGeneratorTool
{
	Workbook wb;
	Sheet sheet;
	Row currentRow;
	
	
	Font fontNonGras;
	Font fontGras;
	Font fontGrasBlue;
	Font fontGrasHaut;

	
	public CellStyle grasGaucheNonWrappe;
	public CellStyle grasGaucheNonWrappeColor;
	public CellStyle grasGaucheNonWrappeBordure;
	
	public CellStyle grasCentreBordure;
	public CellStyle grasCentreBordureColor;
	
	
	public CellStyle nonGrasCentreBordure;
	public CellStyle nonGrasCentreBordureColor;
	public CellStyle nonGrasGaucheBordure;
	public CellStyle nongrasGaucheWrappe;
	
	public CellStyle prixCentreBordure;
	public CellStyle prixCentreBordureColor;
	public CellStyle titre;
	
	boolean firstLine = true;
	
	
	int nbColMax;
	

	public ExcelGeneratorTool(ExcelFormat format)
	{
		if (format==ExcelFormat.XLS)
		{
			wb = new HSSFWorkbook();
		}
		else
		{
			wb = new XSSFWorkbook();
		}
		initializeFont();
		initializeStyle();
	}


	/**
	 * On utilise en tout 4 fontes 
	 *  ->une Arial taille 10 gras noir
	 *  ->une Arial taille 10 non gras noir
	 *  ->une Arial taille 10 gras bleue
	 *  ->une Arial taille 12 gras noir 
	 */
	private void initializeFont()
	{
		// Création des différentes fontes
		fontNonGras = wb.createFont();
		fontNonGras.setFontHeightInPoints((short) 10);
		fontNonGras.setFontName("Arial");
		fontNonGras.setColor(IndexedColors.BLACK.getIndex());
		fontNonGras.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		fontNonGras.setItalic(false);

		fontGras = wb.createFont();
		fontGras.setFontHeightInPoints((short) 10);
		fontGras.setFontName("Arial");
		fontGras.setColor(IndexedColors.BLACK.getIndex());
		fontGras.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontGras.setItalic(false);

		fontGrasBlue = wb.createFont();
		fontGrasBlue.setFontHeightInPoints((short) 10);
		fontGrasBlue.setFontName("Arial");
		fontGrasBlue.setColor(IndexedColors.BLUE.getIndex());
		fontGrasBlue.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontGrasBlue.setItalic(false);

		fontGrasHaut = wb.createFont();
		fontGrasHaut.setFontHeightInPoints((short) 12);
		fontGrasHaut.setFontName("Arial");
		fontGrasHaut.setColor(IndexedColors.BLACK.getIndex());
		fontGrasHaut.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontGrasHaut.setItalic(false);

	}

	
	
	/**
	 * On utilise en tout 11 styles
	 */
	private  void initializeStyle()
	{

		// Création des styles
		grasGaucheNonWrappe = wb.createCellStyle();
		grasGaucheNonWrappe.setAlignment(CellStyle.ALIGN_LEFT);
		grasGaucheNonWrappe.setFont(fontGras);
		grasGaucheNonWrappe.setWrapText(false);
		beWhite(grasGaucheNonWrappe);

		grasGaucheNonWrappeColor = wb.createCellStyle();
		grasGaucheNonWrappeColor.cloneStyleFrom(grasGaucheNonWrappe);
		beOrange(grasGaucheNonWrappeColor);

		grasGaucheNonWrappeBordure = wb.createCellStyle();
		grasGaucheNonWrappeBordure.setAlignment(CellStyle.ALIGN_LEFT);
		grasGaucheNonWrappeBordure.setFont(fontGras);
		grasGaucheNonWrappeBordure.setWrapText(false);
		addBorderedStyle(grasGaucheNonWrappeBordure);
		beWhite(grasGaucheNonWrappeBordure);

		grasCentreBordure = wb.createCellStyle();
		grasCentreBordure.setAlignment(CellStyle.ALIGN_CENTER);
		grasCentreBordure.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		grasCentreBordure.setFont(fontGras);
		addBorderedStyle(grasCentreBordure);
		grasCentreBordure.setWrapText(true);
		beWhite(grasCentreBordure);

		grasCentreBordureColor = wb.createCellStyle();
		grasCentreBordureColor.cloneStyleFrom(grasCentreBordure);
		beOrange(grasCentreBordureColor);

		nonGrasCentreBordure = wb.createCellStyle();
		nonGrasCentreBordure.setAlignment(CellStyle.ALIGN_CENTER);
		nonGrasCentreBordure.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		nonGrasCentreBordure.setFont(fontNonGras);
		addBorderedStyle(nonGrasCentreBordure);
		nonGrasCentreBordure.setWrapText(true);
		beWhite(nonGrasCentreBordure);

		nonGrasCentreBordureColor = wb.createCellStyle();
		nonGrasCentreBordureColor.cloneStyleFrom(nonGrasCentreBordure);
		beOrange(nonGrasCentreBordureColor);

		nonGrasGaucheBordure = wb.createCellStyle();
		nonGrasGaucheBordure.setAlignment(CellStyle.ALIGN_LEFT);
		nonGrasGaucheBordure.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		nonGrasGaucheBordure.setFont(fontNonGras);
		addBorderedStyle(nonGrasGaucheBordure);
		nonGrasGaucheBordure.setWrapText(true);
		beWhite(nonGrasGaucheBordure);

		prixCentreBordure = wb.createCellStyle();
		prixCentreBordure.setAlignment(CellStyle.ALIGN_CENTER);
		prixCentreBordure.setFont(fontGrasBlue);
		addBorderedStyle(prixCentreBordure);
		prixCentreBordure.setWrapText(true);
		DataFormat df = wb.createDataFormat();
		prixCentreBordure.setDataFormat(df.getFormat("#,##0.00€"));
		beWhite(prixCentreBordure);

		prixCentreBordureColor = wb.createCellStyle();
		prixCentreBordureColor.cloneStyleFrom(prixCentreBordure);
		beOrange(prixCentreBordureColor);
		
	    
	    titre = wb.createCellStyle(); 
	    titre.setAlignment(CellStyle.ALIGN_CENTER);
	    titre.setFont(fontGrasHaut);
	    titre.setWrapText(false);
	 	beWhite(titre);
	 	
	 	
	 	  CellStyle nongrasGaucheWrappe = wb.createCellStyle(); 
		    nongrasGaucheWrappe.setAlignment(CellStyle.ALIGN_LEFT);
		    nongrasGaucheWrappe.setFont(fontNonGras);
		    nongrasGaucheWrappe.setWrapText(true);
		    beWhite(nongrasGaucheWrappe);

	}
	
	private void beWhite(CellStyle grasGaucheNonWrappe)
	{
		grasGaucheNonWrappe.setFillPattern(CellStyle.SOLID_FOREGROUND);
		grasGaucheNonWrappe.setFillForegroundColor(IndexedColors.WHITE.getIndex());

	}

	private void beOrange(CellStyle grasGaucheNonWrappe)
	{
		grasGaucheNonWrappe.setFillPattern(CellStyle.SOLID_FOREGROUND);
		grasGaucheNonWrappe.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

	}

	private void addBorderedStyle(CellStyle style)
	{
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	}
	
	
	/**
	 * Permet de créer une feuille, en indiquant le nombre de colonnes
	 * et la taille de chaque colonne en caractères
	 * 
	 * 
	 * @param sheetName
	 * @param nbCol
	 * @param colWidth
	 * @return
	 */
	public void addSheet(String sheetName,int nbCol,int colWidth)
	{
		sheet = wb.createSheet(sheetName);
		currentRow = null;
		firstLine = true;
		
		this.nbColMax = nbCol;

		
		for (int i = 0; i < nbCol; i++)
		{
			//
			setColumnWidth(i, colWidth);

			// Style par defaut pour toutes les colonnes
			sheet.setDefaultColumnStyle(i, grasGaucheNonWrappe);
		}
	}
	
	
	/**
	 * Permet d'ajuster la page en largeur à une page pile 
	 * en modifiant la largeur de chaque colonne
	 */
	public void adjustSheetForOnePage()
	{
		// La largueur d'une page est de 95 *256 
		int PAGE_WIDTH = 95 * 256;
		
		int nbTotal=0;
		for (int i = 0; i < nbColMax; i++)
		{
			nbTotal = nbTotal+sheet.getColumnWidth(i);
		}
		
		if (nbTotal<=PAGE_WIDTH)
		{
			return;
		}
		
		for (int i = 0; i < nbColMax; i++)
		{
			int newWidth = (sheet.getColumnWidth(i)*PAGE_WIDTH)/nbTotal;
			sheet.setColumnWidth(i, newWidth);
		}
	}
	
	/**
	 * Permet de déplacer la page courante à l'index spécifié
	 */
	public void setSheetFirst()
	{
		int nb = wb.getNumberOfSheets();
		
		// On construit la liste des noms des feuilles dans le bon ordre
		List<String> sheetName = new ArrayList<>();
		sheetName.add(sheet.getSheetName());
		for (int i = 0; i < nb-1; i++)
		{
			Sheet s = wb.getSheetAt(i);
			sheetName.add(s.getSheetName());
		}
		
		// On applique ensuite à chaque feuille son nouveau numero d'ordre
		for (int i = 0; i < nb; i++)
		{
			wb.setSheetOrder(sheetName.get(i),i);
			Sheet s = wb.getSheetAt(i);
			if (i==0)
			{
				s.setSelected(true);
			}
			else
			{
				s.setSelected(false);
			}
		}
	}

	
	
	/**
	 * Permet de fixer la hauteur de la ligne en nombre de ligne de texte avec fonte 12
	 * @param nbLine
	 */
	public void setRowHeigth(int nbLine)
	{
		
		currentRow.setHeight( (short) (20*12*nbLine));
	}
	
	
	/**
	 * Permet d'ajouter une ligne, avec le bon nombre de celulles
	 */
	public Row addRow()
	{		
		int index = sheet.getLastRowNum();
		if (firstLine)
		{
			index=-1;
			firstLine=false;
		}
		
			
		Row row = sheet.createRow(index+1);
		
		for (int i = 0; i < nbColMax; i++)
		{
			Cell cell = row.createCell(i);
			cell.setCellStyle(grasGaucheNonWrappe);
		}
		
		currentRow = row;
		
		return row;
	}

	

	/**
	 * Permet d'ajouter une ligne avec la première cellule contenant 
	 * le style indiqué et le texte 
	 * 
	 * @param text
	 * @param style
	 */
	public void addRow(String text, CellStyle style)
	{
		Row row= addRow();
		Cell cell = row.getCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(text);
	}
	
	/**
	 * Permet de merger les cellules par rapport à la ligne courante, 
	 * vers le droite, et d'une longueur nbCol
	 * 
	 * @param nbRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeCellsRight(int numCol,int nbCol)
	{
		checkNumCol(numCol);
		checkNumCol(numCol+nbCol-1);
		
		int lastRow = currentRow.getRowNum();
		int firstRow = lastRow;
		mergeCells(firstRow, lastRow, numCol, numCol+nbCol-1);
	}

	
	/**
	 * Permet de merger les cellules par rapport à la ligne courante, 
	 * vers le haut, et d'une hauteur de nbRow
	 * 
	 * @param nbRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeCellsUp(int numCol,int nbRow)
	{
		checkNumCol(numCol);
		
		mergeCellsUp(numCol,numCol,nbRow);
	}
	
	/**
	 * Permet de merger les cellules par rapport à la ligne courante, 
	 * vers le haut, et d'une hauteur de nbRow
	 * 
	 * @param nbRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeCellsUp(int firstCol, int lastCol,int nbRow )
	{
		checkNumCol(firstCol);	
		checkNumCol(lastCol);
		
		int lastRow = currentRow.getRowNum();
		int firstRow = lastRow-nbRow+1;
		mergeCells(firstRow, lastRow, firstCol, lastCol);
	}
	
	
	/**
	 * Merge des cellules
	 * 
	 * Le style de la cellule en haut à gauche est copié sur toutes les autres cellules 
	 * 
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol)
	{
		CellStyle style = sheet.getRow(firstRow).getCell(firstCol).getCellStyle();
		
		for (int numRow = firstRow; numRow <= lastRow; numRow++)
		{
			Row row = sheet.getRow(numRow);	
			for (int numCol = firstCol; numCol <= lastCol; numCol++)
			{
				row.getCell(numCol).setCellStyle(style);
			}
		}
		
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	
	public void createEmptyCell(Row row, int firstCol, int lastCol)
	{
		for (int i = firstCol; i <= lastCol; i++)
		{
			row.createCell(i);
		}
	}
	
	
	// Gestion de la couleur
	public CellStyle switchColor(CellStyle style, int i)
	{
		if ((i%2)==0)
		{
			return style;
		}
		else
		{
			if (style==grasCentreBordure)
			{
				return grasCentreBordureColor;
			}
			else if (style==prixCentreBordure)
			{
				return prixCentreBordureColor;
			}
			else if (style == nonGrasCentreBordure)
			{
				return nonGrasCentreBordureColor;
			}
			else
			{
				throw new RuntimeException("erreur de programme");
			}
		}
	}




	
	
	/**
	 * Retourne true si la ligne doit être colorée
	 * @param i
	 * @return
	 */
	private boolean isColored(int i, int nbProd)
	{
		int numDate = i / nbProd;
		if ((numDate % 2) == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private void colorize(Row row, int nbDates, int nbProd)
	{
		for (int i = 0; i < nbDates / 2; i++)
		{
			int index = 3 + nbProd + i * 2 * nbProd;
			for (int j = 0; j < nbProd; j++)
			{
				Cell cell = row.getCell(j + index);
				if (cell != null)
				{
					CellStyle st = cell.getCellStyle();
					beOrange(st);
					cell.setCellStyle(st);

				}
			}

		}

	}



	

	/**
	 * index : 1 based !!!
	 * 
	 */
	private void set(Row row, int index, String str)
	{
		row.createCell(index - 1).setCellValue(str);
	}

	private void setFormula(Row row, int index, String str)
	{
		row.createCell(index - 1).setCellFormula(str);
	}

	
	
	
	public Workbook getWb()
	{
		// Recalcul des formules
		/*if (wb instanceof XSSFWorkbook)
		{
			XSSFFormulaEvaluator.evaluateAllFormulaCells( (XSSFWorkbook) wb);
		}
		else
		{
			HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		}
		*/
		HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		
		return wb;
	}
	
	
	// Opérations sur les colonnes
	
	/**
	 * Permet de positionner une largeur en nombre de caractères
	 * @param width
	 */
	public void setColumnWidth(int numCol,int width)
	{
		sheet.setColumnWidth(numCol, width*256);
	}

	  
	
	
	// Opérations sur les cellules


	/**
	 * Permet de positionner le contenu d'une cellule avec un texte
	 * @param numCol
	 * @param text
	 */
	public void setCell(int numCol, String text,CellStyle style)
	{
		Cell cell = currentRow.getCell(numCol);
		cell.setCellValue(text);
		cell.setCellStyle(style);
	}


	public void setCellPrix(int numCol, int montant,CellStyle style)
	{
		Cell cell = currentRow.getCell(numCol);
		
		if (montant==0)
		{
			// Ne rien faire 
		}
		else
		{
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(( (double) montant)/100.0);
		}
		
		cell.setCellStyle(style);
	}
	
	
	public void setCellQte(int numCol, int qte,CellStyle style)
	{
		checkNumCol(numCol);
		Cell cell = currentRow.getCell(numCol);
		
		
		if (qte==0)
		{
			// Ne rien faire 
		}
		else
		{
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((double) qte);
		}
		
		cell.setCellStyle(style);
	}
	
	
	
	
	private void checkNumCol(int numCol)
	{
		if (numCol<0)
		{
			throw new RuntimeException("Vous essayez d'accèder à la colonne "+numCol+" : les nombres négatifs sont interdits ");
		}
		else if (numCol>=this.nbColMax)
		{
			throw new RuntimeException("La feuille possède "+this.nbColMax+" colonnes et vous essayez d'accèder à la colonne "+numCol);
		}
		
	}


	// PARTIE SUR LES SOMMES
	
	/**
	 * Permet d'indiquer que cette celule est la somme de X cellules de la même ligne.
	 *
	 * Les cellules a sommer sont obligatoirement contigues
	 * 
	 */
	public void setCellSumInRow(int numCol,int firstCellOfTheSum, int nbCellToSum,CellStyle style)
	{
		String formula = "SUM(" + getCellLabel(firstCellOfTheSum) + ":" + getCellLabel(firstCellOfTheSum+nbCellToSum) + ")";

		setCellFormula(numCol, formula, style);
	}
	
	
	/**
	 *Permet d'indiquer que cette celule est la somme de X cellules de la même ligne.
	 *
	 * Les cellules a sommer ne sont pas forcément contigues, elles sont séparés de <code>stepBetweenCellToSum</code> cases
	 * 
	 *
	 */
	public void setCellSumInRow(int numCol,int firstCellOfTheSum, int stepBetweenCellToSum,int nbCellToSum,int[] additionalCells,CellStyle style)
	{
		String formula = asSumString(additionalCells);
		if (formula.length()!=0)
		{
			formula=formula+"+";
		}
		
		for (int i = 0; i < nbCellToSum; i++)
		{
			formula=formula+getCellLabel(firstCellOfTheSum+stepBetweenCellToSum*i);
			if (i!=nbCellToSum-1)
			{
				formula=formula+"+";
			}
		}
		setCellFormula(numCol, formula, style);
	}
	
	
	/**
	 * Permet d'indiquer que cette celule est la somme de X cellules de la même colonne, en dessous de la cellule courante
	 *
	 * Les cellules a sommer sont obligatoirement contigues
	 * 
	 * 
	 * deltaVertical =1 si ca demarre juste en dessous
	 * deltaVertical =2 si il y a une case d'espace 
	 * 
	 */
	public void setCellSumInColDown(int numCol,int deltaVertical, int nbCellToSum,CellStyle style)
	{
		int firstNumRow = currentRow.getRowNum()+deltaVertical;
		int lastNumRow = firstNumRow+nbCellToSum-1;
		
		String formula = "SUM(" + getCellLabel(firstNumRow,numCol) + ":" + getCellLabel(lastNumRow,numCol) + ")";

		setCellFormula(numCol, formula, style);
	}
	
	/**
	 * Permet d'indiquer que cette celule est la somme de X cellules de la même colonne, en dessus de la cellule courante
	 *
	 * Les cellules a sommer sont obligatoirement contigues
	 * 
	 * 
	 * deltaVertical =1 si ca demarre juste en dessus
	 * deltaVertical =2 si il y a une case d'espace 
	 * 
	 */
	public void setCellSumInColUp(int numCol,int deltaVertical, int nbCellToSum,CellStyle style)
	{
		int firstNumRow = currentRow.getRowNum()-deltaVertical-nbCellToSum+1;;
		int lastNumRow = firstNumRow+nbCellToSum-1;
		
		String formula = "SUM(" + getCellLabel(firstNumRow,numCol) + ":" + getCellLabel(lastNumRow,numCol) + ")";

		setCellFormula(numCol, formula, style);
	}
	
	
	// PARTIE SUMPROD
	
	
	/**
	 * Permet d'indiquer que cette celule est la somme du produit de X cellules de la même ligne avec une autre ligne fixe
	 *
	 * Les cellules a sommer et multiplier sont obligatoirement contigues
	 * 
	 * rowIndex représente l'index de la ligne qu va être multiplié avant de faire la multiplication 
	 * rowIndex est 0-based 
	 * 
	 */
	public void setCellSumProdInRow(int numCol,int firstCellOfTheSum, int nbCellToSum,int rowIndex, CellStyle style)
	{
		String formula = 	"SUMPRODUCT(" 
							+ getCellLabel(rowIndex,firstCellOfTheSum) + ":" + getCellLabel(rowIndex,firstCellOfTheSum+nbCellToSum) 
							+ ","
							+ getCellLabel(firstCellOfTheSum) + ":" + getCellLabel(firstCellOfTheSum+nbCellToSum) + ")";

		setCellFormula(numCol, formula, style);
	}
	
	
	
	// PARTIE FORMULES
	
	
	
	/**
	 *Permet d'indiquer que cette celule une formule
	 * 
	 *
	 */
	public void setCellFormula(int numCol,String formula,CellStyle style)
	{
		Cell cell = currentRow.getCell(numCol);
		
		cell.setCellFormula(formula);
		cell.setCellStyle(style);
	}
	
	/**
	 * Permet de créer une formule simple sur une ligne, en disant 
	 * que la cellule numCol est égale à la somme d'autres cellules de la ligne
	 * et la la soutracation d'autres cellules de la ligne
	 */
	public void setCellBasicFormulaInRow(int numCol, int[] cellsToAdd, int[] cellsToSubstract, CellStyle style)
	{
		String sum=asSumString(cellsToAdd);
		String sub=asSumString(cellsToSubstract);
		
		String formula=sum;
		if (sub.length()!=0)
		{
			formula = formula+"-("+sub+")";
		}
		setCellFormula(numCol, formula, style);
	}
	
	
	private String asSumString(int[] cells)
	{
		if ( (cells==null) || (cells.length==0) )
		{
			return "";
		}
		
		String formula="";
		for (int i = 0; i < cells.length; i++)
		{
			int cell = cells[i];	
			formula=formula+getCellLabel(cell);
			if (i!=cells.length-1)
			{
				formula=formula+"+";
			}
		}
		return formula;
	}
	
	
	
	// GESTION DES LABELS
	
	
	/**
	 * Retourne le label de la cellule indiqué, par exemple A9
	 * 
	 * ligIndex et colIndex sont 0-based
	 */
	public String getCellLabel(int ligIndex, int colIndex)
	{
		return CellReference.convertNumToColString(colIndex) + (ligIndex+1);
	}
	
	
	/**
	 * Retourne le label de la cellule indiqué, par exemple A9
	 * 
	 * @param row
	 * @param colIndex
	 * @return
	 */
	public String getCellLabel(Row row, int colIndex)
	{
		return CellReference.convertNumToColString(colIndex) + (row.getRowNum()+1);
	}

	/**
	 * Retourne le label de la cellule indiqué, par exemple A9, sur la ligne courante 
	 * 
	 * @param row
	 * @param colIndex
	 * @return
	 */
	public String getCellLabel(int colIndex)
	{
		return getCellLabel(currentRow,colIndex);
	}

	
	
	/**
	 * Cacher les colonnes
	 */

	public void setColHidden(int columnIndex, boolean hidden)
	{
		sheet.setColumnHidden(columnIndex, hidden);
	}

}
