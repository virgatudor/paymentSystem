package net.javaguides.springboot.springsecurity.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="audit", schema="public")
public class Audit extends BaseEntity{
	
		
	
		

		private LocalDateTime time;
		
		private String operation;
		
		private String performer;
		
		private Long performedId;
		
		private String performed;
		
		public String getPerformed() {
			return performed;
		}

		public void setPerformer(String performer) {
			this.performer = performer;
		}

		public void setPerformedId(Long performedId) {
			this.performedId = performedId;
		}

		public Audit(LocalDateTime time, String operation, String performer, Long performedId,String performed) {
			super();
			this.time = time;
			this.operation = operation;
			this.performer = performer;
			this.performedId = performedId;
			this.performed = performed;
		}
		
		public Audit() {
			
		}
		
		public LocalDateTime getTime() {
			return time;
		}

		public void setTime(LocalDateTime time) {
			this.time = time;
		}

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public String getPerformer() {
			return performer;
		}

		public void setPerformed(String performer) {
			this.performer = performer;
		}
		
		public Long getPerformedId() {
			return performedId;
		}

		public void setPerformedDId(Long performerId) {
			this.performedId = performedId;
		}
		
}

