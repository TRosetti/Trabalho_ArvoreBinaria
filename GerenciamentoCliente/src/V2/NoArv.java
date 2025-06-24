package V2;
import dados.Item;

public class NoArv {
    private Item info; 
    private NoArv esq, dir;

    public NoArv(Item elem) {
        this.info = elem;
        this.esq = null;
        this.dir = null;
    }

    public NoArv getEsq() {
        return this.esq;
    }

    public NoArv getDir() {
        return this.dir;
    }

    public Item getInfo() {
        return this.info;
    }

    public void setEsq(NoArv no) {
        this.esq = no;
    }

    public void setDir(NoArv no) {
        this.dir = no;
    }

    public void setInfo(Item elem) {
        this.info = elem;
    }
}

