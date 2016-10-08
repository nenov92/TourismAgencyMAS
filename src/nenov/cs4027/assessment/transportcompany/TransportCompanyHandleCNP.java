package nenov.cs4027.assessment.transportcompany;

import nenov.cs4027.assessment.main.Confirmation;
import nenov.cs4027.assessment.main.Offer;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.protocol.handler.fipacontractnet.ContractNetProtocolParticipant;

public class TransportCompanyHandleCNP extends ContractNetProtocolParticipant {

	private TransportCompany transportCompany;

	@Override
	protected IJiacMessage handleCallForProposal(IJiacMessage message) {
		transportCompany = (TransportCompany) this.businesslogicRef;
		Offer answer = transportCompany.getOffer((Offer) message.getPayload());
		log.info("Transport Company " + transportCompany.getCompanyName() + ": Sending Offer");

		// wait for 1 second so that all ticketing offers are handled by the tourism agency 
		// and tourism packages have been created
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return createCfpAnswer(ResponseType.PROPOSE, answer, message);
	}

	@Override
	protected IJiacMessage handleAcceptProposal(IJiacMessage message) {
		transportCompany = (TransportCompany) this.businesslogicRef;
		Confirmation confirmation = new Confirmation(transportCompany.getCompanyName(), true);
		log.info("Transport Company " + transportCompany.getCompanyName() + ": Offer has been accepted, sending result");

		return createResultNotification(ResultType.INFORM, confirmation);
	}

	@Override
	protected IJiacMessage handleRejectProposal(IJiacMessage message) {
		transportCompany = (TransportCompany) this.businesslogicRef;
		log.info("Transport Company " + transportCompany.getCompanyName() + ": Offer has been rejected");

		return null;
	}
}
