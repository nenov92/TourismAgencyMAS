package nenov.cs4027.assessment.tourismagency;

import nenov.cs4027.assessment.main.Confirmation;
import nenov.cs4027.assessment.main.Constants;
import nenov.cs4027.assessment.main.Offer;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.protocol.handler.fipacontractnet.ContractNetProtocolParticipant;

public class TourismAgencyParticipantCNP extends ContractNetProtocolParticipant {

	private TourismAgency tourismAgency;

	@Override
	protected IJiacMessage handleCallForProposal(IJiacMessage message) {
		tourismAgency = (TourismAgency) this.businesslogicRef;
		Offer answer = new Offer();
		answer.setType(Constants.PURCHASE_OFFER);
		answer.setCapacity(tourismAgency.getMaxCapacity());
		answer.setCompanyName(tourismAgency.getName());
		log.info("Tourism Agency " + tourismAgency.getName() + ": Sending Offer");

		// wait for 3 seconds so that all ticketing and transport offers are handled by the tourism agency 
		// and excessive transport tickets have been calculated
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return createCfpAnswer(ResponseType.PROPOSE, answer, message);
	}

	@Override
	protected IJiacMessage handleAcceptProposal(IJiacMessage message) {
		tourismAgency = (TourismAgency) this.businesslogicRef;
		Confirmation confirmation = new Confirmation(tourismAgency.getName(), true);
		log.info("Tourism Agency " + tourismAgency.getName() + ": Offer has been accepted, sending result");
		
		return createResultNotification(ResultType.INFORM, confirmation);
	}

	@Override
	protected IJiacMessage handleRejectProposal(IJiacMessage message) {
		tourismAgency = (TourismAgency) this.businesslogicRef;
		log.info("Tourism Agency " + tourismAgency.getName() + ": Offer has been rejected");
		
		return null;
	}
}
