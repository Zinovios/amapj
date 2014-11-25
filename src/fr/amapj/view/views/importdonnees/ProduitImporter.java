package fr.amapj.view.views.importdonnees;

import java.io.IOException;
import java.util.List;

import fr.amapj.common.StringUtils;
import fr.amapj.service.services.importdonnees.ImportDonneesService;
import fr.amapj.service.services.importdonnees.ImportProduitProducteurDTO;
import fr.amapj.view.views.importdonnees.tools.AbstractImporter;

@SuppressWarnings("serial")
public class ProduitImporter extends AbstractImporter<ImportProduitProducteurDTO>
{

	@Override
	public int getNumCol()
	{
		return 3;
	}

	@Override
	public List<ImportProduitProducteurDTO> getAllDataInDatabase()
	{
		List<ImportProduitProducteurDTO> existing = new ImportDonneesService().getAllProduits();
		return existing;
	}

	@Override
	public String checkBasic(ImportProduitProducteurDTO dto)
	{
		if (isEmpty(dto.produit))
		{
			return 	"Le nom du produit n'est pas renseigné. Il est obligatoire.";
		}

		if (isEmpty(dto.producteur))
		{
			return "Le nom du producteur n'est pas renseigné. Elle est obligatoire.";
		}
		
		return null;
		
	}
	
	@Override
	public ImportProduitProducteurDTO createDto(String[] strs)
	{
		ImportProduitProducteurDTO dto = new ImportProduitProducteurDTO();
		
		dto.producteur = strs[0];
		dto.produit = strs[1];
		dto.conditionnement = strs[2];
		
		return dto;
	}

	@Override
	public void saveInDataBase(List<ImportProduitProducteurDTO> prods)
	{	
		new ImportDonneesService().insertDataProduits(prods);
	}

	
	@Override
	public String checkDifferent(ImportProduitProducteurDTO dto1, ImportProduitProducteurDTO dto2)
	{	
		if (    dto1.producteur.equalsIgnoreCase(dto2.producteur)
				&& dto1.produit.equalsIgnoreCase(dto2.produit) 
				&& (StringUtils.equalsIgnoreCase(dto1.conditionnement, dto2.conditionnement)) )
		{
			return "Deux produits chez le même producteur ont le même nom et condtionnement alors que ceci est interdit.";
		}
		
		return null;
		
	}
	
	@Override
	public void dumpInfo(List<String> errorMessage,ImportProduitProducteurDTO dto)
	{
		errorMessage.add("Producteur:"+dto.producteur);
		errorMessage.add("Nom du produit:"+dto.produit);
		errorMessage.add("Conditionnement du produit:"+dto.conditionnement);
		
	}
	

	
	
	
	public static void main(String[] args) throws IOException
	{		
		ProduitImporter importer = new ProduitImporter();
		importer.test("liste-adherents-1.xls");
			
	}

}
