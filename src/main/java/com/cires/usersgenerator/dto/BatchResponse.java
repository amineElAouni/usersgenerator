package com.cires.usersgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {

    private int usersImportedWithSuccessCount;

    private int usersNotImportedWithSuccessCount;
}
