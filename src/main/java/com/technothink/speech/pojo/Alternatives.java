package com.technothink.speech.pojo;
public class Alternatives
{
    private String content;

    private String confidence;

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    public String getConfidence ()
    {
        return confidence;
    }

    public void setConfidence (String confidence)
    {
        this.confidence = confidence;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [content = "+content+", confidence = "+confidence+"]";
    }
}
	