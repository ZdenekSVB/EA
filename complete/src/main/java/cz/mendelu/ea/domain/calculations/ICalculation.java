package cz.mendelu.ea.domain.calculations;

public interface ICalculation {
    String getType();
    String getDescription();
    Object calculate();
}
