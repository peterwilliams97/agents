package examples.peter;

import jade.core.Agent;

/*
 * This will be built from 2 directories above this one this file is in
 */

public class PeterAgent extends Agent {

 // Needed ?
  private static final long serialVersionUID = 1L;

  protected void setup() {
  	System.out.println("Hello from Peter! My name is " + getLocalName());
  	
  	// Make this agent terminate
  	doDelete();
  } 

    // Allows simple check of class being well-formed
	public static void main(String[] args)  {
 		System.out.println("Main !!!");
	}
}

