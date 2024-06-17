package com.WarcraftBingo.DatabaseFunctions;
import jakarta.persistence.*;

@Entity
@Table(name = "Raids", schema = "WarcraftBingo")
public class Database {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int keyid;

    @Column
    private String MoltenCore;

    @Column
    private String BlackwingLair;

    @Column
    private String ZulGurub;


    public String getBlackwingLair() {
        return BlackwingLair;
    }

    public void setBlackwingLair(String blackwingLair) {
        BlackwingLair = blackwingLair;
    }

    public String getZulGurub() {
        return ZulGurub;
    }

    public void setZulGurub(String zulGurub) {
        ZulGurub = zulGurub;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public String getMoltenCore() {
        return MoltenCore;
    }

    public void setMoltenCore(String moltenCore) {
        MoltenCore = moltenCore;
    }
}
