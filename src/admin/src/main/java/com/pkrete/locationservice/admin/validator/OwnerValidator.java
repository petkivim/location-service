/**
 * This file is part of Location Service :: Admin.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.validator;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.OwnersService;
import com.pkrete.locationservice.admin.util.WebUtil;
import java.util.Map;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The {@link OwnerValidator OwnerValidator} class validates the values
 * inserted via addowner and editowner forms. This class defines the
 * obligatory fields and returns an error message associated with the field
 * when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class OwnerValidator implements Validator {

    private OwnersService ownersService;
    private ConverterService converterService;

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setOwnersService(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Owner.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Owner owner = (Owner) target;

        if (owner.getCode() == null || owner.getCode().isEmpty()) {
            errors.rejectValue("code", "error.owner.code.required");
        }

        if (owner.getName() == null || owner.getName().isEmpty()) {
            errors.rejectValue("name", "error.owner.name.required");
        }

        if (owner.getColor() == null || owner.getColor().isEmpty()) {
            errors.rejectValue("color", "error.owner.color.required");
        }

        if (owner.getOpacity() == null || owner.getOpacity().isEmpty()) {
            errors.rejectValue("opacity", "error.owner.opacity.required");
        }

        if (owner.getAllowedIPs() == null) {
            errors.rejectValue("allowedIPs", "error.owner.ips.null");
        } else {
            if (owner.getAllowedIPs().length() > 500) {
                errors.rejectValue("allowedIPs", "error.owner.ips.length");
            }
            if (!owner.getAllowedIPs().matches("[\\s\\d\\.\\*]*")) {
                errors.rejectValue("allowedIPs", "error.owner.ips.format");
            }
        }

        if (owner.getLocatingStrategy() == null) {
            errors.rejectValue("locatingStrategy", "error.owner.strategy.null");
        }

        if (owner.getCode().length() > 10) {
            errors.rejectValue("code", "error.owner.code.length");
        }

        if (owner.getName().length() > 100) {
            errors.rejectValue("name", "error.owner.name.length");
        }

        if (owner.getOpacity().length() > 3) {
            errors.rejectValue("opacity", "error.owner.opacity.length");
        }

        if (!owner.getCode().isEmpty() && !owner.getCode().matches("^[A-Za-z0-9\\-]+$")) {
            errors.rejectValue("code", "error.owner.code.format");
        }

        if (owner.getNotFoundRedirects() != null) {
            Map<Integer, Boolean> ids = this.ownersService.getNotFoundRedirectIds(owner.getId());
            for (int i = 0; i < owner.getNotFoundRedirects().size(); i++) {
                if (owner.getNotFoundRedirects().get(i).getCondition().length() > 200) {
                    errors.rejectValue("notFoundRedirects[" + i + "].condition", "error.owner.modifications.condition.length");
                }
                if (owner.getNotFoundRedirects().get(i).getOperation().length() > 200) {
                    errors.rejectValue("notFoundRedirects[" + i + "].operation", "error.owner.modifications.operation.length");
                }
                if (owner.getNotFoundRedirects().get(i).getId() > 0 && !ids.containsKey(owner.getNotFoundRedirects().get(i).getId())) {
                    errors.rejectValue("notFoundRedirects[" + i + "].id", "error.owner.modifications.id");
                }
            }
        }

        if (owner.getPreprocessingRedirects() != null) {
            Map<Integer, Boolean> ids = this.ownersService.getPreprocessingRedirectIds(owner.getId());
            for (int i = 0; i < owner.getPreprocessingRedirects().size(); i++) {
                if (owner.getPreprocessingRedirects().get(i).getCondition().length() > 200) {
                    errors.rejectValue("preprocessingRedirects[" + i + "].condition", "error.owner.modifications.condition.length");
                }
                if (owner.getPreprocessingRedirects().get(i).getOperation().length() > 200) {
                    errors.rejectValue("preprocessingRedirects[" + i + "].operation", "error.owner.modifications.operation.length");
                }
                if (owner.getPreprocessingRedirects().get(i).getId() > 0 && !ids.containsKey(owner.getPreprocessingRedirects().get(i).getId())) {
                    errors.rejectValue("notFoundRedirects[" + i + "].id", "error.owner.modifications.id");
                }
            }
        }

        if (owner.getId() == 0) {
            if (ownersService.getOwnerByCode(owner.getCode()) != null) {
                errors.rejectValue("code", "error.owner.code.exist");
            }
        } else {
            Owner temp = ownersService.getOwnerByCode(owner.getCode());
            if (temp != null) {
                if (!owner.equals(temp)) {
                    errors.rejectValue("code", "error.owner.code.exist");
                }
            }
        }
        if (!owner.getColor().isEmpty()) {
            if (owner.getColor().length() != 6 && owner.getColor().length() != 3) {
                errors.rejectValue("color", "error.owner.color.length");
            } else if (!WebUtil.validateHexColor(owner.getColor())) {
                errors.rejectValue("color", "error.owner.color.form");
            }
        }

        if (!owner.getOpacity().isEmpty()) {
            String temp = owner.getOpacity();
            try {
                int opacityInt = this.converterService.strToInt(temp);
                temp = Integer.toString(opacityInt);
            } catch (NumberFormatException nfe) {
                temp = "255";
            }
            owner.setOpacity(temp);
            if (owner.getOpacity().length() < 1 && owner.getOpacity().length() > 3) {
                errors.rejectValue("opacity", "error.owner.opacity.length");
            } else if (!owner.getOpacity().matches("^\\d+$")) {
                errors.rejectValue("opacity", "error.owner.opacity.form");
            } else if (this.converterService.strToInt(owner.getOpacity()) > 255
                    || this.converterService.strToInt(owner.getOpacity()) < 0) {
                errors.rejectValue("opacity", "error.owner.opacity.value");
            }
        }
    }
}
