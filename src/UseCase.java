public class UseCase {

    // ===== ENUM =====
    enum WeightUnit {
        KILOGRAM(1.0),
        GRAM(0.001),
        POUND(0.453592);

        private final double toKgFactor;

        WeightUnit(double toKgFactor) {
            this.toKgFactor = toKgFactor;
        }

        double toKilogram(double value) {
            return value * toKgFactor;
        }

        double fromKilogram(double kgValue) {
            return kgValue / toKgFactor;
        }
    }

    // ===== VALUE CLASS =====
    static class QuantityWeight {
        private final double value;
        private final WeightUnit unit;

        public QuantityWeight(double value, WeightUnit unit) {
            if (unit == null || Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Invalid input");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toBase() {
            return unit.toKilogram(value);
        }

        public QuantityWeight convertTo(WeightUnit target) {
            double base = toBase();
            return new QuantityWeight(target.fromKilogram(base), target);
        }

        public QuantityWeight add(QuantityWeight other) {
            return add(other, this.unit);
        }

        public QuantityWeight add(QuantityWeight other, WeightUnit resultUnit) {
            double sumKg = this.toBase() + other.toBase();
            return new QuantityWeight(resultUnit.fromKilogram(sumKg), resultUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityWeight)) return false;

            QuantityWeight other = (QuantityWeight) obj;
            return Math.abs(this.toBase() - other.toBase()) < 1e-6;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight gram = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight pound = new QuantityWeight(2.20462, WeightUnit.POUND);

        // Equality
        System.out.println("kg == gram -> " + kg.equals(gram));
        System.out.println("kg == pound -> " + kg.equals(pound));

        // Conversion
        System.out.println("kg to gram -> " + kg.convertTo(WeightUnit.GRAM));
        System.out.println("kg to pound -> " + kg.convertTo(WeightUnit.POUND));

        // Addition
        System.out.println("kg + gram (kg) -> " +
                kg.add(gram, WeightUnit.KILOGRAM));

        System.out.println("kg + gram (gram) -> " +
                kg.add(gram, WeightUnit.GRAM));

        System.out.println("kg + pound (kg) -> " +
                kg.add(pound, WeightUnit.KILOGRAM));
    }
}