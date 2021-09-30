package com.fabbi.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.fabbi.dto.ReportBestSoldDTO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NamedNativeQuery(
		name = "find_best_sold",
		query = "SELECT p.name AS 'name', SUM(od.quantity) AS 'total' "
				+ "FROM order_detail od INNER JOIN product p ON od.product_id = p.id "
				+ "GROUP BY od.product_id "
				+ "ORDER BY SUM(od.quantity) DESC",
		resultSetMapping = "report_best_sold_dto"
)
@SqlResultSetMapping(
		name = "report_best_sold_dto",
		classes = @ConstructorResult(
				targetClass = ReportBestSoldDTO.class,
				columns = {
						@ColumnResult(name = "name", type = String.class),
						@ColumnResult(name = "total", type = Integer.class)
				}
		)
)
public class OrderDetail extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1052535215822894442L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "ORDER_DETAIL_ORD_FK"))
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "ORDER_DETAIL_PROD_FK"))
	private Product product;
	
	@Column
	private String thumbnail;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Column(name = "price", nullable = false)
	private Integer price;
	
	@Column(name = "amount", nullable = false)
	private Integer amount;
	
	@Column(name = "unit", nullable = false)
	private Integer unit;
}
