package fr.amapj.view.engine.grid;


/**
 * Object immatble contenant une position i,j
 *  
 *
 */
public class GridIJData
{
	private int i;
	private int j;
	
	
	public GridIJData(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	public int i()
	{
		return i;
	}
		
	public int j()
	{
		return j;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		GridIJData o = (GridIJData) obj;
		return ( (o.i==i) && (o.j==j));
	}

	@Override
	public int hashCode()
	{
		return i+107*j;
	}
	
}
