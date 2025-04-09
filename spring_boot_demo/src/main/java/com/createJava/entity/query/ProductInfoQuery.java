package com.createJava.entity.query;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: moZi
 * @date: 2025/3/29 20:46
 * @description: 商品信息查询对象
 */
public class ProductInfoQuery {

	/**
	 * 自增ID
 	 */
	private Integer id;

	/**
	 * 公司ID
 	 */
	private String companyId;

	private String companyIdFuzzy;

	/**
	 * 商品编号
 	 */
	private String code;

	private String codeFuzzy;

	/**
	 * 商品名称
 	 */
	private String productName;

	private String productNameFuzzy;

	/**
	 * 价格
 	 */
	private BigDecimal price;

	/**
	 * sku类型
 	 */
	private Integer skuType;

	/**
	 * 创建时间
 	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 创建时间
 	 */
	private Date createData;

	private String createDataStart;

	private String createDataEnd;

	/**
	 * 颜色类型
 	 */
	private Integer colorType;

	/**
	 * 库存
 	 */
	private Long stock;

	/**
	 * 状态
 	 */
	private Integer status;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	}

	public Integer getSkuType() {
		return this.skuType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateData(Date createData) {
		this.createData = createData;
	}

	public Date getCreateData() {
		return this.createData;
	}

	public void setColorType(Integer colorType) {
		this.colorType = colorType;
	}

	public Integer getColorType() {
		return this.colorType;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Long getStock() {
		return this.stock;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setCompanyIdFuzzy(String companyIdFuzzy) {
		this.companyIdFuzzy = companyIdFuzzy;
	}

	public String getCompanyIdFuzzy() {
		return this.companyIdFuzzy;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy() {
		return this.codeFuzzy;
	}

	public void setProductNameFuzzy(String productNameFuzzy) {
		this.productNameFuzzy = productNameFuzzy;
	}

	public String getProductNameFuzzy() {
		return this.productNameFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setCreateDataStart(String createDataStart) {
		this.createDataStart = createDataStart;
	}

	public String getCreateDataStart() {
		return this.createDataStart;
	}

	public void setCreateDataEnd(String createDataEnd) {
		this.createDataEnd = createDataEnd;
	}

	public String getCreateDataEnd() {
		return this.createDataEnd;
	}

}