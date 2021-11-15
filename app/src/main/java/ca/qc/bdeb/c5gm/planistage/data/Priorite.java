package ca.qc.bdeb.c5gm.planistage.data;

import ca.qc.bdeb.c5gm.planistage.StageUtils;

public enum Priorite {
    ELEVEE(StageUtils.FLAG_ELEVEE),
    MOYENNE(StageUtils.FLAG_MOYEN),
    BASSE(StageUtils.FLAG_BAS);

    private byte flag;

    Priorite(byte flag) {
        this.flag = flag;
    }

    public byte getFlag() {
        return flag;
    }
}
