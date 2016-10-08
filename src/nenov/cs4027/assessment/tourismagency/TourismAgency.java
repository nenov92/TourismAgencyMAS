package nenov.cs4027.assessment.tourismagency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nenov.cs4027.assessment.main.Constants;
import nenov.cs4027.assessment.main.Offer;
import nenov.cs4027.assessment.main.Ticket;
import nenov.cs4027.assessment.main.TourismPackage;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.comm.ICommunicationAddress;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;
import de.dailab.jiactng.protocol.ProtocolManagementBean;
import de.dailab.jiactng.protocol.handler.fipacontractnet.ContractNetProtocolInitiator;

public class TourismAgency extends AbstractAgentBean {

	private ContractNetProtocolInitiator cnp;
	private ProtocolManagementBean protocolManager;

	private int maxCapacity;
	private String name;
	private List<TourismPackage> tourismPackages = new ArrayList<TourismPackage>();
	private Set<Ticket> excessiveTransportTickets = new HashSet<Ticket>();

	@Override
	public void doStart() {
		if (!this.beanName.startsWith(Constants.TOURISM_AGENCY_BEAN)) {
			protocolManager = thisAgent.findAgentBean(ProtocolManagementBean.class);
			this.execute();
		}
	}

	@Override
	public void execute() {
		List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

		List<ICommunicationAddress> ticketingCompaies = new ArrayList<ICommunicationAddress>();
		List<ICommunicationAddress> transportCompaies = new ArrayList<ICommunicationAddress>();
		List<ICommunicationAddress> tourismAgencies = new ArrayList<ICommunicationAddress>();

		for (IAgentDescription agent : agentDescriptions) {
			if (agent.getName().startsWith(Constants.TICKET_COMPANY)) {
				ticketingCompaies.add(agent.getMessageBoxAddress());
			} else if (agent.getName().startsWith(Constants.TRANSPORT_COMPANY)) {
				transportCompaies.add(agent.getMessageBoxAddress());
			} else if (agent.getName().startsWith(Constants.TOURISM_AGENCY)) {
				tourismAgencies.add(agent.getMessageBoxAddress());
			}
		}

		if (ticketingCompaies.size() == 0 && transportCompaies.size() == 0) {
			log.info("No ticketing or transport companies found!");
			return;
		}

		cnp = (ContractNetProtocolInitiator) protocolManager.getInitiator(Constants.PROTOCOL_NAME);
		log.info("TourismAgency: Starting CNP with " + ticketingCompaies.size() + " ticketing companies.");
		cnp.startProtocol(ticketingCompaies, new Offer());

		cnp = (ContractNetProtocolInitiator) protocolManager.getInitiator(Constants.PROTOCOL_NAME);
		log.info("TourismAgency: Starting CNP with " + transportCompaies.size() + " transport companies.");
		cnp.startProtocol(transportCompaies, new Offer());

		cnp = (ContractNetProtocolInitiator) protocolManager.getInitiator(Constants.PROTOCOL_NAME);
		log.info("TourismAgency: Starting CNP with " + tourismAgencies.size() + " participating tourism agencies.");
		cnp.startProtocol(tourismAgencies, new Offer());
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TourismPackage> getTourismPackages() {
		return tourismPackages;
	}

	public void setTourismPackages(List<TourismPackage> tourismPackage) {
		this.tourismPackages = tourismPackage;
	}

	public void addTourismPackage(TourismPackage tourismPackage) {
		this.tourismPackages.add(tourismPackage);
	}

	public Set<Ticket> getExcessiveTransportTickets() {
		return excessiveTransportTickets;
	}

	public void setExcessiveTransportTickets(Set<Ticket> excessiveTransportTickets) {
		this.excessiveTransportTickets = excessiveTransportTickets;
	}

	public void addExcessiveTransportTicket(Ticket ticket) {
		this.excessiveTransportTickets.add(ticket);
	}

	public void removeExcessiveTransportTicket(int numberOfTickets) {
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.addAll(this.excessiveTransportTickets);
		int listSize = tickets.size();
		for (int i = listSize - 1; i >= listSize - numberOfTickets; i--) {
			this.excessiveTransportTickets.remove(tickets.get(i));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tourism agency: ");
		sb.append(getName());
		sb.append("; Capacity needed: ");
		sb.append(getMaxCapacity());
		sb.append("; Tourism packages created: ");
		sb.append(getTourismPackages().size());
		sb.append("; Excessive transport tickets: ");
		sb.append(getExcessiveTransportTickets().size());
		return sb.toString();
	}

}
