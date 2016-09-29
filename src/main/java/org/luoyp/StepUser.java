package org.luoyp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "step_user")
public class StepUser
{
	@Id
	@Column(name = "user_id")
	private String userId;
	@Column(name = "lic_no")
	private String licNo;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "issue_date")
	private Date issueDate;
	@Column(name = "age")
	private Integer age;
	@Column(name = "create_date")
	private Long createDate;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLicNo()
	{
		return licNo;
	}

	public void setLicNo(String licNo)
	{
		this.licNo = licNo;
	}

	public BigDecimal getAmount()
	{
		return amount;
	}

	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}

	public Date getIssueDate()
	{
		return issueDate;
	}

	public void setIssueDate(Date issueDate)
	{
		this.issueDate = issueDate;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}
}
