public class UseCase {

    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double value) {
            return value / toFeetFactor;
        }
    }

    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null || !Double.isFinite(value)) {
                throw new IllegalArgumentException();
            }
            this.value = value;
            this.unit = unit;
        }

        public QuantityLength convertTo(LengthUnit targetUnit) {
            double base = unit.toFeet(value);
            double converted = targetUnit.fromFeet(base);
            return new QuantityLength(converted, targetUnit);
        }

        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException();
            }
            double base1 = this.unit.toFeet(this.value);
            double base2 = other.unit.toFeet(other.value);
            double sumBase = base1 + base2;
            double result = this.unit.fromFeet(sumBase);
            return new QuantityLength(result, this.unit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            QuantityLength other = (QuantityLength) obj;
            return Double.compare(unit.toFeet(value), other.unit.toFeet(other.value)) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static QuantityLength add(QuantityLength q1, QuantityLength q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException();
        }
        return q1.add(q2);
    }

    public static QuantityLength add(double v1, LengthUnit u1, double v2, LengthUnit u2) {
        QuantityLength q1 = new QuantityLength(v1, u1);
        QuantityLength q2 = new QuantityLength(v2, u2);
        return q1.add(q2);
    }

    public static void main(String[] args) {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println(q1.add(q2));
        System.out.println(add(12.0, LengthUnit.INCHES, 1.0, LengthUnit.FEET));
    }
}