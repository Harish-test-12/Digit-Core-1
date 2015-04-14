/*
 * @(#)ModuleDao.java 3.0, 17 Jun, 2013 11:25:38 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.dao;

import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infstr.commons.Module;

public interface ModuleDao {
	Module getModuleByName(String moduleName);

	List<Module> getModuleInfoForRoleIds(Set<Role> roles);

	List<Module> getApplicationModuleByParentId(Integer parentId, Long userId);

	List<Module> getUserFavourites(Long userId);
}
