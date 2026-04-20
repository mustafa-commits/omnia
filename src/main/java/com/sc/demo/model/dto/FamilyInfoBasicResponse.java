package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record FamilyInfoBasicResponse(
       String FamilyNo,
       Long FollowId,
       Long FamilyPersonId,
       String PersonFullName,
       String RelationId,
       LocalDateTime Age,
       String Phone1,
       String Phone2,
       String Phone3
) {
}
