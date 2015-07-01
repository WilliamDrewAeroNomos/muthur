/**
 * 
 */
package com.csc.muthur.server.model;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationExecutionModel {

	private String name;
	private String description;
	private String fedExecModelUUID;
	private long logicalStartTimeMSecs;
	private boolean autoStart = true;
	private long defaultDurationWithinStartupProtocolMSecs;
	private long durationFederationExecutionMSecs;
	private long durationJoinFederationMSecs;
	private long durationRegisterPublicationMSecs;
	private long durationRegisterSubscriptionMSecs;
	private long durationRegisterToRunMSecs;
	private long durationWaitForStartFederationDirectiveMSecs;
	private long durationTimeToWaitAfterTerminationMSecs;

	private Set<String> namesOfRequiredFederates = new TreeSet<String>();

	/**
	 * @param name
	 * @throws MuthurException
	 */
	public FederationExecutionModel(String name) throws MuthurException {
		super();
		if (name == null) {
			throw new MuthurException(
					"Error constructing FederationExecutionModel. Name parameter was null");
		}
		this.name = name;
		fedExecModelUUID = UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @param fem
	 */
	public FederationExecutionModel(final FederationExecutionModel fem) {
		super();

		this.name = new String(fem.name);

		if (fem.description != null) {
			this.description = new String(fem.description);
		}

		if (fem.fedExecModelUUID != null) {
			this.fedExecModelUUID = new String(fem.fedExecModelUUID);
		}

		this.logicalStartTimeMSecs = fem.logicalStartTimeMSecs;
		this.autoStart = fem.autoStart;
		this.defaultDurationWithinStartupProtocolMSecs =
				fem.defaultDurationWithinStartupProtocolMSecs;
		this.durationFederationExecutionMSecs =
				fem.durationFederationExecutionMSecs;
		this.durationJoinFederationMSecs = fem.durationJoinFederationMSecs;
		this.durationRegisterPublicationMSecs =
				fem.durationRegisterPublicationMSecs;
		this.durationRegisterSubscriptionMSecs =
				fem.durationRegisterSubscriptionMSecs;
		this.durationRegisterToRunMSecs = fem.durationRegisterToRunMSecs;
		this.durationWaitForStartFederationDirectiveMSecs =
				fem.durationWaitForStartFederationDirectiveMSecs;
		this.durationTimeToWaitAfterTerminationMSecs =
				fem.durationTimeToWaitAfterTerminationMSecs;

		setNamesOfRequiredFederates(fem.namesOfRequiredFederates);

	}

	/**
	 * 
	 */
	public FederationExecutionModel() {
		super();
	}

	/**
	 * 
	 * @param federateName
	 */
	public void addRequiredFededrate(final String federateName) {
		if (federateName != null) {
			namesOfRequiredFederates.add(federateName);
		}
	}

	/**
	 * 
	 * @param federateName
	 */
	public void removeRequiredFederate(final String federateName) {
		if (federateName != null) {
			namesOfRequiredFederates.remove(federateName);
		}
	}

	/**
	 * @return Returns the description.
	 */
	public final String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *          The description to set.
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the namesOfRequiredFederates.
	 */
	public final Set<String> getNamesOfRequiredFederates() {
		return this.namesOfRequiredFederates;
	}

	/**
	 * @param namesOfRequiredFederates
	 *          The namesOfRequiredFederates to set.
	 */
	public final void setNamesOfRequiredFederates(
			Set<String> namesOfRequiredFederates) {

		if (namesOfRequiredFederates != null) {
			this.namesOfRequiredFederates =
					new TreeSet<String>(namesOfRequiredFederates);
		}
	}

	/**
	 * @return Returns the name.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *          The name to set.
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the fedExecModelUUID.
	 */
	public final String getFedExecModelUUID() {
		return this.fedExecModelUUID;
	}

	/**
	 * @param fedExecModelUUID
	 *          The fedExecModelUUID to set.
	 */
	public final void setFedExecModelUUID(String fedExecModelUUID) {
		this.fedExecModelUUID = fedExecModelUUID;
	}

	/**
	 * @return the logicalStartTimeMSecs
	 */
	public final long getLogicalStartTimeMSecs() {
		return logicalStartTimeMSecs;
	}

	/**
	 * @param logicalStartTimeMSecs
	 *          the logicalStartTimeMSecs to set
	 */
	public final void setLogicalStartTimeMSecs(long logicalStartTimeMSecs) {
		this.logicalStartTimeMSecs = logicalStartTimeMSecs;
	}

	/**
	 * If true, federation execution is initiated immediately after all ready to
	 * run requests are received from all required federates. If false then the
	 * federation will begin only after a start federation request is sent to
	 * Muthur.
	 * 
	 * @param autoStart
	 *          Set to true to initiate the execution of a federation directly
	 *          after the read to run step or false to wait after the ready to run
	 *          requests are received and until a start federate request is
	 *          received by Muthur. Default is true.
	 * 
	 * @see #isAutoStart()
	 */
	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	/**
	 * If true federation execution is initiated immediately after all ready to
	 * run requests are received from all required federates. If false then the
	 * federation will begin only after a start federation request is sent to
	 * Muthur.
	 * 
	 * @return Current value of autoStart.
	 * 
	 * @see #setAutoStart(boolean)
	 */
	public boolean isAutoStart() {
		return autoStart;
	}

	/**
	 * @param defaultDurationWithinStartupProtocolMSecs
	 *          the defaultDurationWithinStartupProtocolMSecs to set
	 */
	public void setDefaultDurationWithinStartupProtocolMSecs(
			long defaultDurationWithinStartupProtocolMSecs) {
		this.defaultDurationWithinStartupProtocolMSecs =
				defaultDurationWithinStartupProtocolMSecs;
	}

	/**
	 * @return the defaultDurationWithinStartupProtocolMSecs
	 */
	public long getDefaultDurationWithinStartupProtocolMSecs() {
		return defaultDurationWithinStartupProtocolMSecs;
	}

	/**
	 * @return the durationFederationExecutionMSecs
	 */
	public final long getDurationFederationExecutionMSecs() {
		return durationFederationExecutionMSecs;
	}

	/**
	 * @param durationFederationExecutionMSecs
	 *          the durationFederationExecutionMSecs to set
	 */
	public final void setDurationFederationExecutionMSecs(
			long durationFederationExecutionMSecs) {
		this.durationFederationExecutionMSecs = durationFederationExecutionMSecs;
	}

	/**
	 * @return the durationJoinFederationMSecs
	 */
	public final long getDurationJoinFederationMSecs() {
		return durationJoinFederationMSecs;
	}

	/**
	 * @param durationJoinFederationMSecs
	 *          the durationJoinFederationMSecs to set
	 */
	public final void setDurationJoinFederationMSecs(
			long durationJoinFederationMSecs) {
		this.durationJoinFederationMSecs = durationJoinFederationMSecs;
	}

	/**
	 * @return the durationRegisterPublicationMSecs
	 */
	public final long getDurationRegisterPublicationMSecs() {
		return durationRegisterPublicationMSecs;
	}

	/**
	 * @param durationRegisterPublicationMSecs
	 *          the durationRegisterPublicationMSecs to set
	 */
	public final void setDurationRegisterPublicationMSecs(
			long durationRegisterPublicationMSecs) {
		this.durationRegisterPublicationMSecs = durationRegisterPublicationMSecs;
	}

	/**
	 * @return the durationRegisterSubscriptionMSecs
	 */
	public final long getDurationRegisterSubscriptionMSecs() {
		return durationRegisterSubscriptionMSecs;
	}

	/**
	 * @param durationRegisterSubscriptionMSecs
	 *          the durationRegisterSubscriptionMSecs to set
	 */
	public final void setDurationRegisterSubscriptionMSecs(
			long durationRegisterSubscriptionMSecs) {
		this.durationRegisterSubscriptionMSecs = durationRegisterSubscriptionMSecs;
	}

	/**
	 * @return the durationRegisterToRunMSecs
	 */
	public final long getDurationRegisterToRunMSecs() {
		return durationRegisterToRunMSecs;
	}

	/**
	 * @param durationRegisterToRunMSecs
	 *          the durationRegisterToRunMSecs to set
	 */
	public final void setDurationRegisterToRunMSecs(
			long durationRegisterToRunMSecs) {
		this.durationRegisterToRunMSecs = durationRegisterToRunMSecs;
	}

	/**
	 * @return the durationTimeToWaitAfterTerminationMSecs
	 */
	public final long getDurationTimeToWaitAfterTerminationMSecs() {
		return durationTimeToWaitAfterTerminationMSecs;
	}

	/**
	 * @param durationTimeToWaitAfterTerminationMSecs
	 *          the durationTimeToWaitAfterTerminationMSecs to set
	 */
	public final void setDurationTimeToWaitAfterTerminationMSecs(
			long durationTimeToWaitAfterTerminationMSecs) {
		this.durationTimeToWaitAfterTerminationMSecs =
				durationTimeToWaitAfterTerminationMSecs;
	}

	/**
	 * 
	 * @param federateName
	 *          Name of the federate to search for in the list of required
	 *          federates for this {@link FederationExecutionModel}
	 * @return True if federateName is found in the list of required federates for
	 *         this {@link FederationExecutionModel}
	 */
	public final boolean isRequired(final String federateName) {
		return namesOfRequiredFederates.contains(federateName);
	}

	/**
	 * @param durationWaitForStartFederationDirectiveMSecs
	 *          the durationWaitForStartFederationDirectiveMSecs to set
	 */
	public void setDurationWaitForStartFederationDirectiveMSecs(
			long durationWaitForStartFederationDirectiveMSecs) {
		this.durationWaitForStartFederationDirectiveMSecs =
				durationWaitForStartFederationDirectiveMSecs;
	}

	/**
	 * @return the durationWaitForStartFederationDirectiveMSecs
	 */
	public long getDurationWaitForStartFederationDirectiveMSecs() {
		return durationWaitForStartFederationDirectiveMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FederationExecutionModel other = (FederationExecutionModel) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FederationExecutionModel [name=" + name + ", description="
				+ description + ", fedExecModelUUID=" + fedExecModelUUID
				+ ", logicalStartTimeMSecs=" + logicalStartTimeMSecs + ", autoStart="
				+ autoStart + ", defaultDurationWithinStartupProtocolMSecs="
				+ defaultDurationWithinStartupProtocolMSecs
				+ ", durationFederationExecutionMSecs="
				+ durationFederationExecutionMSecs + ", durationJoinFederationMSecs="
				+ durationJoinFederationMSecs + ", durationRegisterPublicationMSecs="
				+ durationRegisterPublicationMSecs
				+ ", durationRegisterSubscriptionMSecs="
				+ durationRegisterSubscriptionMSecs + ", durationRegisterToRunMSecs="
				+ durationRegisterToRunMSecs
				+ ", durationTimeToWaitAfterTerminationMSecs="
				+ durationTimeToWaitAfterTerminationMSecs + ", fedExecModelUUID="
				+ fedExecModelUUID + ", logicalStartTimeMSecs=" + logicalStartTimeMSecs
				+ "]";
	}

}
