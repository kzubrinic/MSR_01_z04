package hr.unidu.kz.msr_zadatak_04;

public class Poruka {
    private int id;
    private String odkoga;
    private String kome;
    private String poruka;
    public Poruka(){}
    public Poruka(int id, String odkoga, String kome, String poruka) {
        this.id = id;
        this.odkoga = odkoga;
        this.kome = kome;
        this.poruka = poruka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOdkoga() {
        return odkoga;
    }

    public void setOdkoga(String odkoga) {
        this.odkoga = odkoga;
    }

    public String getKome() {
        return kome;
    }

    public void setKome(String kome) {
        this.kome = kome;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
}
