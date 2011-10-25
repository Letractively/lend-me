package com.lendme.entities;
/**
 * 
 * @author THE LENDERS
 *
 * @param <K>
 */
public interface InterestedOn<K> {

	public void warnInterestedThatTargetIsAvailable(K target, InterestedOn<K> interested) throws Exception;
	
}
