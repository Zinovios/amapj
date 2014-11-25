package fr.amapj.service.services.searcher;

import fr.amapj.model.engine.Identifiable;


public class SearcherBean
{
	private String lib;
	
	private Long id;
	
	public SearcherBean(Identifiable identifiable,String lib)
	{
		this.id = identifiable.getId();
		this.lib = lib;
	}

	public String getLib()
	{
		return lib;
	}

	public void setLib(String lib)
	{
		this.lib = lib;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}


	
	
	
	
	

}
