package examples.peter;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class PeterService extends Agent {
	public PeterService() { }
	
	protected void setup() {
		Object[] args = getArguments();
		System.out.println("Service name : " + args[0].toString());
		registerService(args[0].toString());
	}
	
	private void registerService(String service) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		sd.setName(service);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
