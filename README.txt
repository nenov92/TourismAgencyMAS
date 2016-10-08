***********************************
* Instructions to run the project *
***********************************

I. To import the project from the archive file in Eclipse, please follow these steps.

	1. Go to File -> Import...
	2. Select Existing Projects into Workspace
	3. Click the radio button next to Select archive file and click the Browse button on the following dialog.
	4. Find the archive file on your hard disk. Click Open to select it.
	5. The project name will appear in the box below, already checked. Click Finish to perform the import.

II. To run the project, please select the NodeStarter.java in nenov.cs4027.assessment.main package and run it.
    Also, make sure that the following libraries are added to the Build Path: agentCore-5.1.5-jar-with-dependencies.jar, 
    protocol-5.1.5_01.jar and guava-18.0.jar.


***********************************
*          Configurations         *
***********************************

Òicketing companies have capacity and price properties which can be configured in resources/agents/TicketingCompanies.xml

Transport companies have capacity and price properties which can be configured in resources/agents/TransportCompanies.xml

Tourism agencies can be of two types - participant and initiator tourism agencies.
Both have maxCapacity property that represents the needed number of tickets they want to buy.
The property can be configured in resources/agents/TourismAgency.xml

You can add new ticketing and transport companies or participating tourism agency as beans in the corresponding XML file.
Finally, add the new agent in agents/ContractNetNode.xml


***********************************
*             Solution            *
***********************************

I. Functionality

1. The project can run with any number of participating tourism agencies and ticketing and transport companies. 
   However, it must have only one initiator tourism agency.
2. Ticketing company offers are selected according to the minimum price per ticket (as specified in CGS A section) with the fractionalKnapsack_CGS_A() method.
   The bought tickets and available tickets are recalculated and the tourism agency informs the ticketing company if any available tickets are left.
3. Transport company offers are selected according to the price of the whole offer (as specified in CGS C section) with the optimalSelection_CGS_C() method.
   To get the poweset of all possible combinations I used the Sets.powerSet() method that is implemented in the Guava library developed Google.
4. When the initiator tourism agency buys tickets it adds them to packages and it can be left with excessive transport tickets.
5. The initiator initiator tourism agency will then try to sell its excessive transport tickets to other participating tourism agencies.
6. To sell its excessive tickets the initiator tourism agency must provide the exact number of tickets wanted by the participating tourism agency.

II. Implementation decisions

1. To describe the whole ontology of the problem I have created Offer which has capacity, total price, available/bought tickets and type (ticket/transport/purchase).
2. A ticket is the single ticket within an Offer. Every ticket has a unique ticket number generated for it.
3. Tourism package which contains both event and transport tickets.
4. Confirmation which is sent to the initiator tourism agency by participating agent whenever a proposal is successfully completed.

