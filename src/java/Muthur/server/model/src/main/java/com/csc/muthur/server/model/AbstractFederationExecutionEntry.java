/**
 * 
 */
package com.csc.muthur.server.model;

import javax.jms.Message;

import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * @author <a href=mailto:Nexsim@csc.com>Nexsim</a>
 * @version $Revision$
 */
public abstract class AbstractFederationExecutionEntry implements
		FederationExecutionEntry {

	private FederationExecutionModel federationExecutionModel;
	private String federateName;
	private Message message;
	private IEvent iEvent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.FederationExecutionEntry#getEvent()
	 */
	@Override
	public final IEvent getEvent() {
		return iEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.FederationExecutionEntry#getFederateName()
	 */
	@Override
	public final String getFederateName() {
		return federateName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.FederationExecutionEntry#getMessage()
	 */
	@Override
	public final Message getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.FederationExecutionEntry#getFederationExecutionModel()
	 */
	public final FederationExecutionModel getFederationExecutionModel() {
		return federationExecutionModel;
	}

	/**
	 * @return the iEvent
	 */
	public final IEvent getiEvent() {
		return iEvent;
	}

	/**
	 * @param iEvent
	 *          the iEvent to set
	 */
	public final void setiEvent(IEvent iEvent) {
		this.iEvent = iEvent;
	}

	/**
	 * @param federationExecutionModel
	 *          the federationExecutionModel to set
	 */
	public final void setFederationExecutionModel(
			FederationExecutionModel federationExecutionModel) {
		this.federationExecutionModel = federationExecutionModel;
	}

	/**
	 * @param federateName
	 *          the federateName to set
	 */
	public final void setFederateName(String federateName) {
		this.federateName = federateName;
	}

	/**
	 * @param message
	 *          the message to set
	 */
	public final void setMessage(Message message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		super.hashCode();
		final int prime = 31;
		int result = 1;
		result =
				prime * result + ((federateName == null) ? 0 : federateName.hashCode());
		result =
				prime
						* result
						+ ((federationExecutionModel == null) ? 0
								: federationExecutionModel.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractFederationExecutionEntry other =
				(AbstractFederationExecutionEntry) obj;
		if (federateName == null) {
			if (other.federateName != null) {
				return false;
			}
		} else if (!federateName.equals(other.federateName)) {
			return false;
		}
		if (federationExecutionModel == null) {
			if (other.federationExecutionModel != null) {
				return false;
			}
		} else if (!federationExecutionModel.equals(other.federationExecutionModel)) {
			return false;
		}
		return true;
	}

}
