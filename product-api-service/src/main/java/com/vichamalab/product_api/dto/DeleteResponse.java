package com.vichamalab.product_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResponse {
	private int count;
	private Boolean status;
	private String message;
}
