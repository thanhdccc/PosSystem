package com.fabbi.constant;

public final class Constant {
	public static final int PAGE_SIZE = 4;
	
	public static final int CUSTOMER_TYPE_NEW = 1;
	public static final int CUSTOMER_TYPE_CLOSE = 2;
	
	public static final int USER_TYPE_MANAGER = 1;
	public static final int USER_TYPE_SELLER = 2;
	
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;
	
	public static final int UNIT_KILOGRAM = 0;
	public static final int UNIT_PIECE = 1;
	
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_FAIL = 1;
	
	public static final int ORDER_SEARCH_STATUS = 0;
	public static final int ORDER_SEARCH_CUSTOMER = 1;
	
	public static final int ORDER_STATUS_WAIT = 0;
	public static final int ORDER_STATUS_PROCESS = 1;
	
	public static final String ORDER_STATUS_WAIT_TEXT = "Wait";
	public static final String ORDER_STATUS_PROCESS_TEXT = "Process";
	
	public static final String UPLOAD_DIR = "./product-images";
	
	public static final String SESSION_NAME_CREATE = "orderSessionCreate";
	public static final String SESSION_NAME_EDIT = "orderSessionEdit";
	public static final String SESSION_ORDER_INFOR = "orderInforSession";
	public static final String SESSION_ORDER_TOTAL_AMOUNT = "orderTotalAmountSession";
	public static final String SESSION_CUSTOMER_INFOR = "customerInforSession";
	public static final String SESSION_EDIT_PRODUCT_CATE = "editProductCate";
	public static final String SESSION_EDIT_PRODUCT_SUPP = "editProductSupp";
	
	public static final int ORDER_FORM_CREATE = 0;
	public static final int ORDER_FORM_EDIT = 1;
	
	public static final String EXPORT_EXCEL_HEADER_NAME = "Content-Disposition";
	public static final String EXPORT_EXCEL_HEADER_VALUE_TOTAL_MONEY = "attachment; filename=report_total_money.xlsx";
	public static final String EXPORT_EXCEL_HEADER_VALUE_PRODUCT = "attachment; filename=report_best_sold_product.xlsx";
	public static final String EXPORT_EXCEL_HEADER_VALUE_TOTAL_MONEY_PDF = "attachment; filename=report_total_money.pdf";
	public static final String EXPORT_EXCEL_HEADER_VALUE_PRODUCT_PDF = "attachment; filename=report_best_sold_product.pdf";
}
