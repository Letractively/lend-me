package com.lendme.entities;

public interface InterestedOn<K> {

	public void warnInterestedThatTargetIsAvailable(K target, InterestedOn<K> interested) throws Exception;
	
}
