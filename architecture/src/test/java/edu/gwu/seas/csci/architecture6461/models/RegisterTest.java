package edu.gwu.seas.csci.architecture6461.models;

import org.junit.Assert;
import org.junit.Test;

public class RegisterTest 
{
    private Register fourBitRegister = new Register(4);
    private Register twelveBitRegister = new Register(12);

    @Test
    public void testFourBitRegister()
    {
        this.fourBitRegister.setValue(0);
        Assert.assertEquals(0, fourBitRegister.getValue());
        this.fourBitRegister.setValue(13);
        Assert.assertEquals(13, fourBitRegister.getValue());
        this.fourBitRegister.setValue(25);
        Assert.assertEquals(9, fourBitRegister.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFourBitRegisterThrowsException()
    {
        this.fourBitRegister.setValue(-5);
    }

    @Test
    public void testTwelveBitRegister()
    {
        this.twelveBitRegister.setValue(0);
        Assert.assertEquals(0, twelveBitRegister.getValue());
        this.twelveBitRegister.setValue(5);
        Assert.assertEquals(5, twelveBitRegister.getValue());
        this.twelveBitRegister.setValue(4099);
        Assert.assertEquals(3, twelveBitRegister.getValue());
        this.twelveBitRegister.setValue(5099);
        Assert.assertEquals(1003, twelveBitRegister.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwelveBitRegisterThrowsException()
    {
        this.twelveBitRegister.setValue(-48);
    }
}