package com.mockrunner.test;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TestBodyTag extends BodyTagSupport
{
    private String testString;
    private Integer testInteger;
    private double testDouble;
    private PageContext context;

    public double getTestDouble()
    {
        return testDouble;
    }

    public Integer getTestInteger()
    {
        return testInteger;
    }

    public String getTestString()
    {
        return testString;
    }

    public void setTestDouble(double testDouble)
    {
        this.testDouble = testDouble;
    }

    public void setTestInteger(Integer testInteger)
    {
        this.testInteger = testInteger;
    }

    public void setTestString(String testString)
    {
        this.testString = testString;
    }
    
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        this.context = context;
    }

    public PageContext getPageContext()
    {
        return context;
    }
}
