/*****************************************************************

This code was derived from the bookTrading example code supplied 
with the Jade distribution. Therefore the following copyright
from that example is included here.
=Peter Williams 24 May 2010

--- Original Copyright notice--

JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
 *****************************************************************/

package examples.ordering;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BuyerAgent extends Agent {
	// Order to place in format 'item1,number1;item2,price2;..'
	private String _orderString;
	// The list of known seller agents
	private AID[] sellerAgents;

	// Initialize the Agent
	protected void setup() {
		// Show startup on console
		System.out.println("Buyer agent " + getAID().getName() + " is starting.");

		// Read the order as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			String csvFilePath = (String)args[0];
			System.out.println(getAID().getName() + ") Reading order from '" + csvFilePath + "'");
			ItemList order = new ItemList(csvFilePath);
			_orderString = order.getAsString();
			System.out.println(getAID().getName() + ") Order string is '" + _orderString + "'");
			
			// Add a TickerBehaviour that schedules a request to seller agents every minute
			addBehaviour(new TickerBehaviour(this, 60000) {
				protected void onTick() {
					System.out.println(getAID().getName() + ") Trying to buy '" + _orderString + "'");
					// Update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("supplier");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template); 
						System.out.println(getAID().getName() + ") Found the following " + result.length + " supplier agents:");
						sellerAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							sellerAgents[i] = result[i].getName();
							System.out.println(sellerAgents[i].getName());
						}
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}

					// Perform the request
					myAgent.addBehaviour(new RequestPerformer());
				}
			} );
		}
		else {
			// Make the agent terminate
			System.out.println(getAID().getName() + ") No order CSV file path specified");
			doDelete();
		}
	}

	//  clean-up the agent
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Buyer-agent " + getAID().getName() + " terminating.");
	}

	/**
	   Inner class RequestPerformer.
	   This is the behaviour used by a Buyer Agents to ask all Supplier  
	   Agents if they can fulfill the orer.
	 */
	private class RequestPerformer extends Behaviour {
		private AID bestSeller;     // The Supplier agent who provides the best offer 
		private double bestPrice;   // The best offered price
		private int repliesCnt = 0; // Number of replies from Supplier agents
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;

		public void action() {
			switch (step) {
			case 0:
				// Send the cfp to all Suppliers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				} 
				cfp.setContent(_orderString);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all proposals/refusals from Supplier agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer 
						double price = Double.parseDouble(reply.getContent());
						if (bestSeller == null || price < bestPrice) {
							// This is the best offer at present
							bestPrice = price;
							bestSeller = reply.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						step = 2; 
					}
				}
				else {
					block();
				}
				break;
			case 2:
				// Send the purchase order to the seller that provided the best offer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(_orderString);
				order.setConversationId("book-trade");
				order.setReplyWith("order" + System.currentTimeMillis());
				myAgent.send(order);
				// Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				break;
			case 3:      
				// Receive the purchase order reply
				reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println(getAID().getName() + ") '" + _orderString + "' successfully purchased from agent " + reply.getSender().getName());
						System.out.println("Price = $" + bestPrice);
						myAgent.doDelete();
					}
					else {
						System.out.println(getAID().getName() + ") Attempt failed: Could not purchase '" + _orderString + "'");
					}

					step = 4;
				}
				else {
					block();
				}
				break;
			}        
		}

		public boolean done() {
			if (step == 2 && bestSeller == null) {
				System.out.println("Attempt failed: '" + _orderString + "' could not be fulfilled");
			}
			return ((step == 2 && bestSeller == null) || step == 4);
		}
	}  
}
