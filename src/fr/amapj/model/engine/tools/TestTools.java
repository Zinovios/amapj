package fr.amapj.model.engine.tools;

import fr.amapj.model.engine.db.DbManager;
import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.view.engine.ui.AppConfiguration;

public class TestTools
{
	
	static public void init()
	{
		AppConfiguration.initializeForTesting();
		new DbManager().startDatabase();
		DbUtil.setDbNameForDeamonThread("amap1");
	}
	

}
