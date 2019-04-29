package com.example.myfirefighterapplicationv001;

public class GasValues {
    public GasValues(float ch4, float ibut, float o2, float co){
    CH4 = ch4;
    IBUT = ibut;
    O2 = o2;
    CO = co;

    }

    private float CH4;
    private float IBUT;
    private float O2;
    private float CO;

    public float getCH4() {
        return CH4;
    }


    public float getIBUT() {
        return IBUT;
    }

    public float getO2() {
        return O2;
    }

    public float getCO() {
        return CO;
    }

    public void setCH4(float CH4) {
        this.CH4 = CH4;
    }

    public void setIBUT(float IBUT) {
        this.IBUT = IBUT;
    }

    public void setO2(float o2) {
        this.O2 = o2;
    }

    public void setCO(float CO) {
        this.CO = CO;
    }

    public byte[] floatToByteArray(float value) {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] {
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }

    public byte[] getBytesCH4(){
        return floatToByteArray(this.CH4);
    }

    public byte[] getBytesIBUT(){
        return floatToByteArray(this.IBUT);
    }

    public byte[] getBytesO2(){
        return floatToByteArray(this.O2);
    }

    public byte[] getBytesCO(){
        return floatToByteArray(this.CO);
    }


}
