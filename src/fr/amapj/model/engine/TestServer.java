package fr.amapj.model.engine;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConfiguration;

public class TestServer
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException
	{
		HsqlProperties argProps = new HsqlProperties();

        argProps.setProperty("server.database.0","file:c:\\amapj\\workspace\\amapj\\db\\data\\amapj");
        argProps.setProperty("server.dbname.0","amapj");
        argProps.setProperty("server.no_system_exit","false");
        
      
        ServerConfiguration.translateAddressProperty(argProps);

        // finished setting up properties;
        Server server = new Server();

        try 
        {
            server.setProperties(argProps);
        } 
        catch (Exception e) 
        {
            //server.printError("Failed to set properties");
            //server.printStackTrace(e);
        	// TODO
        	e.printStackTrace();
            return;
        }

        // now messages go to the channel specified in properties
        System.out.println("Startup sequence initiated from main() method");

        

        server.start();
        
        
        while (true)
		{
			System.out.println("Tomcat running");
			Thread.sleep(2000);
			
		}
        

	}

}
