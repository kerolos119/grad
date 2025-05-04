package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public  class Auditable {
   @JsonIgnore
   private LocalDateTime modifiedAt;
   @JsonIgnore
   private String modifiedBy;
   @JsonIgnore
   private LocalDateTime createdAt;
   @JsonIgnore
   private String createdBy;

}

