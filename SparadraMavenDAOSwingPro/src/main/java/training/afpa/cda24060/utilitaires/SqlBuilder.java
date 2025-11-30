package training.afpa.cda24060.utilitaires;

public class SqlBuilder {

    private final StringBuilder sb = new StringBuilder();

    public SqlBuilder append(String s) {
        sb.append(s).append(" ");
        return this;
    }

    @Override
    public String toString() {
        return sb.toString().trim();
    }
}
