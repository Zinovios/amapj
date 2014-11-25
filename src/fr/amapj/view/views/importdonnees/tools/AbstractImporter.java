package fr.amapj.view.views.importdonnees.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.engine.tools.TableItem;

@SuppressWarnings("serial")
abstract public class AbstractImporter<T extends TableItem> implements Receiver , SucceededListener
{

	private List<String> errorMessage = new ArrayList<>();

	private ByteArrayOutputStream baos;

	abstract public void saveInDataBase(List<T> utilisateurs);
	
	abstract public T createDto(String[] strs);
	
	abstract public int getNumCol();
	
	abstract public List<T> getAllDataInDatabase();
	
	abstract public String checkBasic(T dto);
	
	abstract public String checkDifferent(T dto1, T dto2);
	
	abstract public void dumpInfo(List<String> errorMessage, T dto);
	


	public OutputStream receiveUpload(String filename, String mimeType)
	{
		baos = new ByteArrayOutputStream();
		return baos;
	}

	public void uploadSucceeded(SucceededEvent event)
	{
		try
		{	
			processFile();
			if (errorMessage.size()==0)
			{
				MessagePopup popup = new MessagePopup("Chargement effectué", "Le chargement a été effectué");
				MessagePopup.open(popup);
			}
			else
			{
				MessagePopup popup = new MessagePopup("Erreur lors du chargement", errorMessage);
				MessagePopup.open(popup);
			}
		}
		catch (IOException e)
		{
			MessagePopup popup = new MessagePopup("Erreur lors du chargement", e.getMessage());
			MessagePopup.open(popup);
		}
	}

	private void processFile() throws IOException
	{

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()));
		DataFormatter df = new DataFormatter(Locale.FRANCE);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		int numCol = getNumCol();
		
		List<T> existing = getAllDataInDatabase();
		
		List<T> utilisateurs = new ArrayList<>();

		int lastRowNum = sheet.getLastRowNum();
		
		for (int numLigne = 2; numLigne <= lastRowNum+1; numLigne++)
		{
			Row row = sheet.getRow(numLigne-1);
			
			System.out.println(numLigne);
			System.out.println("row="+row);
			
			String[] strs = new String[numCol];
			boolean isEmptyLine = true;
			for (int i = 0; i < strs.length; i++)
			{
				strs[i] = getCell(row,i,df);
				if ( (strs[i]!=null) && (strs[i].length()>0) )
				{
					isEmptyLine = false;
				}
			}
			
			if (isEmptyLine==false)
			{
				
				// On crée le DTO
				T dto = createDto(strs);
				
				// On vérifie tout d'abord si les elements de base sont bien présents
				performBasicCheck(dto,numLigne);
				if (errorMessage.size()!=0)
				{
					return;
				}
				
				// On verifie ensuite si l'élément est bien compatible avec les autres lignes du fichier 
				checkLineInSameFile(utilisateurs,dto,numLigne);
				if (errorMessage.size()!=0)
				{
					return;
				}
				
	
				// On verifie ensuite si l'élément est bien compatible avec les autres enregistrement de la base			
				checkLineInDataBase(existing,dto,numLigne);
				if (errorMessage.size()!=0)
				{
					return;
				}
				
				
				utilisateurs.add(dto);
			}
			
		}
		
		saveInDataBase(utilisateurs);
	}

	private void performBasicCheck(T dto,int numLigne)
	{
		String msg = checkBasic(dto);
		if (msg!=null)
		{
			errorMessage.add("Il y a une erreur sur la ligne "+numLigne+" du fichier à importer");
			errorMessage.add("Sur cette ligne :");
			errorMessage.add(msg);
			errorMessage.add("Voici d'autres informations sur la ligne "+numLigne+":");
			dumpInfo(errorMessage, dto);
			errorMessage.add("");
			return;
		}
	}

		
	private void checkLineInSameFile(List<T> utilisateurs, T dto, int numLigne) 
	{
		int numMax = utilisateurs.size();
		for (int i = 0; i < numMax; i++)
		{
			T utilisateurDTO = utilisateurs.get(i);
			String msg = checkDifferent(utilisateurDTO,dto);
			
			if (msg!=null)
			{
				errorMessage.add("Il y a une incohèrence entre la ligne "+(i+2)+" et la ligne "+numLigne+ " du fichier à importer");
				errorMessage.add("Sur ces deux lignes :");
				errorMessage.add(msg);
				errorMessage.add("Voici d'autres informations sur la ligne "+(i+2)+":");
				dumpInfo(errorMessage, utilisateurDTO);
				errorMessage.add("");
				errorMessage.add("Voici d'autres informations sur la ligne "+numLigne+":");
				dumpInfo(errorMessage, dto);
				errorMessage.add("");
				return;
			}
		}
	}
	

	private void checkLineInDataBase(List<T> existing, T dto, int numLigne) 
	{
		for (T utilisateurDTO : existing)
		{
			String msg = checkDifferent(utilisateurDTO,dto);
			
			if (msg!=null)
			{
				errorMessage.add("Il y a une erreur sur la ligne "+numLigne+" du fichier à importer");
				errorMessage.add("Sur cette ligne :");
				errorMessage.add(msg);
				errorMessage.add("Il existe déjà dans la base de données un enregistrement avec ces informations :");
				dumpInfo(errorMessage, utilisateurDTO);
				errorMessage.add("");
				errorMessage.add("Voici d'autres informations sur la ligne "+numLigne+":");
				dumpInfo(errorMessage, dto);
				errorMessage.add("");
				return;
			}
		}
		
	}
	
	
	

	// On vérifie que la chaine est vide
	protected boolean isEmpty(String str)
	{
		if ((str==null) || (str.length()==0))
		{
			return true;
		}
		return false;
		
	}

	

	private String getCell(Row row, int i,DataFormatter df)
	{
		if (row==null)
		{
			return null;
		}
		Cell cell = row.getCell(i);
		if (cell == null)
		{
			return null;
		}

		return df.formatCellValue(cell);
	}
	
	
	/**
	 * Permet de verifier un élement, à utiliser dans les fichiers de base
	 * 
	 * Retourne null si tout est ok, sinon retourne une liste de messages d'erreur
	 */
	public List<String> checkThisElement(T dto)
	{
		String str = checkBasic(dto);
		if (str!=null)
		{
			errorMessage.add(str);
			return errorMessage;
		}
		
		List<T> existing = getAllDataInDatabase();
		for (T t : existing)
		{
			// On vérifie que ce n'est pas l'élement déjà dans la base que l'on est en train de modifier
			if (isSameId(t,dto)==false)
			{
				
				str = checkDifferent(t,dto);
				
				if (str!=null)
				{
					errorMessage.add("Il existe un enregistrement dans la base de données qui entre en conflit avec votre saisie");
					errorMessage.add("Raison :");
					errorMessage.add(str);
					errorMessage.add("Il existe déjà dans la base de données un enregistrement avec ces informations :");
					dumpInfo(errorMessage, t);
					errorMessage.add("");
					return errorMessage;
				}
			}
		}
		return null;
	}
	
	private boolean isSameId(T t1, T t2)
	{
		TableItem ti1 = (TableItem) t1;
		TableItem ti2 = (TableItem) t2;
		
		return LongUtils.equals(ti1.getId(),ti2.getId());
	}

	public void checkThisElementAsException(T dto) throws OnSaveException
	{
		List<String> strs = checkThisElement(dto);
		if (strs!=null)
		{
			throw new OnSaveException(strs);
		}
	}
	
	
	
	
	
	
	
	
	public void test(String filename) throws IOException
	{
		TestTools.init();
		
	
		baos = new ByteArrayOutputStream();
		baos.write(Files.readAllBytes(FileSystems.getDefault().getPath(filename)));

		
		processFile();
		if (errorMessage.size()==0)
		{
			System.out.println("Fichier importé avec succés");
		}
		else
		{
			System.out.println("Erreur lors du chargement");
			for (String string : errorMessage)
			{
				System.out.println(string);
			}
		}		
	}


	

}
