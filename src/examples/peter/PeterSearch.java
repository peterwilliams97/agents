package examples.peter;

import java.util.Iterator;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PeterSearch extends Agent {
	public PeterSearch() { }
	
	protected void setup() {
		searchServiceAgent();
	}
	private void searchServiceAgent() {
		DFAgentDescription dfd = new DFAgentDescription();
		try	{
			DFAgentDescription[] services = DFService.search(this, dfd);
			for (int i = 0; i < services.length; i++){
				String output = services[i].getName() + "provides : ";
				Iterator serviceList = services[i].getAllServices();
				while(serviceList.hasNext()){
					ServiceDescription sd = (ServiceDescription)
					serviceList.next();
					output += " " + sd.getName();
				}
				System.out.println("Available Services : " + output);
			}
		} catch(Exception e)	{
			e.printStackTrace();
		}
	}
}