package nenov.cs4027.assessment.tourismagency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nenov.cs4027.assessment.main.Confirmation;
import nenov.cs4027.assessment.main.Constants;
import nenov.cs4027.assessment.main.Offer;
import nenov.cs4027.assessment.main.Ticket;
import nenov.cs4027.assessment.main.TourismPackage;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.protocol.handler.fipacontractnet.ContractNetProtocolInitiator;
import de.dailab.jiactng.protocol.message.FipaPerformatives;
import de.dailab.jiactng.protocol.message.MessageParameters;

import com.google.common.collect.Sets;

public class TourismAgencyInitiatorCNP extends ContractNetProtocolInitiator {

	@Override
	protected List<IJiacMessage> handleAllResponses(List<IJiacMessage> responses) {
		log.info("TourismAgency: handling all responses");
		
		TourismAgency agency = (TourismAgency) this.businesslogicRef;
		Offer offer;
		IJiacMessage response;
		List<IJiacMessage> proposalAnswers = new ArrayList<IJiacMessage>();
		Set<Offer> ticketOffers = new HashSet<Offer>();
		Set<Offer> transportOffers = new HashSet<Offer>();
		Set<Offer> purchaseOffers = new HashSet<Offer>();

		for (int i = 0; i < responses.size(); i++) {
			if (FipaPerformatives.PROPOSE.equals(responses.get(i).getHeader(MessageParameters.FIPA_PERFORMATIVE))) {
				log.info("TourismAgency: received proposal");
				
				offer = (Offer) responses.get(i).getPayload();
				
				// offer must have positive capacity and price
				if (offer.getCapacity() >= 0 && offer.getPrice() >= 0) {
					if (offer.getType().equals(Constants.PURCHASE_OFFER)) {
						purchaseOffers.add(offer);
					} else if (offer.getType().equals(Constants.TICKET_OFFER)) {
						ticketOffers.add(offer);
					} else {
						transportOffers.add(offer);
					}
				} else {
					log.info("TourismAgency: rejecting offer " + offer.toString());
					proposalAnswers.add(createProposalAnswer(ResultType.REJECT, offer, responses.get(i)));
				}
				
			   /* CGS D: Solution
				* if (offer.getCapacity() >= agency.getMaxCapacity()) {
				*	 log.info("TourismAgency: accepting offer " + offer.toString());
				*	 proposalAnswers.add(createProposalAnswer(ResultType.ACCEPT, responses.get(i).getPayload(), responses.get(i)));
				* } else {
				*	 log.info("TourismAgency: rejecting offer " + offer.toString());
				*	 proposalAnswers.add(createProposalAnswer(ResultType.REJECT, responses.get(i).getPayload(), responses.get(i)));
				* }
				*/
			} else {
				log.info("TourismAgency: refused proposal");
			}
		}

		if (!purchaseOffers.isEmpty()) {
			for (Offer o : purchaseOffers) {
				response = null;
				for (IJiacMessage res : responses) {
					if (res.getPayload().equals(o)) {
						response = res;
					}
				}
				if (o.getCapacity() > 0 && agency.getExcessiveTransportTickets().size() > 0
						&& agency.getExcessiveTransportTickets().size() >= o.getCapacity()) {
					agency.removeExcessiveTransportTicket(o.getCapacity());
					proposalAnswers.add(createProposalAnswer(ResultType.ACCEPT, o, response));
				} else {
					proposalAnswers.add(createProposalAnswer(ResultType.REJECT, o, response));
				}
			}
			log.info("TourismAgency: excessive transport tickets left: " + agency.getExcessiveTransportTickets().size());
		} else if (!ticketOffers.isEmpty()) {
			Set<Offer> optimalOffers = fractionalKnapsack_CGS_A(agency.getMaxCapacity(), ticketOffers);
			log.info("TourismAgency: ticket offer/offers with minimal cost is/are: " + optimalOffers);

			proposalAnswers.addAll(acceptOrRejectOffers(ticketOffers, optimalOffers, responses, agency));
		} else {
			Set<Offer> optimalOffers = optimalSelection_CGS_C(agency.getMaxCapacity(), transportOffers);
			log.info("TourismAgency: transport offer/offers with minimal cost is/are: " + optimalOffers);

			proposalAnswers.addAll(acceptOrRejectOffers(transportOffers, optimalOffers, responses, agency));

			log.info("TourismAgency: tourism packages created: " + agency.getTourismPackages().size());
			log.info("TourismAgency: excessive transport tickets: " + agency.getExcessiveTransportTickets().size());
		}

		return proposalAnswers;
	}

	/*
	 * get the set of all offers and for each check if it is part of the set of optimal offers
	 * if true - accept the offer, otherwise reject it
	 */
	private List<IJiacMessage> acceptOrRejectOffers(Set<Offer> allOffers, Set<Offer> optimalOffers, List<IJiacMessage> responses, TourismAgency agency) {
		List<IJiacMessage> proposalAnswers = new ArrayList<IJiacMessage>();
		IJiacMessage response;
		
		for (Offer offer : allOffers) {
			response = null;
			for (IJiacMessage res : responses) {
				if (res.getPayload().equals(offer)) {
					response = res;
				}
			}

			if (optimalOffers.contains(offer)) {
				log.info("TourismAgency: accepting offer " + offer.toString());
				
				proposalAnswers.add(createProposalAnswer(ResultType.ACCEPT, offer, response));
				addTicketsToTourismPackage(offer, agency, offer.getType());
			} else {
				log.info("TourismAgency: rejecting offer " + offer.toString());
				
				proposalAnswers.add(createProposalAnswer(ResultType.REJECT, offer, response));
			}
		}

		return proposalAnswers;
	}

	/*
	 * for each ticket in a given offer, either create a new package and add it
	 * (if it is event ticket) or add it to existing package (if it is transport ticket)
	 */
	private void addTicketsToTourismPackage(Offer offer, TourismAgency agency, String type) {
		for (Ticket ticket : offer.getBoughtTickets()) {
			if (ticket.getType().equals(Constants.EVENT_TICKET)) {
				TourismPackage tourismPackage = new TourismPackage();
				tourismPackage.setEventTicket(ticket);
				agency.addTourismPackage(tourismPackage);
			} else {
				for (TourismPackage tPackage : agency.getTourismPackages()) {
					if (tPackage.getTransportTicket() == null) {
						tPackage.setTransportTicket(ticket);
						ticket.setPartOfPackage(true);
						break;
					}
				}
				if (!ticket.isPartOfPackage()) {
					agency.addExcessiveTransportTicket(ticket);
				}
			}
		}
	}

	private Set<Offer> fractionalKnapsack_CGS_A(int neededCapacity, Set<Offer> offers) {
		Set<Offer> optimalSet = new HashSet<Offer>();
		List<Offer> offersList = new ArrayList<Offer>();

		offersList.addAll(offers);
		Collections.sort(offersList, new Comparator<Offer>() {
			@Override
			public int compare(Offer o1, Offer o2) {
				return ((Float) (o1.getPrice() / o1.getCapacity())).compareTo((Float) (o2.getPrice() / o2.getCapacity()));
			}
		});

		for (int i = 0; i < offersList.size() && neededCapacity > 0; i++) {
			if (offersList.get(i).getCapacity() <= neededCapacity) {
				offersList.get(i).removeTickets(offersList.get(i).getAvailableTickets().size());
				optimalSet.add(offersList.get(i));
				neededCapacity -= offersList.get(i).getBoughtTickets().size();
			} else {
				offersList.get(i).removeTickets(neededCapacity);
				optimalSet.add(offersList.get(i));
				break;
			}
		}
		return optimalSet;
	}

	private Set<Offer> optimalSelection_CGS_C(int maxCapacity, Set<Offer> offers) {
		int minimumPrice = Integer.MAX_VALUE;
		int localCapacity = 0;
		int localPrice = 0;
		Set<Offer> localSet = new HashSet<Offer>();
		Set<Offer> optimalSet = new HashSet<Offer>();
		Set<Set<Offer>> allCombinations = Sets.powerSet(offers);

		Iterator<Set<Offer>> iter = allCombinations.iterator();
		while (iter.hasNext()) {
			Set<Offer> currentOffer = iter.next();

			Iterator<Offer> iterator = currentOffer.iterator();
			while (iterator.hasNext()) {
				Offer offer = iterator.next();
				localSet.add(offer);
			}
			if (localSet.size() > 0) {
				for (Offer offer : localSet) {
					localCapacity += offer.getCapacity();
					localPrice += offer.getPrice();
				}

				if (localCapacity >= maxCapacity && localPrice < minimumPrice) {
					optimalSet = localSet;
					minimumPrice = localPrice;
				}
			}

			localCapacity = 0;
			localPrice = 0;
			localSet = new HashSet<Offer>();
		}

		Iterator<Offer> iterator = optimalSet.iterator();
		while (iterator.hasNext()) {
			Offer offer = iterator.next();
			offer.removeTickets(offer.getAvailableTickets().size());
		}

		return optimalSet;
	}

	@Override
	protected List<IJiacMessage> handleAllResultNotifications(List<IJiacMessage> results) {
		log.info("TourismAgency: handling all results");
		for (IJiacMessage result : results) {
			Confirmation confirmation = (Confirmation) result.getPayload();
			if (confirmation != null) {
				log.info("TourismAgency: result from " + confirmation.getCompanyName() + " is " + result.getHeader(MessageParameters.FIPA_PERFORMATIVE));
			}
		}

		return null;
	}
}
