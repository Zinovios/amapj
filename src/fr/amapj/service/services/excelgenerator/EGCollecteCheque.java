package fr.amapj.service.services.excelgenerator;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.service.services.mespaiements.DetailPaiementAFournirDTO;
import fr.amapj.service.services.mespaiements.MesPaiementsService;


/**
 * Permet la generation des feuilles de collecte de cheques
 * 
 *  
 *
 */
public class EGCollecteCheque extends AbstractExcelGenerator
{
	
	Long modeleContratId;
	
	public EGCollecteCheque(Long modeleContratId)
	{
		this.modeleContratId = modeleContratId;
	}

	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		et.addSheet("Amap", 1, 100);
		
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
		
		
		et.addRow("Bordereau de collecte des chèques",et.titre);
		et.addRow("",et.grasGaucheNonWrappe);
		
		et.addRow("Nom du contrat : "+mc.getNom(),et.grasGaucheNonWrappe);
		et.addRow("Nom du producteur : "+mc.getProducteur().getNom(),et.grasGaucheNonWrappe);
		et.addRow("Date limite de remise des chèques : "+df.format(mc.getDateRemiseCheque()),et.grasGaucheNonWrappe);
		et.addRow("Ordre des chèques : "+mc.getLibCheque(),et.grasGaucheNonWrappe);
		et.addRow("",et.grasGaucheNonWrappe);
		
		// Avec une sous requete, on obtient la liste de tous les utilisateur ayant commandé au moins un produit
		List<Utilisateur> utilisateurs = new MesContratsService().getUtilisateur(em, mc);
		et.addRow(utilisateurs.size()+" adhérents pour ce contrat",et.grasGaucheNonWrappe);
		et.addRow("",et.grasGaucheNonWrappe);
		
		
		for (Utilisateur utilisateur : utilisateurs)
		{
			et.addRow(utilisateur.getPrenom()+" "+utilisateur.getNom(),et.grasGaucheNonWrappe);
			Contrat c = getContrat(modeleContratId,em,utilisateur);
			List<DetailPaiementAFournirDTO> details = new MesPaiementsService().getPaiementAFournir(em,c);
			for (DetailPaiementAFournirDTO detail : details)
			{
				String text = detail.formatPaiement();
				et.addRow( text,et.nongrasGaucheWrappe);
			}
			et.addRow( "",et.nongrasGaucheWrappe);
		}
	}

	
	
	
	private Contrat getContrat(Long modeleContratId, EntityManager em, Utilisateur utilisateur)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		Query q = em.createQuery("select c from Contrat c where c.utilisateur =:u and c.modeleContrat=:mc");
		q.setParameter("mc",mc);
		q.setParameter("u",utilisateur);
		
		List<Contrat> cs = q.getResultList();
		if (cs.size()!=1)
		{
			throw new RuntimeException("Erreur inattendue pour "+utilisateur.getNom()+utilisateur.getPrenom());
		}
		
		return cs.get(0);
	}


	@Override
	public String getFileName(EntityManager em)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		return "collecte-cheque-"+mc.getNom();
	}
	

	@Override
	public String getNameToDisplay(EntityManager em)
	{
		return "la feuille de collecte des cheques";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}

}
