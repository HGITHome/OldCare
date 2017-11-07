package com.dgut.core.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.dgut.common.email.EmailSender;
import com.dgut.common.email.MessageTemplate;
import com.dgut.core.entity.base.BaseConfig;
import com.dgut.core.entity.base.BaseMessage;

public class Message extends BaseMessage {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Message() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Message(java.lang.String id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	

}