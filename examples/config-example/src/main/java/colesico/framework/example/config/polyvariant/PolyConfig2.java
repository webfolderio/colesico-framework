package colesico.framework.example.config.polyvariant;

import colesico.framework.config.Config;

@Config
public class PolyConfig2 extends PolyConfigPrototype {
    @Override
    public String getValue() {
        return "Poly2";
    }
}
