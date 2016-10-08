package nenov.cs4027.assessment.main;

import de.dailab.jiactng.agentcore.knowledge.IFact;

public class Confirmation implements IFact {
	private static final long serialVersionUID = 4998234033756440027L;

	private String companyName;
	private boolean isSuccessful;

	public Confirmation() {
	}

	public Confirmation(String companyName, boolean isSuccessful) {
		this.companyName = companyName;
		this.isSuccessful = isSuccessful;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
}
