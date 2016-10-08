package nenov.cs4027.assessment.ticketingcompany;

import nenov.cs4027.assessment.main.Confirmation;
import nenov.cs4027.assessment.main.Offer;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.protocol.handler.fipacontractnet.ContractNetProtocolParticipant;

public class TicketingCompanyHandleCNP extends ContractNetProtocolParticipant {
	
	private TicketingCompany ticketingCompany;
	
	@Override
	protected IJiacMessage handleCallForProposal(IJiacMessage message) {
		ticketingCompany = (TicketingCompany) this.businesslogicRef;
		Offer answer = ticketingCompany.getOffer((Offer) message.getPayload());
		log.info("Ticketing Company " + ticketingCompany.getCompanyName() + ": Sending Offer");
		
		return createCfpAnswer(ResponseType.PROPOSE, answer, message);
	}

	@Override
	protected IJiacMessage handleAcceptProposal(IJiacMessage message) {
		ticketingCompany = (TicketingCompany) this.businesslogicRef;
		Confirmation confirmation = new Confirmation(ticketingCompany.getCompanyName(), true);
		log.info("Ticketing Company " + ticketingCompany.getCompanyName() + ": Offer has been accepted, sending result");

		return createResultNotification(ResultType.INFORM, confirmation);
	}

	@Override
	protected IJiacMessage handleRejectProposal(IJiacMessage message) {
		ticketingCompany = (TicketingCompany) this.businesslogicRef;
		log.info("Ticketing Company " + ticketingCompany.getCompanyName() + ": Offer has been rejected");

		return null;
	}
}
