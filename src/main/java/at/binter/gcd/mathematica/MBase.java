package at.binter.gcd.mathematica;

public abstract class MBase implements HTMLWrapper {
    private boolean doLinebreaks = false;
    private boolean addSemicolon = false;

    public boolean isDoLinebreaks() {
        return doLinebreaks;
    }

    public void setDoLinebreaks(boolean doLinebreaks) {
        this.doLinebreaks = doLinebreaks;
    }

    public boolean isAddSemicolon() {
        return addSemicolon;
    }

    public void setAddSemicolon(boolean addSemicolon) {
        this.addSemicolon = addSemicolon;
    }

    @Override
    public String toHTML() {
        HTMLBuilder builder = new HTMLBuilder();
        toHTML(builder);
        return builder.toString();
    }
}
