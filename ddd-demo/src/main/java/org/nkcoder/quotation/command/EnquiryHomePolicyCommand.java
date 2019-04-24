package org.nkcoder.quotation.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class EnquiryHomePolicyCommand {

    @NotBlank(message = "buildingMaterial can not be empty")
    @ApiModelProperty(value = "buildingMaterial", example = "铝材")
    private String buildingMaterial;

    @NotBlank(message = "buildingType can not be empty")
    @ApiModelProperty(value = "buildingType", example = "别墅")
    private String buildingType;

    @NotBlank(message = "numberOfRooms can not be empty")
    @ApiModelProperty(value = "numberOfRooms", example = "二室")
    private String numberOfRooms;
}
