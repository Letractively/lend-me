package com.lendme.server;

import java.io.Serializable;

public enum ItemState implements Serializable {
	LENT, AVAILABLE, RETURNED, ASKED_FOR_RETURN,
	UNAVAILABLE, INTERESTED, REQUESTED;
}