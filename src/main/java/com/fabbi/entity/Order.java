package com.fabbi.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.fabbi.dto.ReportTotalMoneyDTO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NamedNativeQueries(value = {
		@NamedNativeQuery(
				name = "find_total_amount",
				query = "SELECT SUM(o.amount) AS 'total', DATE(o.created_date) AS 'date' "
						+ "FROM orders o "
						+ "GROUP BY DATE(o.created_date) "
						+ "ORDER BY SUM(o.amount) DESC",
				resultSetMapping = "report_total_amount_dto"
		),
		@NamedNativeQuery(
				name = "find_total_amount_by_date",
				query = "SELECT SUM(o.amount) AS 'total', DATE(o.created_date) AS 'date' "
						+ "FROM orders o "
						+ "WHERE DATE(o.created_date) = :searchDate "
						+ "GROUP BY DATE(o.created_date) "
						+ "ORDER BY SUM(o.amount) DESC",
				resultSetMapping = "report_total_amount_dto"
		)
})
@SqlResultSetMapping(
		name = "report_total_amount_dto",
		classes = @ConstructorResult(
				targetClass = ReportTotalMoneyDTO.class,
				columns = {
						@ColumnResult(name = "total", type = Integer.class),
						@ColumnResult(name = "date", type = Date.class)
				}
		)
)
public class Order extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7806375938098341255L;

	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "status")
	private Integer status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = true, foreignKey = @ForeignKey(name = "ORDER_CUS_FK"))
	private Customer customer;
	
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> items = new ArrayList<>();
}
