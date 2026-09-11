package com.sbowling.warmachine.war.calibration;

public record LinearRegression(double intercept, double slope) {

  public static LinearRegression calculate(double[] x, double[] y) {
    if (x.length != y.length) {
      throw new IllegalArgumentException("X and Y must contain the same number of observations.");
    }

    if (x.length < 2) {
      throw new IllegalArgumentException("At least two observations are required.");
    }

    double xMean = 0.0;
    double yMean = 0.0;

    for (int i = 0; i < x.length; i++) {
      xMean += x[i];
      yMean += y[i];
    }

    xMean /= x.length;
    yMean /= y.length;

    double numerator = 0.0;
    double denominator = 0.0;

    for (int i = 0; i < x.length; i++) {
      double xDifference = x[i] - xMean;
      double yDifference = y[i] - yMean;

      numerator += xDifference * yDifference;
      denominator += xDifference * xDifference;
    }

    if (denominator == 0.0) {
      throw new IllegalArgumentException("Cannot calculate regression with no variation in X.");
    }

    double slope = numerator / denominator;
    double intercept = yMean - slope * xMean;

    return new LinearRegression(intercept, slope);
  }
}
