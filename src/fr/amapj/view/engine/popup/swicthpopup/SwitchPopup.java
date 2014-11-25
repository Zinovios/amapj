package fr.amapj.view.engine.popup.swicthpopup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Permet de créer un popup avec une liste de choix, qui ménera ensuite 
 * à un autre popup 
 */
@SuppressWarnings("serial")
abstract public class SwitchPopup extends CorePopup
{	
	
	private OptionGroup group;
	
	private int index;
	
	private List<SwitchPopupInfo> infos = new ArrayList<>();
	
	protected String line1;
	
	
	static public class SwitchPopupInfo
	{
		public String lib;
		public CorePopup popup;
		
		public SwitchPopupInfo(String lib, CorePopup popup)
		{
			this.lib = lib;
			this.popup = popup;
		}
		
		
	}
	
	protected void createContent(VerticalLayout contentLayout)
	{
		loadFollowingPopups();
		
		group = new OptionGroup(line1);
		for (SwitchPopupInfo info : infos)
		{
			group.addItem(info.lib);
		}
		
		contentLayout.addComponent(group);
	}
	
	abstract protected void loadFollowingPopups();
	
	
	protected void addLine(String lib,CorePopup popup)
	{
		infos.add(new SwitchPopupInfo(lib,popup));
	}
	

	protected void createButtonBar()
	{
		addDefaultButton("Continuer ...", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleContinuer();
			}
		});
				
		
		addButton("Annuler", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
	}
	
	
	protected void handleAnnuler()
	{
		close();
	}

	protected void handleContinuer()
	{
		index = ((Container.Indexed) group.getContainerDataSource()).indexOfId(group.getValue());
		
		if (index==-1)
		{
			close();
			return;
		}
		
		
		removeCloseListener(popupCloseListener);
		addCloseListener(new CloseListener()
		{
			@Override
			public void windowClose(CloseEvent e)
			{
				swithToNextPopup();
			}
		});
		close();
	}

	protected void swithToNextPopup()
	{
		SwitchPopupInfo info = infos.get(index);
		info.popup.addCloseListener(popupCloseListener);
		CorePopup.open(info.popup);
	}
	
	
		
		
	
	
}
