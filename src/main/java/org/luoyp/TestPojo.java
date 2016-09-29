package org.luoyp;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/27.
 */
public class TestPojo
{
	private String userId;
	private String licNo;
	private Float amount;
	private Date issueDate;
	private Integer age;

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

	public Float getAmount()
	{
		return amount;
	}

	public void setAmount(Float amount)
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
}
