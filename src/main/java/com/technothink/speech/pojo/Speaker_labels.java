package com.technothink.speech.pojo;

public class Speaker_labels
{
    private Segments[] segments;

    private String speakers;

    public Segments[] getSegments ()
    {
        return segments;
    }

    public void setSegments (Segments[] segments)
    {
        this.segments = segments;
    }

    public String getSpeakers ()
    {
        return speakers;
    }

    public void setSpeakers (String speakers)
    {
        this.speakers = speakers;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [segments = "+segments+", speakers = "+speakers+"]";
    }
}
	