package numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

  private boolean running;
  private final ArrayList<String> wrongTokens = new ArrayList<>();
  Map<Property, Boolean> properties = new HashMap<>();

  public static void main(String[] args) {
    Main game = new Main();
    game.printIntro();
    game.running = true;
    while (game.running) {
      game.start();
    }
    String goodbyeMsg = "Goodbye!";
    System.out.println(goodbyeMsg);
  }

  private void printIntro() {
    String welcomeMsg = "Welcome to Amazing Numbers!\n\n"
        + "Supported requests:\n"
        + "- enter a natural number to know its properties;\n"
        + "- enter two natural numbers to obtain the properties of the list:\n"
        + "  * the first parameter represents a starting number;\n"
        + "  * the second parameter shows how many consecutive numbers are to be printed;\n"
        + "- two natural numbers and properties to search for;\n"
        + "- a property preceded by minus must not be present in numbers;\n"
        + "- separate the parameters with one space;\n"
        + "- enter 0 to exit.";
    System.out.println(welcomeMsg);
  }

  private void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("\nEnter a request: ");
    String input = scanner.nextLine();
    System.out.println();
    proceedInput(input);
  }

  private void proceedInput(String input) {
    if ("0".equals(input)) {
      running = false;
      return;
    }
    String[] tokens = input.split(" ");
    long inputNum;

    // proceed first argument
    try {
      inputNum = Long.parseLong(tokens[0]);
    } catch (NumberFormatException e) {
      System.out.println("The first parameter should be a natural number or zero.");
      return;
    }
    if (inputNum < 1) {
      System.out.println("The first parameter should be a natural number or zero.");
      return;
    }
    if (tokens.length == 1) {
      checkNumber(inputNum);
      return;
    }

    // proceed second argument
    int quantity;
    try {
      quantity = Integer.parseInt(tokens[1]);
    } catch (NumberFormatException e) {
      System.out.println("The second parameter should be a natural number.");
      return;
    }
    if (quantity < 1) {
      System.out.println("The second parameter should be a natural number.");
      return;
    }
    if (tokens.length == 2) {
      checkNumber(inputNum, quantity);
      return;
    }

    checkNumber(inputNum, quantity, tokens);
  }

  private void checkNumber(long num) {
    System.out.printf("Properties of %,d%n", num);
    StringBuilder propertiesMsg = new StringBuilder();
    int slotLength = 12;
    String propertyName;
    for (Property p : Property.values()) {
      propertyName = p.name().toLowerCase();
      propertiesMsg.append(" ".repeat(slotLength - propertyName.length()));
      propertiesMsg.append(propertyName);
      propertiesMsg.append(": ").append(p.func.apply(num)).append("\n");
    }
    System.out.println(propertiesMsg);
  }

  private void checkNumber(long num, int quantity) {
    long number = num;
    if (number < 1) {
      System.out.println("The first parameter should be a natural number or zero.");
      return;
    }
    for (int i = 0; i < quantity; i++) {
      String numResult = processOneNumber(number);
      System.out.println(numResult);
      number++;
    }
  }

  private void checkNumber(long start, int quantity, String[] tokens) {
    properties.clear();
    wrongTokens.clear();
    for (int i = 2; i < tokens.length; i++) {
      addProperty(tokens[i]);
    }
    if (properties.containsKey(null) && wrongTokens.size() == 0) {
      return;
    }
    if (properties.containsKey(null) && wrongTokens.size() == 1) {
      System.out.printf("The property %s is wrong.\n"
              + "Available properties: %s%n",
          wrongTokens, Arrays.toString(Property.values()));
      return;
    }
    if (properties.containsKey(null)) {
      System.out.printf("The properties %s are wrong.\n"
              + "Available properties: %s%n",
          wrongTokens, Arrays.toString(Property.values()));
      return;
    }

    if (properties.containsKey(Property.EVEN) && properties.containsKey(Property.ODD) &&
        properties.get(Property.EVEN) == properties.get(Property.ODD)) {
      System.out.printf("The request contains mutually exclusive properties: %s\n"
          + "There are no numbers with these properties.\n", "[" +
          (properties.get(Property.EVEN) ? "" : "-") + "EVEN, " +
          (properties.get(Property.ODD) ? "" : "-") + "ODD]");
      return;
    }
    if (properties.containsKey(Property.DUCK) && properties.containsKey(Property.SPY) &&
        properties.get(Property.DUCK) && properties.get(Property.SPY)) {
      System.out.printf("The request contains mutually exclusive properties: %s\n"
          + "There are no numbers with these properties.\n", "[DUCK, SPY]");
      return;
    }
    if (properties.containsKey(Property.SUNNY) && properties.containsKey(Property.SQUARE) &&
        properties.get(Property.SUNNY) && properties.get(Property.SQUARE)) {
      System.out.printf("The request contains mutually exclusive properties: %s\n"
          + "There are no numbers with these properties.\n", "[SUNNY, SQUARE]");
      return;
    }
    if (properties.containsKey(Property.HAPPY) && properties.containsKey(Property.SAD) &&
        properties.get(Property.HAPPY) == properties.get(Property.SAD)) {
      System.out.printf("The request contains mutually exclusive properties: %s\n"
          + "There are no numbers with these properties.\n", "[" +
          (properties.get(Property.HAPPY) ? "" : "-") + "HAPPY, " +
          (properties.get(Property.SAD) ? "" : "-") + "SAD]");
      return;
    }

    long num = start;
    long[] nums = new long[quantity];
    ArrayList<Boolean> result;
    for (int i = 0; i < nums.length; i++) {
      while (nums[i] == 0) {
        result = new ArrayList<>();
        for (Property p : properties.keySet()) {
          if (properties.get(p)) {
            result.add(p.func.apply(num));
          } else {
            result.add(!p.func.apply(num));
          }
        }
        if (!result.contains(false)) {
          nums[i] = num;
        }
        num++;
      }
    }

    for (long checkedNum : nums) {
      System.out.println(processOneNumber(checkedNum));
    }
  }

  private String processOneNumber(long num) {
    StringBuilder stringBuilder = new StringBuilder(num + " is ");
    for (Property p : Property.values()) {
      if (p.func.apply(num)) {
        stringBuilder.append(p.name().toLowerCase());
        stringBuilder.append(", ");
      }
    }
    stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
    return stringBuilder.toString();
  }

  private void addProperty(String token) {
    String propertyStr = token;
    Property property;
    boolean isDirect = true;
    try {
      if (token.charAt(0) == '-') {
        isDirect = false;
        propertyStr = propertyStr.substring(1);
      }
      property = Property.valueOf(propertyStr.toUpperCase());

    } catch (IllegalArgumentException e) {
      wrongTokens.add(token);
      properties.put(null, isDirect);
      return;
    }
    if (properties.containsKey(property) && properties.get(property) != isDirect) {
      System.out.printf("The request contains mutually exclusive properties: [-%s, %s]\n"
          + "There are no numbers with these properties.%n", property.name(), property.name());
      properties.put(null, isDirect);
      return;
    }
    properties.put(property, isDirect);
  }

  static boolean isEven(long num) {
    return num % 2 == 0;
  }

  static boolean isOdd(long num) {
    return num % 2 != 0;
  }

  static boolean isBuzz(long num) {
    return num % 7 == 0 || num % 10 == 7;
  }

  static boolean isDuck(long num) {
    String numStr = num + "";
    return numStr.contains("0");
  }

  static boolean isPalindromic(long num) {
    char[] nums = (num + "").toCharArray();
    int length = nums.length;
    if (length == 1) {
      return true;
    }
    for (int i = 0; i < length / 2; i++) {
      if (nums[i] != nums[length - 1 - i]) {
        return false;
      }
    }
    return true;
  }

  static boolean isGapful(long num) {
    String numStr = Long.toString(num);
    if (numStr.length() < 3) {
      return false;
    }
    String dividerStr = "" + numStr.charAt(0) + numStr.charAt(numStr.length() - 1);
    int divider = Integer.parseInt(dividerStr);
    return num % divider == 0;
  }

  static boolean isSpy(long num) {
    char[] digits = (num + "").toCharArray();
    long sum = 0;
    long mult = 1;
    for (char digit : digits) {
      sum += Character.getNumericValue(digit);
      mult *= Character.getNumericValue(digit);
    }
    return sum == mult;
  }

  static boolean isSunny(long num) {
    return isSquare(num + 1);
  }

  static boolean isSquare(long num) {
    double sqrt = Math.sqrt(num);
    return ((int) sqrt == sqrt);
  }

  static boolean isJumping(long num) {
    char[] chars = (num + "").toCharArray();
    int[] digits = new int[chars.length];
    for (int i = 0; i < chars.length; i++) {
      digits[i] = Character.getNumericValue(chars[i]);
    }
    for (int i = 0; i < digits.length - 1; i++) {
      if (Math.abs(digits[i] - digits[i + 1]) != 1) {
        return false;
      }
    }
    return true;
  }

  static boolean isHappy(long num) {
    long sum = num;
    List<Integer> digits = new ArrayList<>();
    Set<Long> visitedNumbers = new HashSet<>();
    visitedNumbers.add(num);
    while (sum != 1) {
      while (sum > 0) {
        digits.add((int) (sum % 10));
        sum /= 10;
      }
      sum = 0;
      for (int digit : digits) {
        sum += ((long) digit) * digit;
      }
      digits.clear();
      if (visitedNumbers.contains(sum)) {
        return false;
      }
      visitedNumbers.add(sum);
    }
    return true;
  }

  static boolean isSad(long num) {
    return !isHappy(num);
  }

}
