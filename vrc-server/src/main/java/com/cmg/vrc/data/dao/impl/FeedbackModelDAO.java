/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.FeedbackModel;
import com.cmg.vrc.data.jdo.FeedbackModelJDO;

/**
 * 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
 */
public class FeedbackModelDAO extends DataAccess<FeedbackModelJDO , FeedbackModel> {

	public FeedbackModelDAO() {
		super(FeedbackModelJDO.class, FeedbackModel.class);
	}
}
