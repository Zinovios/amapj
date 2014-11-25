package fr.amapj.model.engine.ddl;

import java.text.DecimalFormat;


/**
 * Cette classe permet de cr�er des roles dans la base de donn�es
 */
public class GeneratePersistenceXml
{

	public void createData()
	{
		DecimalFormat df = new DecimalFormat("00");
		
		for (int i = 0; i < 100; i++)
		{	
			System.out.println("<persistence-unit name=\"pu"+df.format(i)+"\"> <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>  <exclude-unlisted-classes>false</exclude-unlisted-classes>  <properties> 	</properties> 	</persistence-unit> ");
		}

		
		
	}

	public static void main(String[] args)
	{
		GeneratePersistenceXml generateSqlSchema = new GeneratePersistenceXml();
		generateSqlSchema.createData();

	}

}
